package br.com.arrasaamiga

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(CartTagLib)
@Mock([ItemVenda,ShoppingCart])
class CartTagLibSpec extends Specification {


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

    void "test adicionar produto e conferir taglib"() {
        given:
            def controller = mockController(ShoppingCartController)
        expect:
            applyTemplate('<cart:qtdeTotalItens/>') == '0'


        when:
            request.method = 'POST'
            params.quantidade = 2
            params.unidade = 'un1'
            params.id = 1
            controller.add()

        then:
            session.shoppingCart != null
            applyTemplate('<cart:qtdeTotalItens/>') == '2'

    }

    void "test remover produto e conferir taglib"() {
        given:
            def controller = mockController(ShoppingCartController)
        expect:
            applyTemplate('<cart:qtdeTotalItens/>') == '0'


        when:
            request.method = 'POST'
            params.quantidade = 5
            params.unidade = 'un1'
            params.id = 1
            controller.add()

        then:
            applyTemplate('<cart:qtdeTotalItens/>') == '5'


        when:
            response.reset()
            request.method = 'POST'
            params.quantidade = 2
            params.unidade = 'un1'
            params.id = 1
            controller.removerProduto()

        then:
            applyTemplate('<cart:qtdeTotalItens/>') == '3'

    }
}
