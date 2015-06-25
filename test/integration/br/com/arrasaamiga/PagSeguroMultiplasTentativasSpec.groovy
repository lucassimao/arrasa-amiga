package br.com.arrasaamiga

import grails.plugin.springsecurity.SpringSecurityUtils
import spock.lang.Specification

import static org.junit.Assert.assertNotEquals
import static org.junit.Assert.assertNotNull

/**
 * Created by lucas.simao on 25/06/2015.
 */
class PagSeguroMultiplasTentativasSpec extends Specification {

    def sessionFactory
    def vendaService
    Venda vendaParaCliente2Concluida = null
    Venda vendaParaCliente2NAOConcluida = null
    Cliente cliente1 = null
    Cliente cliente2 = null

    def setup(){
        Estoque.executeUpdate('delete from ItemVenda')
        Estoque.executeUpdate('delete from Venda')
        Estoque.executeUpdate('delete from Estoque')
        Produto.executeUpdate('delete from Produto')
        Produto.executeUpdate('delete from Cliente')
        Produto.executeUpdate('delete from Usuario')
        Produto.executeUpdate('delete from Usuario')
        Produto.executeUpdate('delete from Cidade')
        Produto.executeUpdate('delete from Uf')


        new Uf(nome: 'Piaui', sigla: 'PI').save(flush: true)
        new Cidade(nome: 'Teresina', uf: Uf.piaui).save(flush: true)

        assertNotNull Uf.piaui
        assertNotNull Cidade.teresina

        def user2 = new Usuario(username: 'cliente2@gmail.com', password: '12345', enabled: true,
                accountExpired: false, accountLocked: false, passwordExpired: false).save(flush: true)

        cliente2 = new Cliente(nome: 'Cliente teste2', usuario: user2,
                                endereco: new Endereco(cidade: Cidade.teresina, uf: Uf.piaui),telefone: '91182840',dddTelefone: '22').save(flush: true)

        def user1 = new Usuario(username: 'cliente1@gmail.com', password: '12345', enabled: true,
                accountExpired: false, accountLocked: false, passwordExpired: false).save(flush: true)

        cliente1 = new Cliente(nome: 'Cliente teste', usuario: user1, endereco: new Endereco(cidade: Cidade.teresina, uf: Uf.piaui),telefone: '98182840',dddTelefone: '22').save(flush: true)

        assert 2 == Usuario.count()
        assert 2 == Cliente.count()

        def produto1 = new Produto(descricao: 'Produto 1', nome: 'P1', tipoUnitario: 'un', unidades: ['un'] as List,
                precoAPrazoEmCentavos: 1100,precoAVistaEmCentavos: 1000)
        produto1.save(flush: true)

        def produto2 = new Produto(descricao: 'Produto 2', nome: 'P2', tipoUnitario: 'un', unidades: ['un'] as List,
                precoAPrazoEmCentavos: 2200,precoAVistaEmCentavos: 2000)
        produto2.save(flush: true)

        assert 2 == Produto.count()

        new Estoque(produto: produto1, unidade: 'un', quantidade: 10).save(flush: true)
        new Estoque(produto: produto2, unidade: 'un', quantidade: 5).save(flush: true)

        assert 2 == Estoque.count()

        vendaParaCliente2Concluida = new Venda()
        vendaParaCliente2Concluida.cliente = cliente2
        vendaParaCliente2Concluida.formaPagamento = FormaPagamento.PagSeguro
        vendaParaCliente2Concluida.status = StatusVenda.PagamentoRecebido
        vendaParaCliente2Concluida.dataEntrega = vendaService.getProximosDiasDeEntrega()[0]
        vendaParaCliente2Concluida.carrinho = new ShoppingCart(itens:[new ItemVenda(produto: produto1,quantidade: 5,unidade: 'un')])

        vendaService.salvarVenda(vendaParaCliente2Concluida,false)

        vendaParaCliente2NAOConcluida = new Venda()
        vendaParaCliente2NAOConcluida.cliente = cliente2
        vendaParaCliente2NAOConcluida.formaPagamento = FormaPagamento.PagSeguro
        vendaParaCliente2NAOConcluida.status = StatusVenda.AguardandoPagamento
        vendaParaCliente2NAOConcluida.dataEntrega = vendaService.getProximosDiasDeEntrega()[1]
        vendaParaCliente2NAOConcluida.carrinho = new ShoppingCart(itens:[new ItemVenda(produto: produto2,quantidade: 2,unidade: 'un')])

        vendaService.salvarVenda(vendaParaCliente2NAOConcluida,false)


    }

    void "test cliente tenta comprar muliplas vezes com forma de pagamento pagseguro"(){
        setup:
            Venda primeiraVendaTentativa = null
            def produto1 = Produto.findByNome('P1')
            def produto2 = Produto.findByNome('P2')
            SpringSecurityUtils.reauthenticate cliente1.usuario.username, '12345'

            def mockEmailService = Mock(EmailService)

            def controller = new ShoppingCartController()
            controller.vendaService.emailService = mockEmailService
        expect:
             Venda.count() == 2
             ItemVenda.count() == 2
             Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 5
             Estoque.findByProdutoAndUnidade(produto2, 'un').quantidade == 3

        when:
            controller.request.method = 'POST'
            controller.params.id = produto1.id
            controller.params.unidade = 'un'
            controller.params.quantidade = 5

            controller.apagarPedidosNaoConcluidos() // eh chamado antes do add pelo Grails - remove tentativas de compra do usuario atual
            controller.add()

        then:
            controller.session.shoppingCart.getQuantidade(produto1, 'un') == 5
            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 5
            vendaParaCliente2Concluida.id in Venda.list()*.id
            vendaParaCliente2NAOConcluida.id in Venda.list()*.id

        when:
            controller.response.reset()
            controller.params.dataEntrega = controller.vendaService.proximosDiasDeEntrega[0].time
            controller.params.formaPagamento = FormaPagamento.PagSeguro.name()
            controller.apagarPedidosNaoConcluidos()
            controller.fecharVenda()

            sessionFactory.currentSession.flush()
            sessionFactory.currentSession.clear()
        then: "Cliente tenta fazer a 1ª compra"
            controller.response.redirectedUrl.startsWith('https://pagseguro.uol.com.br/v2/checkout/payment.html?code=')

            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 0
            Venda.count() == 3
            Venda.countByCliente(cliente2) == 2
            Venda.countByCliente(cliente1) == 1

            def venda = Venda.findByCliente(cliente1)

            venda.formaPagamento == FormaPagamento.PagSeguro
            venda.status == StatusVenda.AguardandoPagamento

            assertNotNull venda.itensVenda.find{item-> item.unidade == 'un' && item.produto.id == produto1.id}

            // garantindo que o cliente e a loja so sao avisados quando o cliente efetivamente concui a compra
            1 * mockEmailService.notificarAdministradores(_)
            0 * mockEmailService.notificarCliente(_)

            // o carrinho nao eh esvaziado, p/ caso nao dê certo da primeira vez e assim o cliente pode tentar novamente
            assertNotNull controller.session.shoppingCart
            controller.session.shoppingCart.getQuantidade(produto1, 'un') == 5

        when: " por algum motivo ele volta a tela de checkout e tenta fechar o pedido novamente - o sistema detecta uma venda anterior ñ concluida feita pelo mesmo usuario e apaga "
            primeiraVendaTentativa = Venda.findByCliente(cliente1)

            controller.response.reset()
            controller.params.dataEntrega = controller.vendaService.proximosDiasDeEntrega[1].time // usa outra data
            controller.params.formaPagamento = FormaPagamento.PagSeguro.name()
            controller.apagarPedidosNaoConcluidos()
            controller.fecharVenda()

            sessionFactory.currentSession.flush()
            sessionFactory.currentSession.clear()
        then: "resultado esperado apos 2 tentativa do Cliente"
            controller.response.redirectedUrl.startsWith('https://pagseguro.uol.com.br/v2/checkout/payment.html?code=')

            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 0
            Venda.count() == 3
            Venda.countByCliente(cliente2) == 2
            Venda.countByCliente(cliente1) == 1

            Venda.findByCliente(cliente1).id != primeiraVendaTentativa.id

    }

}
