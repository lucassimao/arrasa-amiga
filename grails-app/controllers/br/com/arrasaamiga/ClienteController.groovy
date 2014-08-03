package br.com.arrasaamiga

import org.springframework.security.web.savedrequest.*
import grails.plugin.springsecurity.annotation.Secured

@Secured(['isAuthenticated()'])
class ClienteController {


	def springSecurityService

    def index() { 
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)

        [cliente: cliente,pedidos: Venda.findAllByCliente(cliente,[max: 3,sort:'dateCreated',order:'desc'])]
    }

    def favoritos(){

    }

    def edit(){
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)

        [cliente:cliente]        
    }

    @Secured(['permitAll'])
    def cadastro(){
    	[cliente: new Cliente(usuario:new Usuario(),endereco:new Endereco())]
    }

   @Secured(['permitAll'])
   def save() {
		
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

            // caso o usuário tenha feito o cadastro enquanto estava fazendo uma compra, o envia
            // para o checkout
            String url = createLink(controller:'shoppingCart',action:'confirmAddress',absolute:true)
            if (savedRequest.redirectUrl?.equals(url))
                redirect(controller:'shoppingCart',action:'checkout')
            else
                redirect(url:savedRequest.redirectUrl)
        
        }else{

        	redirect(url:'/')
        }
       	

    }


   @Secured(['permitAll'])
   def update() {
        

        def cliente = Cliente.get(params.id)
        cliente.properties = params

        if ( !cliente.save(flush: true)) {
            render(view: "edit", model: [cliente: cliente])
            return
        }

        flash.message = 'Seus dados foram atualizados com sucesso!'
        springSecurityService.reauthenticate cliente.email

        redirect(controller:'cliente',action:'edit',model:[cliente: cliente])            

    }    


    def pedidos(){
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)

        [pedidos: Venda.findAllByCliente(cliente,[sort:'dateCreated',order:'desc'])]
    }

}
