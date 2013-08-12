package br.com.arrasaamiga

class Venda {


	List itensVenda
	Date dateCreated
	Cliente cliente
	Endereco enderecoEntrega

	int valorFreteEmCentavos
	int valorTotalEmCentavos

	FormaPagamento formaPagamento

	static hasMany = [itensVenda:ItemVenda]
	static transients = ['valorTotal']

    static constraints = {
    	dateCreated(nullable:false)
    	dateCreated(nullable:false)
    	valorFreteEmCentavos(min:0)
    	valorTotalEmCentavos(min:0)
    	itensVenda(nullable:false)
    	formaPagamento(nullable:false)

    }

    public Double getValorTotal(){
    	double valor = 0d

    	itensVenda.each{item->
    		valor += item.subTotal
    	}

    	return valor
    }


}
