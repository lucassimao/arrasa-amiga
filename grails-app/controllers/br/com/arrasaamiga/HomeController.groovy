package br.com.arrasaamiga

import grails.plugins.springsecurity.Secured
import grails.converters.*
import org.codehaus.groovy.grails.web.json.*

class HomeController {


	//def shoppingCartService
    def springSecurityService	

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
