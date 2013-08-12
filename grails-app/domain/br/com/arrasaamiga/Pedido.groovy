package br.com.arrasaamiga

class Pedido {

	String descricao
	int quantidade
	Date dataPedido

	int valorEmCentavosDeDolar
	int valorEmCentavosDeReais

	String link
	String codigoRastreio

	StatusPedido status



    static constraints = {
    	descricao(blank:false,nullable:false)
    	quantidade(min:1)
    	dataPedido(nullable:false)
    	valorEmCentavosDeDolar(min:0)
    	valorEmCentavosDeReais(min:0)
    	link(blank:true,nullable:true)
    	codigoRastreio(blank:true,nullable:true)
    	status(nullable:false)
    }

}
