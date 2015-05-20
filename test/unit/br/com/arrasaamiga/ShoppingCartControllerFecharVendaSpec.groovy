package br.com.arrasaamiga

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.commons.InstanceFactoryBean
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(ShoppingCartController)
@Mock([ItemVenda,ShoppingCart,Venda])
class ShoppingCartControllerFecharVendaSpec extends Specification {

    def doWithSpring = {
        def springSecurityServiceMock = mockFor(SpringSecurityService)
        springSecurityServiceMock.demandExplicit.getCurrentUser(1..10){-> return Usuario.load(1) }

        def emailServiceMock = mockFor(EmailService)
        emailServiceMock.demandExplicit.notificarAdministradores(1){->  }
        emailServiceMock.demandExplicit.notificarCliente(1){->  }

        def vendaServiceMock = mockFor(VendaService)
        vendaServiceMock.demandExplicit.salvarVenda(1){Venda v,boolean b->  }

        springSecurityService(InstanceFactoryBean, springSecurityServiceMock.createMock(), SpringSecurityService)
        emailService(InstanceFactoryBean, emailServiceMock.createMock(), EmailService)
        vendaService(InstanceFactoryBean, vendaServiceMock.createMock(),VendaService)
    }

    def setup() {
        Usuario.metaClass.encodePassword = { null }
        Cliente.metaClass.reautenticar = { null }

        mockDomain(Usuario,[[id:1,username:'user1@gmail.com',password:'123'],
                            [id:2,username:'user2@gmail.com',password:'123']])

        mockDomain(Uf,[[id:1,nome:'Piaui',sigla:'PI'], [id:2,nome:'Maranhao',sigla:'MA']])
        mockDomain(Cidade,[[id:1,nome:'Teresina',uf:Uf.piaui], [id:2,nome:'Sao Luis', uf:Uf.get(2)]])

        mockDomain(Cliente,[[id:1, usuario: Usuario.load(1),nome: 'Cliente Dentro da Area de Entrega Rapida',
                             endereco: new Endereco(cidade: Cidade.teresina,uf: Uf.piaui)],
                            [id:2, usuario: Usuario.load(2),nome: 'Cliente Fora da Area de Entrega Rapida',
                            endereco: new Endereco(cidade: Cidade.get(2),uf: Uf.get(2))]])

        mockDomain(Produto, [
                [nome: "Produto1", id: 1L, descricao: 'Produto 1', tipoUnitario: 'un',unidades:['un1']],
                [nome: "Produto2", id: 2L, descricao: 'Produto 2', tipoUnitario: 'un',unidades:['un2-a','un2-b']]])

        mockDomain(Estoque,[ [produto: Produto.load(1L), unidade:'un1',quantidade:10],
                             [produto: Produto.load(2L), unidade:'un2-a',quantidade:0],
                             [produto: Produto.load(2L), unidade:'un2-b',quantidade:2]   ] )


        assert Usuario.count() ==2
        assert Cliente.count() == 2
        assertTrue Cliente.get(1).isDentroDaAreaDeEntregaRapida()
        assertFalse Cliente.get(2).isDentroDaAreaDeEntregaRapida()
        assert Produto.count() == 2
        assert Estoque.count() == 3
    }

    def cleanup() {
    }

    void "testar fechar venda com carrinho vazio"() {
        given:
            String menssagemTeste = '<< Carrinho Vazio >>'
            messageSource.addMessage 'shoppingCart.empty', request.locale, menssagemTeste
        when:
            controller.fecharVenda()

        then:
            flash.message == menssagemTeste
            response.redirectedUrl == '/shoppingCart/index'
    }


    void "testar fechar venda sem informar data de entrega"() {
        given:
            String menssagemTeste = '<< DATA NAO INFORMADA >>'
            messageSource.addMessage 'shoppingCart.dataEntrega.invalida', request.locale, menssagemTeste
            controller.vendaService = Mock(VendaService)
        when:
            request.method = 'POST'
            params.id = 1
            params.unidade = 'un1'
            params.quantidade = 3

            controller.add()

        then:
            assertEquals 3, session.shoppingCart.getQuantidade(Produto.load(1L),'un1')
            response.reset()

        when:
            controller.fecharVenda()

        then:
            flash.messageDataEntrega == menssagemTeste
            view == '/shoppingCart/checkout'
            model.containsKey('venda')
            model.containsKey('diasDeEntrega')

    }


    void "testar fechar venda informar data de entrega em formato invalido"() {
        given:
            String menssagemTeste = '<< DATA NO FORMATO INVALIDO >>'
            messageSource.addMessage 'shoppingCart.dataEntrega.invalida', request.locale, menssagemTeste
            controller.vendaService = Mock(VendaService)
        when:
            request.method = 'POST'
            params.id = 1
            params.unidade = 'un1'
            params.quantidade = 3

            controller.add()

        then:
            assertEquals 3, session.shoppingCart.getQuantidade(Produto.load(1L),'un1')
            response.reset()

        when:
            params.dataEntrega = 'formato-incorreto'
            controller.fecharVenda()

        then:
            flash.messageDataEntrega == menssagemTeste
            view == '/shoppingCart/checkout'
            model.containsKey('venda')
            model.containsKey('diasDeEntrega')

    }


    void "testar pagamento a vista para cliente FORA da area de entrega rapida"() {
        given:
            String menssagemTeste = '<< FORMA DE PAGAMENTO NAO ACEITA >>'
            messageSource.addMessage 'shoppingCart.formaPagamento.invalida', request.locale, menssagemTeste

            def springSecurityServiceMock = mockFor(SpringSecurityService)
            springSecurityServiceMock.demandExplicit.getCurrentUser(1..10){-> return Usuario.load(2) }

            controller.springSecurityService = springSecurityServiceMock.createMock()
            controller.vendaService = Mock(VendaService)
        when:
            request.method = 'POST'
            params.id = 1
            params.unidade = 'un1'
            params.quantidade = 3

            controller.add()

        then:
            assertEquals 3, session.shoppingCart.getQuantidade(Produto.load(1L),'un1')
            response.reset()
            flash.clear()

        when:
            params.dataEntrega = System.currentTimeMillis()
            params.formaPagamento = FormaPagamento.AVista.name()
            controller.fecharVenda()

        then: "Não deve aceitar "
            flash.message == menssagemTeste
            view == '/shoppingCart/checkout'
            model.containsKey('venda')
            model.containsKey('diasDeEntrega')

    }


    void "testar marcar data de entrega fora das permitidas pelo controller"() {
        given:
            String menssagemTeste = '<< DATA NÃO FOI ACEITA >>'
            messageSource.addMessage 'shoppingCart.dataEntrega.naoPermitida', request.locale, menssagemTeste
            controller.vendaService = Mock(VendaService)
        when:
            request.method = 'POST'
            params.id = 1
            params.unidade = 'un1'
            params.quantidade = 3

            controller.add()

        then:
            assertEquals 3, session.shoppingCart.getQuantidade(Produto.load(1L),'un1')
            response.reset()
            flash.clear()

        when:
            def ontem = (new Date() - 1 )
            params.dataEntrega = ontem.time
            params.formaPagamento = FormaPagamento.AVista.name()
            controller.fecharVenda()

        then: "Não deve aceitar a data de entrega informada "
            flash.messageDataEntrega == menssagemTeste
            view == '/shoppingCart/checkout'
            model.containsKey('venda')
            model.containsKey('diasDeEntrega')

    }


    void "testar fechar venda a vista"() {

        given:
            controller.springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
            def vendaServiceMock = Mock(VendaService)
            vendaServiceMock.isDataEntregaValida(_) >> true

            final arraylistOfDates = [new Date()]
            vendaServiceMock.getProximosDiasDeEntrega() >> arraylistOfDates
            controller.vendaService= vendaServiceMock

        expect:
            Estoque.findByProdutoAndUnidade(Produto.load(1L),'un1').quantidade == 10

        when:
            request.method = 'POST'
            params.id = 1
            params.unidade = 'un1'
            params.quantidade = 3

            controller.add()

        then:
            assertEquals 3, session.shoppingCart.getQuantidade(Produto.load(1L),'un1')
            response.reset()
            flash.clear()

        when:
            params.dataEntrega = arraylistOfDates[0].time
            params.formaPagamento = FormaPagamento.AVista.name()
            controller.fecharVenda()

        then:
            response.redirectedUrl.endsWith('/venda/show')
            1 * vendaServiceMock.salvarVenda(_)

    }

    void "testar fechar venda atraves do pagseguro"() {
        given:
            controller.springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')

            def vendaServiceMock = Mock(VendaService)
            vendaServiceMock.isDataEntregaValida(_) >> true

            final arraylistOfDates = [new Date()]
            vendaServiceMock.getProximosDiasDeEntrega() >> arraylistOfDates
            controller.vendaService= vendaServiceMock

            def paymentURLDeTeste = new URL("http://www.site.qualquer.de.teste")

            def pagSeguroServiceStub = Mock(PagSeguroService)
            pagSeguroServiceStub.getPaymentURL(_) >> paymentURLDeTeste
            controller.pagSeguroService = pagSeguroServiceStub


        expect:
            Estoque.findByProdutoAndUnidade(Produto.load(1L),'un1').quantidade == 10

        when:
            request.method = 'POST'
            params.id = 1
            params.unidade = 'un1'
            params.quantidade = 3

            controller.add()

        then:
            assertEquals 3, session.shoppingCart.getQuantidade(Produto.load(1L),'un1')
            response.reset()
            flash.clear()

        when:
            params.dataEntrega = arraylistOfDates[0].time
            params.formaPagamento = FormaPagamento.PagSeguro.name()
            controller.fecharVenda()

        then:
            response.redirectedUrl == paymentURLDeTeste.toString()
            1 * vendaServiceMock.salvarVenda(_)
    }
}
