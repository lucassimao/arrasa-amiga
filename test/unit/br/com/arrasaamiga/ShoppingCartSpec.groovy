package br.com.arrasaamiga

import grails.test.mixin.Mock
import spock.lang.Specification
import static org.junit.Assert.*
import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(ItemVenda)
@Mock([ShoppingCart,Produto])
class ShoppingCartSpec extends Specification {

    void setup() {
    }

    void "teste valorTotalAPrazo e valorTotalAVista"() {
        given:
            def shoppingCart = new ShoppingCart()

            def expectedAPrazo = 12.97 + 2.17
            def expectedAVista = 9.99 + 0.99

            def p1 = new Produto(precoAVistaEmReais:9.99, precoAPrazoEmReais:12.97)
            p1.id = 1
            def p2 = new Produto(precoAVistaEmReais:0.99, precoAPrazoEmReais:2.17)
            p2.id = 2
        when:
            shoppingCart.add(p1,'un',1)
            shoppingCart.add(p2,'un',1)

        then:
            assertEquals 2, shoppingCart.itens.size()
            assertEquals expectedAPrazo,  shoppingCart.valorTotalAPrazo,0
            assertEquals expectedAVista,  shoppingCart.valorTotalAVista,0

    }

    void "teste GetQuantidade"() {
        given:
            def p1 = new Produto()
            p1.id = 1
            def p2 = new Produto()
            p2.id = 2

            def shoppingCart = new ShoppingCart()

        when:
            shoppingCart.add(p1, 'un1',1)
            shoppingCart.add(p2, 'un2',1)
        then:
            assertEquals 2, shoppingCart.itens.size()
            assertEquals 1,  shoppingCart.getQuantidade(p1, 'un1')
            assertEquals 0,  shoppingCart.getQuantidade(p1, 'unid-inexistente')
            assertEquals 1,  shoppingCart.getQuantidade(p2, 'un2')
            assertEquals 0,  shoppingCart.getQuantidade(p2, 'unid-inexistente')
        

        when:
            shoppingCart.add(p1,'un1',2)
            shoppingCart.add(p2, 'un2', 3)
        then:
            assertEquals 2, shoppingCart.itens.size()
            assertEquals 3,  shoppingCart.getQuantidade(p1, 'un1')
            assertEquals 4,  shoppingCart.getQuantidade(p2, 'un2')

    }

    void "teste remover item do carrinho"(){
        given:
            def p1 = new Produto()
            p1.id = 1

            def p2 = new Produto()
            p2.id = 2

            def p3 = new Produto()
            p3.id = 3

            def shoppingCart = new ShoppingCart()

        when:
            shoppingCart.add(p1, 'un1',3)
            shoppingCart.add(p2, 'un2',2)
        then:
            assertEquals 2, shoppingCart.itens.size()
            assertEquals 3,  shoppingCart.getQuantidade(p1, 'un1')
            assertEquals 0,  shoppingCart.getQuantidade(p1, 'unid-inexistente')
            assertEquals 2,  shoppingCart.getQuantidade(p2, 'un2')
            assertEquals 0,  shoppingCart.getQuantidade(p2, 'unid-inexistente')


        when:
            shoppingCart.remove(p1,'un1',1)
            shoppingCart.remove(p2, 'un2', 2)
        then:
            assertEquals 1, shoppingCart.itens.size()
            assertEquals 2,  shoppingCart.getQuantidade(p1, 'un1')
            assertEquals 0,  shoppingCart.getQuantidade(p2, 'un2')


        when: "Tentando remover item inexistente"
            shoppingCart.remove(p3,'un3',10)

        then: "Assegura que nada foi alterado"
            assertEquals 1, shoppingCart.itens.size()
            assertEquals 2,  shoppingCart.getQuantidade(p1, 'un1')
            assertEquals 0,  shoppingCart.getQuantidade(p2, 'un2')
    }


}
