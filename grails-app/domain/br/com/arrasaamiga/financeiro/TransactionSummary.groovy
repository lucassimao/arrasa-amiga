package br.com.arrasaamiga.financeiro

import br.com.arrasaamiga.*

class TransactionSummary {

	Date dateCreated
	Date lastUpdated
	String code
	long valorBrutoEmCentavos
	long valorLiquidoEmCentavos 
	long descontoEmCentavos
	long taxaParcelamentoEmCentavos
	long valorExtraEmCentavos
	StatusVenda status
	String detalhes

	static contraints = {
		dateCreated(nullable:false)
		code(blank:false,nullable:false)
		valorBrutoEmCentavos(min:0L)
		valorLiquidoEmCentavos(min:0L)
		descontoEmCentavos(min:0L)
		taxaParcelamentoEmCentavos(min:0L)
		valorExtraEmCentavos(min:0L)
		status(nullable:false)
		detalhes(nullable:true,blank:true)
	}

	static mapping = {
        autoTimestamp true
    }

    static transients = ['valorBrutoEmReais','valorLiquidoEmReais','valorExtraEmReais','taxaParcelamentoEmReais','descontoEmReais']

    public double getValorBrutoEmReais(){
    	return this.valorBrutoEmCentavos/100.0
    }

    public double getValorLiquidoEmReais(){
    	return this.valorLiquidoEmCentavos/100.0
    }   

    public double getValorExtraEmReais(){
    	return this.valorExtraEmCentavos/100.0
    }  

    public double getTaxaParcelamentoEmReais(){
    	return this.taxaParcelamentoEmCentavos/100.0
    }      

    public double getDescontoEmReais(){
    	return this.descontoEmCentavos/100.0
    }

}