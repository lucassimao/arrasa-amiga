package br.com.arrasaamiga

class ShoppingCart {
	static hasMany = [itens : ItemVenda]
	
	Date dateCreated
   	Date lastUpdated
	
	String sessionID
		
	Boolean checkedOut = false
	

	static constraints = {
		sessionID(blank:false)
	}

	static mapping = {
		itens lazy: false
	}

	static transients = ['valorTotalAPrazo','valorTotalAVista']

	public int getQuantidade(Produto produto, String unidade){
		
		def itemVenda = this.itens.find{ itemVenda-> 
			itemVenda.produto.id == produto.id && itemVenda.unidade.equals(unidade) 
		}

		return (itemVenda)?itemVenda.quantidade:0
	}

    private Double getValorTotalAPrazo(){
        double total = 0
        
        this.itens.each{ itemVenda ->
            total += itemVenda.precoAPrazoEmCentavos * itemVenda.quantidade
        }

        return total/100.0

    }


    private Double getValorTotalAVista(){
        double total = 0
        
        this.itens.each{ itemVenda ->
            total += itemVenda.precoAVistaEmCentavos * itemVenda.quantidade
        }

        return total/100.0

    }



}
