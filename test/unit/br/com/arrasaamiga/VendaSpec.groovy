package br.com.arrasaamiga

import br.com.uol.pagseguro.domain.Transaction
import spock.lang.Specification

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(Venda)
@Mock([ShoppingCart,ItemVenda])
class VendaSpec extends  Specification {

    void "Testando valor a vista e a prazo"() {
		given:
			def carrinho = new ShoppingCart()
			carrinho.addToItens(new ItemVenda(quantidade: 2, precoAVistaEmReais: 28.71, precoAPrazoEmReais: 9.01))
			carrinho.addToItens(new ItemVenda(quantidade: 3, precoAVistaEmReais: 2.99, precoAPrazoEmReais: 19.09))

	    	def v = new Venda(carrinho: carrinho)
			def expectedValorAPrazo = 75.29
			def expectedValorAVista = 66.39
		expect:
			expectedValorAPrazo == v.valorItensAPrazo
			expectedValorAVista == v.valorItensAVista

    }

    void "test frete em reais para cliente na area de entrega"(){
		setup:
			def freteExperado = 2d

			Cliente cliente = Mock()
			cliente.isDentroDaAreaDeEntregaRapida() >> true

            def venda = new Venda()
            venda.cliente = cliente

        expect:
            freteExperado == venda.getFreteEmReais()

    }


    void "teste frete em reais para cliente fora da area de entrega"(){
        given:
            def expectedFreteEmReais = 19.99

            def cliente = Mock(Cliente)
            cliente.isDentroDaAreaDeEntregaRapida() >> false

            def correiosService = Mock(CorreiosService)
    	    correiosService.calcularFrete(_,_) >> expectedFreteEmReais

            def v = new Venda()
            v.correiosService = correiosService
            v.cliente = cliente
        expect:
            assertEquals 0, v.freteEmCentavos // garante que vai recuperar o valor do frete a partir do web service dos correiop
    	    assertEquals expectedFreteEmReais, v.getFreteEmReais(),0

    }


    void "test desconto em reais"(){
        given:
            def carrinho = Mock(ShoppingCart)
            carrinho.getValorTotalAPrazo() >> 20.99
            carrinho.getValorTotalAVista() >> 17.89

            def vendaAVista = new Venda(formaPagamento: FormaPagamento.AVista)
            vendaAVista.carrinho = carrinho

            def expectedDesconto = 3.10

            def vendaPagSeguro = new Venda(formaPagamento: FormaPagamento.PagSeguro, descontoPagSeguroEmCentavos: 0)

        expect:
            expectedDesconto == vendaAVista.descontoEmReais
            0 == vendaPagSeguro.descontoEmReais
        when:
            vendaPagSeguro.descontoPagSeguroEmCentavos = 199
        then:
            1.99 ==  vendaPagSeguro.descontoEmReais

    }

    void "test Get Taxa De Entrega"(){
        setup:

            def freteForaDaAreaDeEntrega = 29.99d // ** HARD CODED **
            def freteDentroDaAreaDeEntrega = 2d

            def correiosService = Mock(CorreiosService)
            correiosService.calcularFrete(_,_) >> freteForaDaAreaDeEntrega

            def clienteForaDaAreaDeEntrega = Mock(Cliente)
            clienteForaDaAreaDeEntrega.isDentroDaAreaDeEntregaRapida() >> false

            def vendaForaDaAreaDeEntrega = new Venda()
            vendaForaDaAreaDeEntrega.cliente = clienteForaDaAreaDeEntrega
            vendaForaDaAreaDeEntrega.correiosService = correiosService

            def clienteDentroDaAreaDeEntrega = Mock(Cliente)
            clienteDentroDaAreaDeEntrega.isDentroDaAreaDeEntregaRapida() >> true

            def vendaDentroDaAreaDeEntrega = new Venda()
            vendaDentroDaAreaDeEntrega.cliente = clienteDentroDaAreaDeEntrega

        expect:
            freteForaDaAreaDeEntrega == vendaForaDaAreaDeEntrega.freteEmReais
            freteDentroDaAreaDeEntrega == vendaDentroDaAreaDeEntrega.freteEmReais

    }


    void "test valor total"(){
        setup:
            def taxaEntrega = 2d

            def cliente = Mock(Cliente)
            cliente.isDentroDaAreaDeEntregaRapida() >> isDentroDaAreaDeEntregaRapida

            def carrinho = Mock(ShoppingCart)
            carrinho.getValorTotalAVista() >>  12.31
            carrinho.getValorTotalAPrazo() >>  14.31

            def correiosService = Mock(CorreiosService)
            correiosService.calcularFrete(_,_) >> 89.31

            def venda = new Venda()
            venda.formaPagamento = formaPagamento
            venda.cliente =  cliente
            venda.carrinho = carrinho
            venda.correiosService = correiosService

        expect:
            expectedValorTotal == venda.valorTotal
        where:
            isDentroDaAreaDeEntregaRapida | formaPagamento          | expectedValorTotal
            true                          | FormaPagamento.AVista   | 2 + 12.31 // 2 da taxa de entrega
            false                         | FormaPagamento.AVista   | 89.31 + 12.31 // 89.31 do frete
            true                          | FormaPagamento.PagSeguro| 2 + 14.31 // 2 da taxa de entrega
            false                         | FormaPagamento.PagSeguro| 89.31 + 14.31 // 89.31 do frete

    }

}
