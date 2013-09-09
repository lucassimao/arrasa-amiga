package br.com.arrasaamiga

class Aviso {

	Cliente cliente
	Produto produto
	String unidade
	Date dateCreated


    static constraints = {
    	cliente(nullable:false)
    	produto(nullable:false)
    	unidade(nullable:false,blank:false)
    	dateCreated(nullable:true)
    }

    static mapping = {
        autoTimestamp true
    }

}
