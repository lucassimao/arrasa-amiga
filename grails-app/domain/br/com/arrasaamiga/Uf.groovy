package br.com.arrasaamiga

class Uf {

	String nome
	String sigla
	
	static mapping = {
		table 'estado'
		sigla column : 'uf'
	}

	static transients = ['piaui','maranhao']

	public static Uf getPiaui(){
		return Uf.findBySigla('PI')
	}

	public static Uf getMaranhao(){
		return Uf.findBySigla('MA')
	}

}
