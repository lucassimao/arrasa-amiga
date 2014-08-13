package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.*
import org.codehaus.groovy.grails.web.json.*

class SalvarContatoCommand{

	String nome
	String email
	String dddCelular
	String celular
	String mensagem

	static constraints ={
		nome blank:false, nullable:false
		email blank:false, nullable:false, email:true
		dddCelular blank:true, nullable:true
		celular blank:true, nullable:true
		mensagem blank:false, nullable:false
	}
}

@Secured(['permitAll'])
class HomeController {


    def springSecurityService	
    def emailService

    static allowedMethods = [pwdRecovery: 'POST']

	def index(){
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)
        def banners = Banner.findAllByVisivel(true)

        List subGrupos = []
        def grupoDeProduto = GrupoDeProduto.findByNome(params.grupoDeProduto)
        if (grupoDeProduto){
        	subGrupos << grupoDeProduto.id
        	subGrupos += grupoDeProduto.descendentes
        }


		def criteria = Produto.createCriteria()
		def produtos = criteria.listDistinct {
		    eq("visivel", true)
		    order("ordem", "asc")
		    
		    if(grupoDeProduto){

			    grupos{
			    	inList("id", subGrupos)
			    }
		    }
		}


		[produtos:produtos,grupoRaiz:grupoDeProduto?.grupoRaiz?.nome,banners:banners]

	}

	def pwdRecovery(String email){
		Usuario user = Usuario.findByUsername(email)

		if (user){

			def max = 5000
			def min = 0
			def novaSenha = min + (int)(Math.random() * ((max - min) + 1))

			emailService.enviarNovaSenha(Cliente.findByUsuario(user), novaSenha.toString())
			user.password = novaSenha
			user.save(flush:true)

			flash.message = "Uma nova senha foi criada e enviada para o e-mail informado !"
		}else{
			flash.error = 'Não existe usuário cadastrado com o e-mail informado!'
			[email: email]
		}
	}

	def salvarContato(SalvarContatoCommand cmd){

		if (!cmd.hasErrors()){
			emailService.notificarContato(cmd.nome, cmd.celular, cmd.email, cmd.mensagem)
			flash.message = 'Sua mensagem foi enviada com sucesso!'
			render view:'/home/contato'
			return

		}else{
			println cmd.errors
			render view:'/home/contato', model:[cmd:cmd]
			return			
		}
	}

	def comoComprar(){

		if (params.cidade){
			def cidade = Cidade.get(params.cidade)

			boolean hasEntregaRapida = true
			if (cidade.id != Cidade.teresina.id)
				hasEntregaRapida = false

			render(view:'comoComprar',model:[cidade: cidade, hasEntregaRapida: hasEntregaRapida])
		}else{
			render template:'modalSelectCidade'
		}

	}

    def getCidades(int idUf){
        def uf = Uf.get(idUf)
        def cidades = Cidade.findAllByUf(uf)

        def retorno = []

        cidades.each{c->
            retorno << ['id':c.id,'nome':c.nome]
        }

        render ( retorno as JSON)
    }	

	@Secured(['ROLE_ADMIN'])
	def organizarHome(){
		def criteria = Produto.createCriteria()
		def produtos = criteria.list {
		    eq("visivel", true)
		    order("ordem", "asc")
		}


		[produtos:produtos]
	}

	@Secured(['ROLE_ADMIN'])
	def atualizarOrdemDosItens(){

		
		params['ordem[]'].eachWithIndex{id, index->
			def p = Produto.load(id)
			p.ordem = index
			p.save(flush:true)
		}

		render 'ok'

	}




}
