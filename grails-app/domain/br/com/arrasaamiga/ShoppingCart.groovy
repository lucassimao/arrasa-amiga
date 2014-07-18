package br.com.arrasaamiga

class ShoppingCart {
	
	static hasMany = [itens : ItemVenda]
	Date dateCreated
   	Date lastUpdated
	
	static mapping = {
		itens lazy: false, cascade: 'all'
	}

	static transients = ['valorTotalAPrazo','valorTotalAVista']

	public int getQuantidade(Produto produto, String unidade){
		
		def set = this.itens.findAll{ itemVenda-> 

			itemVenda.produto.id == produto.id && itemVenda.unidade.equals(unidade) 
		
		}

		return set.sum(0){ it.quantidade }

	}

    public Double getValorTotalAPrazo(){
        int total = 0

        this.itens?.each{ itemVenda ->

            total +=  itemVenda.precoAPrazoEmCentavos * itemVenda.quantidade
        }

        return total/100.0

    }


    public Double getValorTotalAVista(){
        int total = 0
        
        this.itens?.each{ itemVenda ->
            total += itemVenda.precoAVistaEmCentavos * itemVenda.quantidade
        }

        return total/100.0

    }



}
