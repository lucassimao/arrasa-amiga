package br.com.arrasaamiga

class Endereco {

	String cep
	String complemento
	String bairro
	Cidade cidade
	Uf uf

    static constraints = {
    	cep(blank:true,nullable:true,validator:{val,obj->

            if (obj.cidade?.id != Cidade.teresina.id &&  obj.cidade?.id != Cidade.timon.id ){
                return val?.trim()?.size()>0
            }

            return true

        })
    	complemento(blank:false,nullable:false)
    	bairro(blank:false,nullable:false)
    	cidade(blank:false,nullable:false)
    	uf(nullable:false)

    }

    public void setCidade(Cidade cidade){

        switch(cidade?.id){
            case Cidade.teresina.id:
                this.cep = '64000-001'
                break
        } 

        this.@cidade = cidade
    }


    public void setCep(String _cep){
       
        switch(cidade?.id){
            case Cidade.teresina.id:
                this.@cep = '64000-001'
                break
            default:
                this.@cep = _cep
        }  

    }


}
