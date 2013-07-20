package br.com.arrasaamiga

class Produto {

	String nome
	List fotos
	String descricao
	int preco


	static hasMany = [fotos:String]
	static transients = ['precoEmReais']

    static constraints = {
    	nome(nullable:false,blank:false)
    	descricao(nullable:false,blank:false,maxSize:100000)
    	preco(min:0)
    }


    public Double getPrecoEmReais(){
    	return this.preco/100
    }

}
