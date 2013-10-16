package br.com.arrasaamiga

class Aviso {

    String nome
    String email
    String celular 
	
	Produto produto
	String unidade
    String facebookUserId


	Date dateCreated


    static constraints = {
    	nome(nullable:false,blank:false)
        email(nullable:false,blank:false)
        celular(nullable:false,blank:false)         
    	produto(nullable:false)
    	unidade(nullable:false,blank:false)
    	dateCreated(nullable:true)
        facebookUserId(nullable:true,blank:true)
    }

    static mapping = {
        autoTimestamp true
    }

}
