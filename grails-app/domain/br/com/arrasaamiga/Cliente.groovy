package br.com.arrasaamiga

class Cliente {

	String nome
	Date dataNascimento

	String celular
	String telefone
	Endereco endereco

	Usuario usuario

	static embedded = ['endereco']
	static transients = ['email','senha']


    static mapping = {
        usuario cascade: 'all'
    }


    static constraints = {
    	nome(blank:false,nullable:false)
    	email(email:true,blank:false,nullable:false)
        senha(blank:false,nullable:false)
    	celular(blank:false,nullable:false)
    	telefone(blank:false,nullable:false)
    	endereco(nullable:false)
    	dataNascimento(nullable:false)
    	usuario(nullable:false)


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
