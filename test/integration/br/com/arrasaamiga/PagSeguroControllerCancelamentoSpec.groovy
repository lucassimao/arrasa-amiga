package br.com.arrasaamiga

import br.com.uol.pagseguro.domain.Sender
import br.com.uol.pagseguro.domain.Transaction
import br.com.uol.pagseguro.domain.TransactionStatus
import grails.plugin.springsecurity.SpringSecurityUtils
import spock.lang.Specification

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull

/**
 *
 */
class PagSeguroControllerCancelamentoSpec extends Specification {

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
        def parnaiba = new Cidade(nome: 'Parnaíba', uf: Uf.piaui).save(flush: true)

        assertNotNull Uf.piaui
        assertNotNull Cidade.teresina

        def user1 = new Usuario(username: 'cliente1@gmail.com', password: '12345', enabled: true,
                accountExpired: false, accountLocked: false, passwordExpired: false).save(flush: true)

        def user2 = new Usuario(username: 'cliente2@gmail.com', password: '123456', enabled: true,
                accountExpired: false, accountLocked: false, passwordExpired: false).save(flush: true)

        assert 2 == Usuario.count()

        new Cliente(nome: 'Cliente de parnaiba', usuario: user1, endereco: new Endereco(cidade: parnaiba, uf: Uf.piaui,cep:'64023620')).save(flush: true)
        new Cliente(nome: 'Cliente de Teresina', usuario: user2, endereco: new Endereco(cidade: Cidade.teresina, uf: Uf.piaui)).save(flush: true)

        assert 2 == Cliente.count()

        def produto1 = new Produto(descricao: 'Produto 1', nome: 'P1', tipoUnitario: 'un', unidades: ['un'] as List,
                precoAPrazoEmCentavos: 1100, precoAVistaEmCentavos: 1000)
        produto1.save(flush: true)

        def produto2 = new Produto(descricao: 'Produto 2', nome: 'P2', tipoUnitario: 'un', unidades: ['un'] as List,
                precoAPrazoEmCentavos: 2200, precoAVistaEmCentavos: 2000)
        produto2.save(flush: true)

        assert 2 == Produto.count()

        new Estoque(produto: produto1, unidade: 'un', quantidade: 10).save(flush: true)
        new Estoque(produto: produto2, unidade: 'un', quantidade: 5).save(flush: true)

        assert 2 == Estoque.count()
    }

    def cleanup() {
    }

    void "test transacao cancelada pelo pagseguro imediatamente"() {
        given:
            def transacaoPagSeguro = 'CODIGO-TRANSACAO-PAGSEGURO'
            def notificationCode = 'CODIGO-DE-NOTIFICACAO-PAG-SEGURO'
            def produto1 = Produto.findByNome('P1')

            SpringSecurityUtils.reauthenticate 'cliente1@gmail.com', '12345'

            def mockEmailService = Mock(EmailService)

            def shoppingCartController = new ShoppingCartController()
            shoppingCartController.emailService = mockEmailService

            def pagSeguroServiceSpy = Spy(PagSeguroService)
            pagSeguroServiceSpy.checkTransaction(notificationCode) >> new Transaction(status: TransactionStatus.CANCELLED,discountAmount: 0,
                feeAmount: 1,sender: new Sender(email:'cliente1@gmail.com'))
            pagSeguroServiceSpy.getTransaction(transacaoPagSeguro) >> new Transaction(status: TransactionStatus.CANCELLED,discountAmount: 0,
                                                                                    feeAmount: 1,sender: new Sender(email:'cliente1@gmail.com'))

            def pagSeguroController = new PagSeguroController()
            pagSeguroController.emailService = mockEmailService
            pagSeguroController.pagSeguroService = pagSeguroServiceSpy



        expect:
            Venda.count() == 0
            ItemVenda.count() == 0
            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 10

        when:
            shoppingCartController.request.method = 'POST'

            shoppingCartController.params.id = produto1.id
            shoppingCartController.params.unidade = 'un'
            shoppingCartController.params.quantidade = 3

            shoppingCartController.add()

        then:
            shoppingCartController.session.shoppingCart.getQuantidade(produto1, 'un') == 3
            shoppingCartController.response.reset()


        when:
            shoppingCartController.params.formaPagamento = FormaPagamento.PagSeguro.name()
            shoppingCartController.fecharVenda()

            sessionFactory.currentSession.flush()
            sessionFactory.currentSession.clear()

        then:
            shoppingCartController.response.redirectedUrl.startsWith('https://pagseguro.uol.com.br/v2/checkout/payment.html?code=')

            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 7
            Venda.count() == 1
            ItemVenda.count() == 1

            def venda = Venda.first()

            venda.formaPagamento == FormaPagamento.PagSeguro
            venda.status == StatusVenda.AguardandoPagamento

            assertNotNull Venda.first().itensVenda.find{item-> item.unidade == 'un' && item.produto.id == produto1.id}

            // garantindo que o cliente e a loja so sao avisados quando o cliente efetivamente concui a compra
            0 * mockEmailService.notificarAdministradores(_)
            0 * mockEmailService.notificarCliente(_)

            // o carrinho nao eh esvaziado, p/ caso nao dê certo da primeira vez e assim o cliente pode tentar novamente
            assertNotNull shoppingCartController.session.shoppingCart


        when: "PagSeguro redireciona p/ o site"
            pagSeguroController.response.reset()

            pagSeguroController.params.transacao = transacaoPagSeguro
            pagSeguroController.params.id = venda.id
            pagSeguroController.retorno()

            sessionFactory.currentSession.flush()
            sessionFactory.currentSession.clear()

            venda.refresh()

        then:

            venda.formaPagamento == FormaPagamento.PagSeguro
            venda.status == StatusVenda.Cancelada

            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 10
            pagSeguroController.response.redirectedUrl == '/venda/cancelada'

            // itens permanecem ainda no carrinho, cliente pode tentar novamente
            assertNotNull pagSeguroController.session.shoppingCart.itens.find{item-> item.unidade == 'un' && item.produto.id == produto1.id}

        when: "pagseguro envia notificação assincrona de cancelamento"


            pagSeguroController = new PagSeguroController()
            pagSeguroController.pagSeguroService = pagSeguroServiceSpy
            pagSeguroController.params.id = venda.id
            pagSeguroController.params.notificationCode = notificationCode

            pagSeguroController.notificacoes()

        then:
            pagSeguroController.response.text == 'ok'
            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 10
    }


    void "test transacao cancelada pelo pagseguro assincronamente"() {
        given:
            def transacaoPagSeguro = 'CODIGO-TRANSACAO-PAGSEGURO'
            def notificationCode = 'CODIGO-DE-NOTIFICACAO-PAG-SEGURO'
            def produto1 = Produto.findByNome('P1')

            SpringSecurityUtils.reauthenticate 'cliente2@gmail.com', '123456'

            def mockEmailService = Mock(EmailService)

            def shoppingCartController = new ShoppingCartController()
            shoppingCartController.emailService = mockEmailService

            def pagSeguroServiceSpy = Spy(PagSeguroService)

            def transaction = new Transaction(status: TransactionStatus.CANCELLED, discountAmount: 0,code:transacaoPagSeguro,
                feeAmount: 1, sender: new Sender(email: 'cliente2@gmail.com'))

            pagSeguroServiceSpy.checkTransaction(notificationCode) >> transaction
            pagSeguroServiceSpy.getTransaction(transacaoPagSeguro) >> transaction

            def pagSeguroController = new PagSeguroController()
            pagSeguroController.emailService = mockEmailService
            pagSeguroController.pagSeguroService = pagSeguroServiceSpy



        expect:
            Venda.count() == 0
            ItemVenda.count() == 0
            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 10

        when:
            shoppingCartController.request.method = 'POST'

            shoppingCartController.params.id = produto1.id
            shoppingCartController.params.unidade = 'un'
            shoppingCartController.params.quantidade = 3

            shoppingCartController.add()

        then:
            shoppingCartController.session.shoppingCart.getQuantidade(produto1, 'un') == 3
            shoppingCartController.response.reset()

        when: " venda vai ser fechada "
            shoppingCartController.params.dataEntrega = shoppingCartController.proximosDiasDeEntrega[0].time
            shoppingCartController.params.formaPagamento = FormaPagamento.PagSeguro.name()
            shoppingCartController.fecharVenda()

            sessionFactory.currentSession.flush()
            sessionFactory.currentSession.clear()

        then:
            shoppingCartController.response.redirectedUrl.startsWith('https://pagseguro.uol.com.br/v2/checkout/payment.html?code=')

            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 7
            Venda.count() == 1
            ItemVenda.count() == 1

            def venda = Venda.first()

            venda.formaPagamento == FormaPagamento.PagSeguro
            venda.status == StatusVenda.AguardandoPagamento
            assertNull venda.transacaoPagSeguro

            assertNotNull Venda.first().itensVenda.find{item-> item.unidade == 'un' && item.produto.id == produto1.id}

            // garantindo que o cliente e a loja so sao avisados quando o cliente efetivamente concui a compra
            0 * mockEmailService.notificarAdministradores(_)
            0 * mockEmailService.notificarCliente(_)

            // o carrinho nao eh esvaziado, p/ caso nao dê certo da primeira vez e assim o cliente pode tentar novamente
            assertNotNull shoppingCartController.session.shoppingCart


        when: "algum tempo depois pagseguro envia notificação assincrona de cancelamento"

            pagSeguroController = new PagSeguroController()
            pagSeguroController.pagSeguroService = pagSeguroServiceSpy
            pagSeguroController.params.id = venda.id
            pagSeguroController.params.notificationCode = notificationCode

            pagSeguroController.notificacoes()

            sessionFactory.currentSession.flush()
            sessionFactory.currentSession.clear()
            venda.refresh()

        then:
            venda.status == StatusVenda.Cancelada
            venda.transacaoPagSeguro == transacaoPagSeguro
            Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade == 10

            1 * mockEmailService.notificarAdministradores(_)
            1 * mockEmailService.notificarCliente(_)
    }
}
