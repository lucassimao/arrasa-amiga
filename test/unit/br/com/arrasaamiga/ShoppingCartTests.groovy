package br.com.arrasaamiga

import static org.junit.Assert.*

import grails.test.mixin.TestFor
import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(DomainClassUnitTestMixin)
@TestFor(ItemVenda)  
class ShoppingCartTests {

    void setUp() {
    	mockDomain(ShoppingCart)
    }

    void tearDown() {
        // Tear down logic here
    }

    void testGetValor() {
        def shoppingCart = new ShoppingCart()

        shoppingCart.addToItens(new ItemVenda(precoAVistaEmReais:9.99, precoAPrazoEmReais:12.97, quantidade:1 ))
        shoppingCart.addToItens(new ItemVenda(precoAVistaEmReais:0.99, precoAPrazoEmReais:2.17, quantidade:1 ))

        assertEquals 2, shoppingCart.itens.size()

        def expectedAPrazo = 12.97 + 2.17
        def expectedAVista = 9.99 + 0.99

        assertEquals expectedAPrazo,  shoppingCart.valorTotalAPrazo,0
        assertEquals expectedAVista,  shoppingCart.valorTotalAVista,0

    }

    void testGetQuantidade() {
    	def p1 = new Produto(id:1)
    	def p2 = new Produto(id:2)
        def shoppingCart = new ShoppingCart()

        def e1 = new ItemVenda(unidade:'un1', quantidade:1, produto: p1 )

        shoppingCart.addToItens(e1)
        shoppingCart.addToItens(new ItemVenda(unidade:'un2', quantidade:1, produto: p2 ))

        assertEquals 2, shoppingCart.itens.size()

        assertEquals 1,  shoppingCart.getQuantidade(p1, 'un1')
        assertEquals 0,  shoppingCart.getQuantidade(p1, 'unid-inexistente')
        assertEquals 1,  shoppingCart.getQuantidade(p2, 'un2')
        assertEquals 0,  shoppingCart.getQuantidade(p2, 'unid-inexistente')
        
        

        shoppingCart.addToItens(new ItemVenda(unidade:'un1', quantidade:2, produto: p1 ))
        shoppingCart.addToItens(new ItemVenda(unidade:'un2', quantidade:3, produto: p2 ))

        assertEquals 4, shoppingCart.itens.size()



        assertEquals 3,  shoppingCart.getQuantidade(p1, 'un1')
        assertEquals 4,  shoppingCart.getQuantidade(p2, 'un2')



        e1.quantidade += 10
        assertEquals 4, shoppingCart.itens.size()
        assertEquals 13,  shoppingCart.getQuantidade(p1, 'un1')

    }

}
