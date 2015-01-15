package br.com.arrasaamiga

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.test.spock.IntegrationSpec

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull

/**
 *
 */
class FecharVendaSpec extends IntegrationSpec {


    def sessionFactory


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


    void "test fechar venda a vista "() {
        given:
            def produto1 = Produto.findByNome('P1')
            def produto2 = Produto.findByNome('P2')

            SpringSecurityUtils.reauthenticate 'cliente1@gmail.com', '12345'
            def mockEmailService = Mock(EmailService)

            def controller = new ShoppingCartController()
            controller.emailService = mockEmailService

        expect:
            Venda.count() == 0
            ItemVenda.count() == 0
            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 10

        when:

            controller.request.method = 'POST'

            controller.params.id = produto1.id
            controller.params.unidade = 'un'
            controller.params.quantidade = 3

            controller.add()

            then:
            controller.session.shoppingCart.getQuantidade(produto1, 'un') == 3
            controller.response.reset()


        when: "adicionando produto 2"

            controller.request.method = 'POST'

            controller.params.id = produto2.id
            controller.params.unidade = 'un'
            controller.params.quantidade = 4

            controller.add()

            then:
            controller.session.shoppingCart.getQuantidade(produto1, 'un') == 3
            controller.session.shoppingCart.getQuantidade(produto2, 'un') == 4
            controller.response.reset()


        when:
            controller.params.dataEntrega = controller.proximosDiasDeEntrega[0].time
            controller.params.formaPagamento = FormaPagamento.AVista.name()

            controller.fecharVenda()
            sessionFactory.currentSession.flush()
            sessionFactory.currentSession.clear()

        then:
            Venda.count() == 1
            ItemVenda.count() == 2

            def venda = Venda.first()

            controller.response.redirectedUrl == "/venda/show/${venda.id}"

            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 7
            Estoque.findByProdutoAndUnidade(produto2, 'un').quantidade == 1


            venda.formaPagamento == FormaPagamento.AVista
            venda.status == StatusVenda.AguardandoPagamento

            assertNotNull Venda.first().itensVenda.find{item-> item.unidade == 'un' && item.produto.id == produto1.id}
            assertNotNull Venda.first().itensVenda.find{item-> item.unidade == 'un' && item.produto.id == produto2.id}

            1 * mockEmailService.notificarAdministradores(_)
            1 * mockEmailService.notificarCliente(_)

            assertNull controller.session.shoppingCart


    }


    void "test fechar venda pagseguro "() {
        given:
            def produto1 = Produto.findByNome('P1')

            SpringSecurityUtils.reauthenticate 'cliente1@gmail.com', '12345'

            def mockEmailService = Mock(EmailService)

            def controller = new ShoppingCartController()
            controller.emailService = mockEmailService

        expect:
            Venda.count() == 0
            ItemVenda.count() == 0
            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 10

        when:
            controller.request.method = 'POST'

            controller.params.id = produto1.id
            controller.params.unidade = 'un'
            controller.params.quantidade = 3

            controller.add()

        then:
            controller.session.shoppingCart.getQuantidade(produto1, 'un') == 3
            controller.response.reset()


        when:
            controller.params.dataEntrega = controller.proximosDiasDeEntrega[0].time
            controller.params.formaPagamento = FormaPagamento.PagSeguro.name()

            controller.fecharVenda()

            sessionFactory.currentSession.flush()
            sessionFactory.currentSession.clear()

        then:
            controller.response.redirectedUrl.startsWith('https://pagseguro.uol.com.br/v2/checkout/payment.html?code=')

            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 7
            Venda.count() == 1
            ItemVenda.count() == 1

            def venda = Venda.first()

            venda.formaPagamento == FormaPagamento.PagSeguro
            venda.status == StatusVenda.AguardandoPagamento

            assertNotNull Venda.first().itensVenda.find{item-> item.unidade == 'un' && item.produto.id == produto1.id}

            // garantindo que o cliente e a loja so sao avisados quando o cliente efetivamente concui a compra
            1 * mockEmailService.notificarAdministradores(_)
            0 * mockEmailService.notificarCliente(_)

            // o carrinho nao eh esvaziado, p/ caso nao dê certo da primeira vez e assim o cliente pode tentar novamente
            assertNotNull controller.session.shoppingCart


    }


}
