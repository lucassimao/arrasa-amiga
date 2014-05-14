package br.com.arrasaamiga

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(DomainClassUnitTestMixin)
@Mock([Produto, Estoque])
class ProdutoTests {

    void setUp() {
        mockDomain(Produto)
        mockDomain(Estoque)
    }

    void tearDown() {
    }


    void testIsProdutoMultiUnidade() {
       def p = new Produto()

       assertFalse p.isMultiUnidade() // Produto sem nenhuma unidade informada

       p.unidades = ['un']

       assertFalse p.isMultiUnidade()

       p.unidades = ['rosa','verde']
       assertTrue p.isMultiUnidade()


    }

    void testGetPrecoAVista(){
      def p = new Produto()
      p.precoAVistaEmReais = 29.99

      assertEquals 2999, p.precoAVistaEmCentavos

      p.precoAVistaEmCentavos = 9071
      assertEquals 90.71, p.precoAVistaEmReais, 0

      p.precoAVistaEmCentavos = 9079
      assertEquals 90.79, p.precoAVistaEmReais, 0

    }

    void testGetPrecoAPrazo(){
      def p = new Produto()
      p.precoAPrazoEmReais = 29.99

      assertEquals 2999, p.precoAPrazoEmCentavos

      p.precoAPrazoEmCentavos = 9071
      assertEquals 90.71, p.precoAPrazoEmReais, 0

      p.precoAPrazoEmCentavos = 79
      assertEquals 0.79, p.precoAPrazoEmReais, 0

    }


    void testGetDescontoAVista(){
      def p = new Produto()
      p.precoAVistaEmReais = 29.99
      p.precoAPrazoEmReais = 29.99

      assertEquals 0, p.descontoAVistaEmReais, 0


      p.precoAPrazoEmReais = 32.89
      assertEquals p.precoAPrazoEmCentavos - p.precoAVistaEmCentavos, p.descontoAVistaEmReais*100, 0

    }

    void testGetQuantidade(){
        def p1 = new Produto(descricao:'Produto 1',nome:'P1',tipoUnitario:'un')
        p1.unidades = ['preto']

        assertNotNull p1.save(failOnError:true)

        assertFalse p1.isMultiUnidade()



        def e1 = new Estoque(produto: p1, unidade: 'preto', quantidade:10)
        assertNotNull e1.save(failOnError:true)

        assertEquals 10, p1.getQuantidadeEmEstoque('preto')

        shouldFail(IllegalArgumentException){

            p1.getQuantidadeEmEstoque('unidade-inexistente')

        }

        shouldFail(IllegalStateException){

            p1.addToUnidades('unidade-xpto')
            assertTrue p1.unidades.contains('unidade-xpto')
            assertNotNull p1.save(failOnError:true)
            assertTrue p1.isMultiUnidade()

            p1.getQuantidadeEmEstoque('unidade-xpto')
            
        }

        def e2 = new Estoque(produto: p1, unidade: 'unidade-xpto', quantidade:100)
        assertNotNull e2.save(failOnError:true)

        assertEquals 100, p1.getQuantidadeEmEstoque('unidade-xpto')



    }






}
