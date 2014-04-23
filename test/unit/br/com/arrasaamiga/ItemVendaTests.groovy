package br.com.arrasaamiga

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class ItemVendaTests {

    void setUp() {
        mockDomain(ItemVenda)
    }

    void tearDown() {
        // Tear down logic here
    }

    void testPrecoAVistaEmReais() {
    	def item = new ItemVenda()
    	item.precoAVistaEmReais = 34.89

    	assertEquals 3489, item.precoAVistaEmCentavos
    }

    void testPrecoAPrazoEmReais() {
    	def item = new ItemVenda()
    	item.precoAPrazoEmReais = 123.89

    	assertEquals 12389, item.precoAPrazoEmCentavos
    }

    void testSubTotalAVista(){
    	def item = new ItemVenda()
    	item.precoAVistaEmReais = 2.63
    	item.quantidade = 10

    	assertEquals 2.63 * 10, item.subTotalAVista, 0
    }

    void testSubTotalAVPrazo(){
    	def item = new ItemVenda()
    	item.precoAPrazoEmReais = 2.63
    	item.quantidade = 10

    	assertEquals 2.63 * 10, item.subTotalAPrazo, 0
    }


}
