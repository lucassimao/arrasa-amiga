package br.com.arrasaamiga

class Estoque  {

	Produto produto
    String unidade
    int quantidade

    List entradas

    static belongsTo = Produto
    static hasMany = [entradas:EntradaEstoque]

    static mapping = {
    	entradas cascade: 'all-delete-orphan'
    }
    
    static constraints = {
    	produto(nullable:false)
    	unidade(nullable:false,blank:false,unique:'produto')
    	quantidade(min:0)
    }



}
