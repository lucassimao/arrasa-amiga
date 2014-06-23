package br.com.arrasaamiga

class Cliente {

	String nome
	String celular
    String dddCelular

    String dddTelefone
	String telefone

	Endereco endereco

	Usuario usuario

    Date dateCreated

	static embedded = ['endereco']
	static transients = ['email','senha','fromTeresina','dentroDaAreaDeEntregaRapida']


    static mapping = {
        usuario cascade: 'all'
        endereco cascade: 'all'
        autoTimestamp true
    }


    static constraints = {
    	nome(blank:false,nullable:false)
    	email(email:true,blank:false,nullable:false,validator: {val,obj->
            
            def other = Usuario.findByUsername(val)
            return ( other == null || other.id == obj.usuario.id )
        })
        senha(blank:false,nullable:false)
    	celular(blank:false,nullable:false,maxSize:9)
        dddCelular(blank:false,nullable:false,maxSize:2)
    	telefone(blank:false,nullable:false,maxSize:9)
        dddTelefone(blank:false,nullable:false,maxSize:2)
    	endereco(nullable:false)
    	usuario(nullable:true)
        dateCreated(nullable:true)


    }

    public boolean isFromTeresina(){

        return this.endereco?.cidade?.id == Cidade.teresina.id  
    }

    public boolean isDentroDaAreaDeEntregaRapida(){
        return isFromTeresina()
    }

    public void setCelular(String celular){
        this.celular = celular.trim().replace('-','')
    }

    public void setTelefone(String telefone){
        this.telefone = telefone.trim().replace('-','')
    }

    public void setDddCelular(String dddCelular){
        this.dddCelular = dddCelular.trim().replace('-','')
    }

    public void setDddTelefone(String dddTelefone){
        this.dddTelefone = dddTelefone.trim().replace('-','')
    }

    public String getEmail(){
    	return usuario?.username
    }

    public void setEmail(String email){
    	if (!this.usuario)
    		this.usuario = new Usuario()

    	this.usuario.username = email
    }

    public String getSenha(){
        return usuario?.password
    }

    public void setSenha(String senha){
        if (!this.usuario)
            this.usuario = new Usuario()

        this.usuario.password = senha
    }
}
