package br.com.arrasaamiga

class PagamentoInvestimento {

	Date dateCreated
	long valorEmCentavos

	static transients = ['valorEmReais']

	static constraints = {
		valorEmCentavos(min:0l)
		dateCreated(nullable:false)
	}

    public Double getValorEmReais(){
    	return this.valorEmCentavos/100.0
    }

    public void setValorEmReais(double valor){
    	this.valorEmCentavos = valor * 100
    }
}
