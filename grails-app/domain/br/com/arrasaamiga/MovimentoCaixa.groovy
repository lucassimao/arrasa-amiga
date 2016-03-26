package br.com.arrasaamiga

import groovy.transform.ToString

@ToString(includeNames=true)
class MovimentoCaixa {

    Date dateCreated,lastUpdated
    TipoMovimentoCaixa tipoMovimentoCaixa
    long valorEmCentavos
    FormaPagamento formaPagamento
    String descricao
    Date data

    static constraints = {
    	tipoMovimentoCaixa(nullable:true)
    	valorEmCentavos(min:0L)
    	formaPagamento(nullable:false)
        lastUpdated(nullable:true)
    }

    static mapping = {
        autoTimestamp true
    }

}
