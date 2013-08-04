package br.com.arrasaamiga

class GrupoDeUsuario {

	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
