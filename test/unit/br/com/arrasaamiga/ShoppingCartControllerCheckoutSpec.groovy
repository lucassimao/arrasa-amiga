package br.com.arrasaamiga

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import org.codehaus.groovy.grails.commons.InstanceFactoryBean
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(ShoppingCartController)
@Mock([ItemVenda,ShoppingCart])
class ShoppingCartControllerCheckoutSpec extends Specification {

    def doWithSpring = {
        def springSecurityServiceMock = mockFor(SpringSecurityService)
        springSecurityServiceMock.demandExplicit.getCurrentUser(1..10){-> return Usuario.load(1) }

        springSecurityService(InstanceFactoryBean, springSecurityServiceMock.createMock(), SpringSecurityService)
    }

    def setup() {
        Usuario.metaClass.encodePassword = { null }
        Cliente.metaClass.reautenticar = { null }

        mockDomain(Usuario,[[id:1,username:'xpto@gmail.com',password:'123']])
        mockDomain(Cliente,[[id:1, usuario: Usuario.load(1),nome: 'Cliente XPTO']])

        mockDomain(Produto, [
                [nome: "Produto1", id: 1L, descricao: 'Produto 1', tipoUnitario: 'un',unidades:['un1']],
                [nome: "Produto2", id: 2L, descricao: 'Produto 2', tipoUnitario: 'un',unidades:['un2-a','un2-b']]])

        mockDomain(Estoque,[ [produto: Produto.load(1L), unidade:'un1',quantidade:10],
                             [produto: Produto.load(2L), unidade:'un2-a',quantidade:0],
                             [produto: Produto.load(2L), unidade:'un2-b',quantidade:2]   ] )


        assert Usuario.count() == 1
        assert Cliente.count() == 1
        assert Produto.count() == 2
        assert Estoque.count() == 3
    }

    def cleanup() {
    }

    void "testar checkout com carrinho vazio"() {
        given:
            String menssagemTeste = '<< Carrinho Vazio >>'
            messageSource.addMessage 'shoppingCart.empty', request.locale, menssagemTeste
            controller.springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
        when:
            controller.checkout()

        then:
            flash.message == menssagemTeste
            response.redirectedUrl == '/shoppingCart/index'
    }

    void "testar checkout com itens no carrinho"() {
        given:
            controller.springSecurityService = grailsApplication.mainContext.getBean('springSecurityService')
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
            request.method = 'POST'
            params.id = 2
            params.unidade = 'un2-b'
            params.quantidade = 2

            controller.add()

        then:
            assertEquals 2, session.shoppingCart.getQuantidade(Produto.load(2L),'un2-b')
            response.reset()

        when:
            def model = controller.checkout()
        then:
            assertNotNull(model.venda.carrinho)
            assertEquals 3, session.shoppingCart.getQuantidade(Produto.load(1L),'un1')
            assertEquals 2, session.shoppingCart.getQuantidade(Produto.load(2L),'un2-b')
            assertNotNull(model.diasDeEntrega)



    }
}
