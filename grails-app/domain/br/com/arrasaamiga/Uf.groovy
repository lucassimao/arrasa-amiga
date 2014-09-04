package br.com.arrasaamiga

class Uf {

	String nome
	String sigla
	
	static mapping = {
		table 'estado'
		sigla column : 'uf'
	}

	static transients = ['piaui']

	public static Uf getPiaui(){
		return Uf.findBySigla('PI')
	}

}
