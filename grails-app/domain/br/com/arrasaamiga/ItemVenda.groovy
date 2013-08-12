package br.com.arrasaamiga

class ItemVenda {

	Produto produto
	String unidade
	int quantidade

	static belongsTo = Venda
	static transients = ['subTotal']

    static constraints = {
    	produto(nullable:false)
    	quantidade(min:1)
    	unidade(blank:false,nullable:false)
    }

    public Double getSubTotal(){
    	return produto.precoEmReais * quantidade
    }
}
