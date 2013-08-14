package br.com.arrasaamiga

class ClienteAvulso {

	String nome
	String telefones
	String endereco
	String facebook
	String observacao

    static constraints = {
    	nome(blank:false)
    	telefones(blank:true,maxSize:1000)
    	endereco(blank:true,maxSize:5000)
    	facebook(blank:true)
    	observacao(blank:true,maxSize:5000)

    }
}
