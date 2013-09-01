package br.com.arrasaamiga

import grails.plugins.springsecurity.Secured

class VendaController {


	@Secured(['IS_AUTHENTICATED_FULLY'])
	def show(Long id){

        def venda = Venda.get(id)
        
        if (venda){
        
        	[	numeroPedido: String.format("%05d", venda.id) , venda : venda,
        		enderecoEntrega: venda.enderecoEntrega,itens: venda.itensVenda,
        		cliente: venda.cliente, subTotal: venda.subTotalItensEmReais, 
        		detalhesPagamento: venda.detalhesPagamento, frete: venda.freteEmReais,desconto: venda.descontoEmReais,
        		valorTotal:venda.valorTotal,
        		dataEntrega: venda.dataEntrega,informacoesAdicionaisEntrega : venda.informacoesAdicionaisEntrega]
        
        }else{
        	println "Erro ao carregar venda ${id}"
        	render 'Venda não encontrada'
	    }

        
	}

	def cancelada(){
		
	}

}
