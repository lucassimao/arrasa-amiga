package br.com.arrasaamiga

class HomeController {


	//def shoppingCartService
    def springSecurityService	

	def index(){
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)

        [cliente : cliente]
	}

	def comocomprar(){

		try{
			def cidade = Cidade.get(params.cidade)

			if ( cidade.id == Cidade.teresina.id || cidade.id == Cidade.timon.id ){
				render(view:'comocomprar',model:[cidade: cidade.nome])
			}else{
				render(view:'comocomprar-outras-cidades',model:[cidade: cidade.nome])
			}

		}catch(Exception e){
			render(view:'comocomprar-outras-cidades',model:[cidade: ''])
		}

	}

}
