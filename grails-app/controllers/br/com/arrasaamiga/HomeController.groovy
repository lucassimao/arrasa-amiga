package br.com.arrasaamiga

import grails.plugins.springsecurity.Secured

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

	@Secured(['ROLE_ADMIN'])
	def organizarHome(){

	}

	@Secured(['ROLE_ADMIN'])
	def left(int id){
		def produto = Produto.get(id)
		def produtoAEsquerda = Produto.findByOrdem(produto.ordem - 1)

		produto.ordem -= 1
		produtoAEsquerda.ordem += 1

		produtoAEsquerda.save(flush:true)
		produto.save(flush:true)
		redirect(action:'organizarHome')
	}

	@Secured(['ROLE_ADMIN'])
	def right(int id){
		def produto = Produto.get(id)
		def produtoADireita = Produto.findByOrdem(produto.ordem + 1)

		produto.ordem += 1
		produtoADireita.ordem -= 1

		produtoADireita.save(flush:true)
		produto.save(flush:true)
		redirect(action:'organizarHome')

	}

	@Secured(['ROLE_ADMIN'])
	def up(int id){
		def produto = Produto.get(id)
		def produtoAcima = Produto.findByOrdem(produto.ordem - 4)

		produto.ordem -= 4
		produtoAcima.ordem += 4

		produtoAcima.save(flush:true)
		produto.save(flush:true)
		redirect(action:'organizarHome')


	}

	@Secured(['ROLE_ADMIN'])
	def down(int id){
		def produto = Produto.get(id)
		def produtoAbaixo = Produto.findByOrdem(produto.ordem + 4)

		produto.ordem += 4
		produtoAbaixo.ordem -= 4

		produtoAbaixo.save(flush:true)
		produto.save(flush:true)
		redirect(action:'organizarHome')
	}

}
