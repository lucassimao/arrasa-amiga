package br.com.arrasaamiga

class EntradaEstoque {

	Pedido pedido
    Date dataEntrada

    static belongsTo = Estoque


    static constraints = {
    	pedido(blank:false,nullable:false)
    	dataEntrada(nullable:false,blank:false,)
    }


}
