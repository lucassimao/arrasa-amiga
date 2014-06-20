package br.com.arrasaamiga

import org.springframework.security.web.savedrequest.*
import grails.plugin.springsecurity.annotation.Secured

class ClienteController {


	def springSecurityService

    def index() { }

    def cadastro(){
    	[cliente: new Cliente(usuario:new Usuario(),endereco:new Endereco())]
    }

   def salvarNovoCliente() {
		
        def cliente = new Cliente(params)
        cliente.usuario.enabled = true

        if ( !cliente.save(flush: true)) {
            render(view: "cadastro", model: [cliente: cliente])
            return
        }

        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response)
        flash.message = 'Bem vinda amiga! Sua conta foi criada com sucesso.'
        springSecurityService.reauthenticate cliente.email

        if (savedRequest){

        	redirect(url:savedRequest.redirectUrl)
        
        }else{

        	redirect(url:'/')
        }
       	

    }

    @Secured(['isAuthenticated()'])
    def pedidos(){
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)

        [pedidos: Venda.findAllByCliente(cliente)]
    }

}
