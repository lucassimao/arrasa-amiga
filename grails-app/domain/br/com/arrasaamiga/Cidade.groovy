package br.com.arrasaamiga

class Cidade {

	String nome
	Uf uf
	
	static transients = ['teresina']

	static mapping = {
		uf column : 'estado'
	}

	public static Cidade getTeresina(){
		return Cidade.findByUfAndNome(Uf.piaui,'Teresina')
	}

}
