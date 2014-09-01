package br.com.arrasaamiga

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(ShoppingCartController)
@Mock([ShoppingCart,ItemVenda])
class ShoppingCartControllerSpec extends Specification {

    def setup() {
        mockDomain(Produto, [
                [nome: "Produto1", id: 1, descricao: 'Produto 1', tipoUnitario: 'un',unidades:['un1']],
                [nome: "Produto2", id: 2, descricao: 'Produto 2', tipoUnitario: 'un',unidades:['un2']]])

        mockDomain(Estoque,[ [produto: Produto.load(1), unidade:'un1',quantidade:10],
                             [produto: Produto.load(2), unidade:'un2',quantidade:5]  ] )

    }

    def cleanup() {
    }

    void "test adicionar Produto"() {
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
}
