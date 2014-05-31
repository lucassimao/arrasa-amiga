package br.com.arrasaamiga

import grails.plugins.springsecurity.Secured
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

class HomeController {


	//def shoppingCartService
    def springSecurityService	
    def emailService


	def index(){
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)

		def criteria = Produto.createCriteria()
		def produtos = criteria.list {
		    eq("visivel", true)
		    order("ordem", "asc")
		}


		[produtos:produtos]

	}

	def salvarContato(SalvarContatoCommand cmd){

		if (!cmd.hasErrors()){
			//emailService.notificarContato(cmd.nome, cmd.celular, cmd.email, cmd.mensagem)
			flash.message = 'Sua mensagem foi enviada com sucesso!'
			render view:'/home/contato'
			return

		}else{
			render view:'/home/contato', model:[cmd:cmd]
			return			
		}
	}

	def comocomprar(){

		try{
			def cidade = Cidade.get(params.cidade)

			if ( cidade.id == Cidade.teresina.id  ){
				render(view:'comocomprar',model:[cidade: cidade.nome])
			}else{
				render(view:'comocomprar-outras-cidades',model:[cidade: cidade.nome])
			}

		}catch(Exception e){
			render(view:'comocomprar-outras-cidades',model:[cidade: ''])
		}

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
			p.save()
		}

		render 'ok'

	}




}
