package br.com.arrasaamiga

class Estoque  {

	Produto produto
    String unidade
    int quantidade

    static belongsTo = Produto
    
    static constraints = {
    	produto(nullable:false)
    	unidade(nullable:false,blank:false,unique:'produto')
    	quantidade(min:0)
    }



}
