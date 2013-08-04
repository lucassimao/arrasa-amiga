package br.com.arrasaamiga

enum Uf {

	AC('Acre'),AL('Alagoas'),AP('Amapá'),
	AM('Amazonas'),BA('Bahia'),CE('Ceará'),
	DF('Distrito Federal'),ES('Espirito Santo'),GO('Goias'),MA('Maranhão'),
	MT('Mato Grosso'),MS('Mato Grosso do Sul'),MG('Minas Gerais'),PA('Pará'),
	PB('Paraíba'),PR('Paraná'),PE('Pernambuco'),PI('Piauí'),
	RJ('Rio de Janeiro'),RN('Rio Grande do Norte'),RS('Rio Grande do Sul'),RO('Rondônia'),
	RR('Roraima'),SC('Santa Catarina'),SP('São Paulo'),SE('Sergipe'),
	TO('Tocantins')

	String estado

	Uf(String estado){
		this.estado = estado
	}

	public String toString(){
		return this.estado
	}

	public String getKey(){ return this.name() }
    
}
