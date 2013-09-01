package br.com.arrasaamiga

class Investimento {

	String descricao
	int valorEmCentavos
	String titular
	Date dateCreated
	List pagamentos

	static transients = ['valorEmReais','valorQuitado','debito']
	static hasMany = [pagamentos: PagamentoInvestimento]

    static constraints = {
    	descricao(blank:false)
    	valorEmCentavos(min:0)
    	titular(blank:false)
    }

    public Double getValorEmReais(){
    	return this.valorEmCentavos/100.0
    }

    public void setValorEmReais(Double valor){
    	this.valorEmCentavos = valor * 100
    }

    public Double getValorQuitado(){
    	double quitadoEmReais = 0

    	pagamentos?.each{ pgto->
    		quitadoEmReais += pgto.valorEmReais
    	}    	

    	return quitadoEmReais
    }

    public Double getDebito(){
    	return getValorEmReais() - getValorQuitado()
    }
}
