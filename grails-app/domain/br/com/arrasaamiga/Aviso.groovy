package br.com.arrasaamiga

class Aviso {

    String nome
    String email
    String dddTelefone
    String telefone
    String dddCelular
    String celular 
	
	Produto produto
	String unidade


	Date dateCreated


    static constraints = {
    	nome(nullable:false,blank:false)
        email(nullable:false,blank:false)
        dddTelefone(nullable:false,blank:false)
        telefone(nullable:false,blank:false)   
        dddCelular(nullable:false,blank:false)
        celular(nullable:false,blank:false)         
    	produto(nullable:false)
    	unidade(nullable:false,blank:false)
    	dateCreated(nullable:true)
    }

    static mapping = {
        autoTimestamp true
    }

}
