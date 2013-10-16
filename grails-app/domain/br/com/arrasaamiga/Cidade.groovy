package br.com.arrasaamiga

class Cidade {

	String nome
	Uf uf
	
	static transients = ['teresina','timon']

	static mapping = {
		uf column : 'estado'
	}

	public static Cidade getTeresina(){
		return Cidade.findByUfAndNome(Uf.piaui,'Teresina')
	}

	public static Cidade getTimon(){
		return Cidade.findByUfAndNome(Uf.maranhao,'Timon')
	}

}
