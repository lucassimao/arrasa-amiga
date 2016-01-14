package br.com.arrasaamiga

class MovimentoCaixa {


    Date dateCreated
    TipoMovimentoCaixa tipoMovimentoCaixa
    long valorEmCentavos
    FormaPagamento formaPagamento


    static constraints = {
    	tipoMovimentoCaixa(nullable:true)
    	valorEmCentavos(min:0L)
    	formaPagamento(nullable:false)
    }

    static mapping = {
        autoTimestamp true
    }

}
