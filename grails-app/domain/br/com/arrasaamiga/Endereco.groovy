package br.com.arrasaamiga

class Endereco {

	String cep
	String complemento
	String bairro
	Cidade cidade
	Uf uf

    static constraints = {
    	cep(blank:true,nullable:true,validator:{val,obj->

            if (obj.cidade?.id != Cidade.teresina.id ){
                return val?.trim()?.size()>0
            }

            return true

        })
    	complemento(blank:true,nullable:true)
    	bairro(blank:false,nullable:false)
    	cidade(blank:false,nullable:false)
    	uf(nullable:false)

    }


    public void setCidade(Cidade cidade){
        if (cidade?.id == Cidade.teresina.id){
            this.cep = ''
        }
        this.cidade = cidade
    }

}
