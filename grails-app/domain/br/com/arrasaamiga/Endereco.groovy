package br.com.arrasaamiga

class Endereco {

	String cep
	String endereco
	String complemento
	String pontoDeReferencia
	String bairro
	String cidade
	Uf uf = Uf.PI

    static constraints = {
    	cep(blank:false,nullable:false)
    	endereco(blank:false,nullable:false)
    	complemento(blank:true,nullable:true)
    	bairro(blank:false,nullable:false)
    	cidade(blank:false,nullable:false)
    	pontoDeReferencia(blank:true,nullable:true)
    	uf(nullable:false)

    }


    public boolean isFromTeresina(){
        return (this.cep) && this.cep.startsWith('640') // 64000-001 a 64099-999
    }

    public boolean isFromTimon(){
        return (this.cep) && this.cep.startsWith('6563') // 65630-001 a 65638-999
    }
}
