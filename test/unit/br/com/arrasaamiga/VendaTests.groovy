package br.com.arrasaamiga

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
class VendaTests {

    void setUp() {
    }

    void tearDown() {
    }

    /** Testando valor a vista e a prazo */
    void testValorItens() {
    	def carrinho = new ShoppingCart()
    	carrinho.addToItens(new ItemVenda(quantidade: 2, precoAVistaEmReais: 28.71, precoAPrazoEmReais: 9.01))
    	carrinho.addToItens(new ItemVenda(quantidade: 3, precoAVistaEmReais: 2.99, precoAPrazoEmReais: 19.09))

    	def v = new Venda(carrinho: carrinho)


    	def expectedValorAPrazo = 75.29
    	def expectedValorAVista = 66.39

    	assertEquals expectedValorAPrazo, v.valorItensAPrazo, 0
    	assertEquals expectedValorAVista, v.valorItensAVista, 0

    }

    void testFreteEmReaisParaClienteNaAreaDeEntrega(){
        def freteExperado = 2d

        def clienteMock = mockFor(Cliente,true)
        clienteMock.demand.isDentroDaAreaDeEntregaRapida(1){-> return true }

        def v = new Venda()
        v.cliente = clienteMock.createMock()

    	assertEquals freteExperado, v.freteEmReais, 0

    	clienteMock.verify()


    }

    void testFreteEmReaisParaClienteForaDaAreaDeEntrega(){
    	def clienteMock = mockFor(Cliente,true)
    	clienteMock.demand.isDentroDaAreaDeEntregaRapida(2){-> return false }

        def expectedFreteEmReais = 19.99

        def correiosServiceMock = mockFor(CorreiosService)
    	correiosServiceMock.demand.calcularFrete(1){String cep, ServicoCorreio sc-> return expectedFreteEmReais }


    	def v = new Venda()
    	v.correiosService = correiosServiceMock.createMock()
    	v.cliente = clienteMock.createMock()


    	assertEquals 0, v.freteEmCentavos // garante que vai recuperar o valor do frete a partir do web service dos correiop
    	assertEquals expectedFreteEmReais, v.freteEmReais, 0



    	v.freteEmCentavos = 1999 // agora nao utiliza mais o web service
    	assertEquals expectedFreteEmReais, v.freteEmReais, 0 



    	clienteMock.verify()
    	correiosServiceMock.verify()

    }


    void testDescontoEmReais(){

		
		def carrinhoMock = mockFor(ShoppingCart,true)
		carrinhoMock.demand.getValorTotalAPrazo(1){-> return 20.99}
		carrinhoMock.demand.getValorTotalAVista(1){-> return 17.89}


		def vendaAVista = new Venda(formaPagamento: FormaPagamento.AVista)
		vendaAVista.carrinho = carrinhoMock.createMock()

		def expectedDesconto = 3.10

		assertEquals expectedDesconto, vendaAVista.descontoEmReais, 0


		carrinhoMock.verify() // garantindo que os metodos foram chamados

		def vendaPagSeguro = new Venda(formaPagamento: FormaPagamento.PagSeguro, descontoPagSeguroEmCentavos: 0)
		assertEquals 0, vendaPagSeguro.descontoEmReais, 0

		vendaPagSeguro.descontoPagSeguroEmCentavos = 199
		assertEquals 1.99, vendaPagSeguro.descontoEmReais, 0



    }

    void testGetTaxaDeEntrega(){
        def freteForaDaAreaDeEntrega = 29.99d // ** HARD CODED **
        def freteDentroDaAreaDeEntrega = 2d

        def correiosServiceMock = mockFor(CorreiosService,true)
        correiosServiceMock.demand.calcularFrete(1){cep,servico-> return freteForaDaAreaDeEntrega}

     	def clienteForaDaAreaDeEntregaMock = mockFor(Cliente,true)
    	clienteForaDaAreaDeEntregaMock.demand.isDentroDaAreaDeEntregaRapida(1){-> return false }

    	def vendaForaDaAreaDeEntrega = new Venda()
    	vendaForaDaAreaDeEntrega.cliente = clienteForaDaAreaDeEntregaMock.createMock()
        vendaForaDaAreaDeEntrega.correiosService = correiosServiceMock.createMock()

    	assertEquals freteForaDaAreaDeEntrega, vendaForaDaAreaDeEntrega.freteEmReais, 0


     	def clienteDentroDaAreaDeEntregaMock = mockFor(Cliente,true)
    	clienteDentroDaAreaDeEntregaMock.demand.isDentroDaAreaDeEntregaRapida(1){-> return true }

    	def vendaDentroDaAreaDeEntrega = new Venda()
    	vendaDentroDaAreaDeEntrega.cliente = clienteDentroDaAreaDeEntregaMock.createMock()


    	assertEquals freteDentroDaAreaDeEntrega, vendaDentroDaAreaDeEntrega.freteEmReais, 0
   	
    }


    void testGetDetalhes(){
    	String expectedDetalhesMsg = '** MSG XPTO RETORNADA PELO PAGSEGURO **'

    	def pagSeguroServiceMock = mockFor(PagSeguroService)
    	pagSeguroServiceMock.demand.getDetalhesPagamento(0..1){String transacao-> return expectedDetalhesMsg }

    	def vendaAVista = new Venda(formaPagamento: FormaPagamento.AVista)
    	vendaAVista.pagSeguroService = pagSeguroServiceMock.createMock()


    	assertFalse vendaAVista.detalhesPagamento?.equals(expectedDetalhesMsg)



    	def vendaPagSeguro = new Venda(formaPagamento: FormaPagamento.PagSeguro, transacaoPagSeguro: '123')
    	vendaPagSeguro.pagSeguroService = pagSeguroServiceMock.createMock()


    	assertEquals vendaPagSeguro.detalhesPagamento, expectedDetalhesMsg


    }


    void testValorTotalAVistaDentroDaAreaDeEntrega(){
      	def clienteDentroDaAreaDeEntregaMock = mockFor(Cliente,true)
    	clienteDentroDaAreaDeEntregaMock.demand.isDentroDaAreaDeEntregaRapida(1){-> return true }

    	def carrinhoMock = mockFor(ShoppingCart,true)
    	carrinhoMock.demand.getValorTotalAVista(1){ -> return 12.31  }
    	carrinhoMock.demand.getValorTotalAPrazo(2){ -> return 14.31  }


    	def venda = new Venda(formaPagamento: FormaPagamento.AVista)
    	venda.cliente = clienteDentroDaAreaDeEntregaMock.createMock()
    	venda.carrinho = carrinhoMock.createMock()

    	def taxaEntrega = 2
    	def expectedValorTotal = taxaEntrega + 12.31

    	assertEquals  expectedValorTotal, venda.valorTotal, 0


    	clienteDentroDaAreaDeEntregaMock.verify()
    	carrinhoMock.verify()


    }

    void testValorTotalAPrazoDentroDaAreaDeEntrega(){
      	def clienteDentroDaAreaDeEntregaMock = mockFor(Cliente,true)
    	clienteDentroDaAreaDeEntregaMock.demand.isDentroDaAreaDeEntregaRapida(1){-> return true }

    	def carrinhoMock = mockFor(ShoppingCart,true)
    	carrinhoMock.demand.getValorTotalAVista(0){ -> return 12.31  }
    	carrinhoMock.demand.getValorTotalAPrazo(1){ -> return 14.31  }


    	def venda = new Venda(formaPagamento: FormaPagamento.PagSeguro)
    	venda.cliente = clienteDentroDaAreaDeEntregaMock.createMock()
    	venda.carrinho = carrinhoMock.createMock()

    	def expectedValorTotal = 16.31 // taxa de entrega + 14.31

    	assertEquals  expectedValorTotal, venda.valorTotal, 0


    	clienteDentroDaAreaDeEntregaMock.verify()
    	carrinhoMock.verify()
    }

    void testValorTotalAVistaForaDaAreaDeEntrega(){
 		def clienteDentroDaAreaDeEntregaMock = mockFor(Cliente,true)
    	clienteDentroDaAreaDeEntregaMock.demand.isDentroDaAreaDeEntregaRapida(1){-> return false }

    	def carrinhoMock = mockFor(ShoppingCart,true)
    	carrinhoMock.demand.getValorTotalAVista(1){ -> return 12.31  }
    	carrinhoMock.demand.getValorTotalAPrazo(2){ -> return 14.31  }

    	def correiosServiceMock = mockFor(CorreiosService)
    	correiosServiceMock.demand.calcularFrete(1){cep, servico -> return 89.31 }


    	def venda = new Venda(formaPagamento: FormaPagamento.AVista)
    	venda.cliente = clienteDentroDaAreaDeEntregaMock.createMock()
    	venda.carrinho = carrinhoMock.createMock()
    	venda.correiosService = correiosServiceMock.createMock()



    	def expectedValorTotal = 101.62 // valor a vista + valor do frete

    	assertEquals  expectedValorTotal, venda.valorTotal, 0


    	clienteDentroDaAreaDeEntregaMock.verify()
    	carrinhoMock.verify()
    	correiosServiceMock.verify()
    }

    void testValorTotalAPrazoForaDaAreaDeEntrega(){
 		def clienteDentroDaAreaDeEntregaMock = mockFor(Cliente,true)
    	clienteDentroDaAreaDeEntregaMock.demand.isDentroDaAreaDeEntregaRapida(1){-> return false }

    	def carrinhoMock = mockFor(ShoppingCart,true)
    	carrinhoMock.demand.getValorTotalAVista(0){ -> return 3.31  }
    	carrinhoMock.demand.getValorTotalAPrazo(1){ -> return 19.31  }

    	def correiosServiceMock = mockFor(CorreiosService)
    	correiosServiceMock.demand.calcularFrete(1){cep, servico -> return 99.99 }


    	def venda = new Venda(formaPagamento: FormaPagamento.PagSeguro)
    	venda.cliente = clienteDentroDaAreaDeEntregaMock.createMock()
    	venda.carrinho = carrinhoMock.createMock()
    	venda.correiosService = correiosServiceMock.createMock()



    	def expectedValorTotal = 119.3 // valor a prazo + valor do frete

    
    	assertEquals  expectedValorTotal, venda.valorTotal, 0


    	clienteDentroDaAreaDeEntregaMock.verify()
    	carrinhoMock.verify()
    	correiosServiceMock.verify()
    }

}
