package br.com.arrasaamiga

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import grails.test.mixin.web.UrlMappingsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(ShoppingCartController)
@Mock([ShoppingCart,ItemVenda])
@TestMixin(UrlMappingsUnitTestMixin)
class ShoppingCartControllerSpec extends Specification {

    def setup() {
        mockDomain(Produto, [
                [nome: "Produto1", id: 1, descricao: 'Produto 1', tipoUnitario: 'un',unidades:['un1']],
                [nome: "Produto2", id: 2, descricao: 'Produto 2', tipoUnitario: 'un',unidades:['un2-a','un2-b']]])

        mockDomain(Estoque,[ [produto: Produto.load(1), unidade:'un1',quantidade:10],
                             [produto: Produto.load(2), unidade:'un2-a',quantidade:0],
                             [produto: Produto.load(2), unidade:'un2-b',quantidade:2]   ] )

    }

    def cleanup() {
    }

    void "teste adicionar produto valido"() {
        when:
            request.method = 'POST'
            params.id = 1
            params.unidade = 'un1'
            params.quantidade = 5
            controller.add()

        then:
            assertNotNull session.shoppingCart
            assertEquals  '/shoppingCart/index', response.redirectedUrl
            assertEquals 5, session.shoppingCart.getQuantidade(Produto.get(1),'un1')
    }

    void "teste adicionar produto inexistente"(){
        when:
            request.method = 'POST'
            params.id = 1000
            params.unidade = 'un1'
            params.quantidade = 5
            controller.add()

        then:
            assertNotNull session.shoppingCart
            assertTrue response.redirectedUrl.endsWith("shoppingCart/index")
            assertNull session.shoppingCart.itens
            assert flash.message.contains('desconhecido')
    }

    void "teste adicionar produto com unidade nao cadastrada"(){

        when:
            request.method = 'POST'
        params.id = 1
        params.unidade = 'unidade-invalida'
        params.quantidade = 5
        controller.add()

        then:
            assertNotNull session.shoppingCart
            assertTrue response.redirectedUrl.endsWith("shoppingCart/index")
            assertTrue flash.message.contains('não contem a unidade')
            assertNull session.shoppingCart.itens
    }


    void "teste adicionar zero unidades de algum produto"(){

        when:
            request.method = 'POST'
            params.id = 1
            params.unidade = 'un1'
            params.quantidade = 0
            controller.add()

        then:
            assertNotNull session.shoppingCart
            assertTrue response.redirectedUrl.endsWith("shoppingCart/index")
            assertTrue flash.message.contains("Quantidade inválida")
            assertNull session.shoppingCart.itens
    }


    void "teste adicionar produto que esta cadastrado mas NAO tem unidades disponiveis no estoque"(){

        when:
            request.method = 'POST'
            params.id = 2
            params.unidade = 'un2-a'
            params.quantidade = 1
            controller.add()

        then:
            assertNotNull session.shoppingCart
            assertTrue response.redirectedUrl.endsWith("shoppingCart/index")
            assertTrue flash.message.contains("Este produto esta em falta temporariamente")
            assertNull session.shoppingCart.itens
    }

    void "teste adicionar quantidade superior a existente em estoque de um produto"(){

        when:
            request.method = 'POST'
            params.id = 2
            params.unidade = 'un2-b'
            params.quantidade = 2
            controller.add()
        then:
            assertNotNull session.shoppingCart
            assertNotNull session.shoppingCart.itens
            assertEquals(2, session.shoppingCart.getQuantidade(Produto.load(2L),'un2-b'))

        when: "tentando adicionar mais 1 unidade do mesmo produto"
            response.reset()
            request.method = 'POST'
            params.id = 2
            params.unidade = 'un2-b'
            params.quantidade = 1
            controller.add()
        then:
            assertEquals(2, session.shoppingCart.getQuantidade(Produto.load(2L),'un2-b'))

    }
}
