package br.com.arrasaamiga

class ItemVenda {

	Produto produto
	String unidade
	int quantidade

    int precoAVistaEmCentavos
    int precoAPrazoEmCentavos   


	static transients = ['subTotalAVista','subTotalAPrazo','precoAVistaEmReais','precoAPrazoEmReais']

    static constraints = {
    	produto(nullable:false)
    	quantidade(min:1)
    	unidade(blank:false,nullable:false)
        precoAPrazoEmCentavos(min:0)
        precoAVistaEmCentavos(min:0)
    }

    public Double getPrecoAVistaEmReais(){
        return this.precoAVistaEmCentavos/100.0
    }

    public void setPrecoAVistaEmReais(Double precoEmReais){
        this.precoAVistaEmCentavos = 100*precoEmReais
    } 

    public Double getPrecoAPrazoEmReais(){
        return this.precoAPrazoEmCentavos/100.0
    }

    public void setPrecoAPrazoEmReais(Double precoEmReais){
        this.precoAPrazoEmCentavos = 100*precoEmReais
    }

    public Double getSubTotalAVista(){
    	return this.precoAVistaEmReais * quantidade
    }

    public Double getSubTotalAPrazo(){
        return this.precoAPrazoEmReais * quantidade
    }
}

     
