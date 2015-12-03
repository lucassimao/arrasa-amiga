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
    Date lastUpdated

    transient springSecurityService

    static embedded = ['endereco']
    static transients = ['email','senha','fromTeresina','dentroDaAreaDeEntregaRapida']


    static mapping = {
        usuario cascade: 'all'
        endereco cascade: 'all'
        autoTimestamp true
       // lastUpdated type:'timestamp'
    }


    static constraints = {
        nome(blank: false, nullable: false)
        email(email: true, blank: true, nullable: true)
        celular(blank: true, nullable: true, maxSize: 9)
        dddCelular(blank: true, nullable: true, maxSize: 2)
        telefone(blank: true, nullable: true, maxSize: 9)
        dddTelefone(blank: true, nullable: true, maxSize: 2)
        endereco(nullable: true)
        usuario(nullable: true)
        dateCreated(nullable: true)
        lastUpdated(nullable:true)

    }

    def afterInsert() {
        reautenticar()
    }

    def afterUpdate() {
        reautenticar()
    }

    protected void reautenticar() {
        Cliente.withNewSession {
            if (usuario) {
                log.info 'Autenticando ' + this.email
                springSecurityService.reauthenticate(getEmail())
            }
        }
    }

    public boolean isFromTeresina(){

        return this.endereco?.cidade?.id == Cidade.teresina.id  
    }

    public boolean isDentroDaAreaDeEntregaRapida(){
        return isFromTeresina()
    }

    public void setCelular(String celular){
        if (celular)
            celular = celular.trim().replace('-','').replace(' ','')
        this.celular = celular
    }

    public void setTelefone(String telefone){
        if (telefone)
            telefone = telefone.trim().replace('-','').replace(' ','')
        this.telefone = telefone
    }

    public void setDddCelular(String dddCelular){
        this.dddCelular = dddCelular?.trim()?.replace('-','')
    }

    public void setDddTelefone(String dddTelefone){
        this.dddTelefone = dddTelefone?.trim()?.replace('-','')
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
