package br.com.arrasaamiga

class HomeController {


	def shoppingCartService
    def springSecurityService	

	def index(){
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)

        [cliente : cliente]
	}

}
