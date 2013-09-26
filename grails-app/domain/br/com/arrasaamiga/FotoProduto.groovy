package br.com.arrasaamiga

class FotoProduto {

	String comentario
	int posicao
	String unidade
	String arquivo

	static constraints = {
		comentario(blank:true,nullable:true)
		posicao(min:0)
		unidade(blank:false,nullable:false)
		arquivo(blank:false,nullable:false)
	}

}
