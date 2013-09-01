package br.com.arrasaamiga

class CartTagLib {

	static namespace = 'cart'

	def shoppingCartService

	def qtdeTotalItens = { attrs, body ->
		def cart = shoppingCartService.shoppingCart
		def qtde = cart.itens?.sum {c-> c.quantidade }

		out << ( (qtde)?:0)
	}

}
