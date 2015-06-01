package br.com.arrasaamiga

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.test.spock.IntegrationSpec

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * Created by lsimaocosta on 11/05/15.
 */
class VerificarEstoqueAntesDeFecharVendaSpec extends IntegrationSpec {

    def sessionFactory

    static transactional=false

    def setup() {

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

        def user1 = new Usuario(username: 'cliente1@gmail.com', password: '12345', enabled: true,
                accountExpired: false, accountLocked: false, passwordExpired: false).save(flush: true)
        assert 1 == Usuario.count()

        def cliente = new Cliente(nome: 'Cliente teste', usuario: user1, endereco: new Endereco(cidade: Cidade.teresina, uf: Uf.piaui)).save(flush: true)
        assert 1 == Cliente.count()

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
    }

    /**
     * este teste garante que caso um cliente inicie um pedido e nao finalize,
     * caso um outro cliente venha e esgote o stoque do mesmo item, o 1º cliente
     * não consegue mais fechar a compra devido a indisponibilidade do item
     */
    void "test cliente adiciona itens no carrinho, demora pra finalizar e outro cliente compra antes"(){
        given:
            def produto1 = Produto.findByNome('P1')
            def produto2 = Produto.findByNome('P2')

            SpringSecurityUtils.reauthenticate 'cliente1@gmail.com', '12345'
            def mockEmailService = Mock(EmailService)

            def controller = new ShoppingCartController()
            controller.vendaService.emailService = mockEmailService

        expect:
            Venda.count() == 0
            ItemVenda.count() == 0
            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 10
            Estoque.findByProdutoAndUnidade(produto2, 'un').quantidade == 5

        when: "adicionando 5 unid do produto 1"

            controller.request.method = 'POST'

            controller.params.id = produto1.id
            controller.params.unidade = 'un'
            controller.params.quantidade = 5

            controller.add()

        then:
            controller.session.shoppingCart.getQuantidade(produto1, 'un') == 5
            controller.response.reset()


        when: "adicionando 4 unidades do produto 2"

            controller.request.method = 'POST'

            controller.params.id = produto2.id
            controller.params.unidade = 'un'
            controller.params.quantidade = 4

            controller.add()

        then:
            controller.session.shoppingCart.getQuantidade(produto1, 'un') == 5
            controller.session.shoppingCart.getQuantidade(produto2, 'un') == 4

            // produtos ainda estao em estoque pq a venda ao cliente 1 ainda nao finalizada
            10 == Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade
            5 == Estoque.findByProdutoAndUnidade(produto2, 'un').quantidade

            controller.response.reset()

        when: " um 2º cliente vem e finaliza um pedido com todas as unidade do Produto 1 disponiveis em estoque"
            def e1 = Estoque.findByProduto(produto1)
            e1.quantidade = 0
            e1.save(flush:true)

        then:
            Estoque.findByProduto(produto1).quantidade==0

        when: " 1º cliente finalmente vem finalizar o pedido"
            controller.params.dataEntrega = controller.vendaService.proximosDiasDeEntrega[0].time
            controller.params.formaPagamento = FormaPagamento.AVista.name()

            controller.fecharVenda()
        then:
            controller.response.redirectedUrl.endsWith("/checkout")
            controller.flash.message.contains(produto1.nome) // garantido que a msg exibira o produto em falta no estoque

            assertEquals 0,Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade;
            assertEquals 5,  Estoque.findByProdutoAndUnidade(produto2, 'un').quantidade;

            0 * mockEmailService.notificarAdministradores(_)
            0 * mockEmailService.notificarCliente(_)

            assertNotNull controller.session.shoppingCart
            controller.session.shoppingCart.getQuantidade(produto1, 'un') == 0
            controller.session.shoppingCart.getQuantidade(produto2, 'un') == 4

        when: " um 3º cliente vem e finaliza um pedido com todas as unidade do Produto 2 disponiveis em estoque"

            def e2 = Estoque.findByProduto(produto2)
            e2.quantidade = 0
            e2.save(flush: true)

            controller.response.reset()

        then:
            Estoque.findByProduto(produto2).quantidade==0

        when: " 1º cliente tenta comprar somente o 2º produto, mudando forma de pagamento, mas tb n consegue "
            controller.params.dataEntrega = controller.vendaService.proximosDiasDeEntrega[0].time
            controller.params.formaPagamento = FormaPagamento.PagSeguro.name()

            controller.fecharVenda()
            sessionFactory.currentSession.clear()

        then:
            controller.response.redirectedUrl.endsWith("/checkout")
            controller.flash.message.contains(produto2.nome) // garantido que a msg exibira o produto em falta no estoque

            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 0;
            Estoque.findByProdutoAndUnidade(produto2, 'un').quantidade == 0;

            0 * mockEmailService.notificarAdministradores(_)
            0 * mockEmailService.notificarCliente(_)

            assertNotNull controller.session.shoppingCart
            controller.session.shoppingCart.getQuantidade(produto1, 'un') == 0
            controller.session.shoppingCart.getQuantidade(produto2, 'un') == 0


    }
}
