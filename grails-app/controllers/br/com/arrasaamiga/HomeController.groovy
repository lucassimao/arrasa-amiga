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

			if (cidade?.uf?.sigla == 'PI' && cidade?.nome == 'Teresina'){
				render(view:'comocomprar-teresina')
			}else{
				render(view:'comocomprar-outras-cidades')
			}

		}catch(Exception e){
			render(view:'comocomprar-outras-cidades')
		}

	}

}
