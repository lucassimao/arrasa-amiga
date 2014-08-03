package br.com.arrasaamiga

import org.springframework.security.web.savedrequest.*
import grails.plugins.springsecurity.Secured
import org.springframework.validation.FieldError


class ClienteController {


	def springSecurityService

    def index() { }

    def cadastro(){
    	[cliente: new Cliente(usuario:new Usuario(),endereco:new Endereco())]
    }

    // ClienteCommand e EnderecoCommand estao definidos no ShoppingartController
   def salvarNovoCliente(ClienteCommand command) {
		
        def cliente = new Cliente(params)
        cliente.usuario.enabled = true

        command.validate()
        command.endereco.validate()

        if (command.hasErrors() || command.endereco.hasErrors() ){ 
            
            command.errors.allErrors.each {FieldError error ->
                String field = error.field
                String code = error.code
                cliente.errors.rejectValue(field, code)
            }

            command.endereco.errors.allErrors.each{FieldError error->
                String field = error.field
                String code = error.code
                cliente.endereco.errors.rejectValue(field, code)
            }

            render(view: "cadastro", model: [cliente: cliente])
            return           
        }  

        cliente.save(flush: true)

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
