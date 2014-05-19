package br.com.arrasaamiga

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*
import grails.test.mixin.domain.DomainClassUnitTestMixin


/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(DomainClassUnitTestMixin)
@Mock([ShoppingCart, ItemVenda,Produto])  
class ShoppingCartServiceTests {

    void setUp() {
    }

    void tearDown() {
        // Tear down logic here
    }

    void testAddToShoppingCart() {
    	def shoppingCart = new ShoppingCart()

    	def shoppingCartFactoryServiceMocker = mockFor(ShoppingCartFactoryService,true)
    	shoppingCartFactoryServiceMocker.demand.getShoppingCart(3){-> return shoppingCart}


    	def shoppingCartService = new ShoppingCartService()
    	shoppingCartService.shoppingCartFactoryService = shoppingCartFactoryServiceMocker.createMock()


    	def p1 = new Produto()
    	p1.id=10
    	def unidade1 = 'UN-P1'

    	def p2 = new Produto()
    	p2.id = 20
    	def unidade2 = 'UN-P2'

    	shoppingCartService.addToShoppingCart(p1, unidade1, 2)
    	shoppingCartService.addToShoppingCart(p2, unidade2, 7)

    	assertEquals 2, shoppingCart.getQuantidade(p1, unidade1)
    	assertEquals 0, shoppingCart.getQuantidade(p1, unidade2)


    	shoppingCartService.addToShoppingCart(p1, unidade1, 3)

    	assertEquals 5, shoppingCart.getQuantidade(p1, unidade1)


    	assertEquals 7, shoppingCart.getQuantidade(p2, unidade2)
    	assertEquals 0, shoppingCart.getQuantidade(p2, unidade1)


    }



    void testRemoveFromShoppingCart() {
    	def shoppingCart = new ShoppingCart()

    	def shoppingCartFactoryServiceMocker = mockFor(ShoppingCartFactoryService,true)
    	shoppingCartFactoryServiceMocker.demand.getShoppingCart(3){-> return shoppingCart}


    	def shoppingCartService = new ShoppingCartService()
    	shoppingCartService.shoppingCartFactoryService = shoppingCartFactoryServiceMocker.createMock()


    	def p1 = new Produto()
    	p1.id=10
    	def unidade1 = 'UN-P1'

    	def p2 = new Produto()
    	p2.id = 20
    	def unidade2 = 'UN-P2'

    	shoppingCart.itens = [ new ItemVenda(produto:p1, unidade:unidade1,quantidade:3), 
    						   new ItemVenda(produto:p2, unidade:unidade2,quantidade:5)  ]

    	shoppingCartService.removeFromShoppingCart(p1, unidade1, 2)
    	shoppingCartService.removeFromShoppingCart(p2, unidade2, 2)

    	assertEquals 1, shoppingCart.getQuantidade(p1, unidade1)
    	assertEquals 0, shoppingCart.getQuantidade(p1, unidade2)
    
    	assertEquals 3, shoppingCart.getQuantidade(p2, unidade2)
    	assertEquals 0, shoppingCart.getQuantidade(p2, unidade1)

    }


}
