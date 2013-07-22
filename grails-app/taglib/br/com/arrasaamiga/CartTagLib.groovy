package br.com.arrasaamiga

import com.metasieve.shoppingcart.*

class CartTagLib {

	static namespace = 'cart'

	def shoppingCartService

	def qtdeTotalItens = { attrs, body ->
		 def shoppingCart =  ShoppingCart.get( shoppingCartService.shoppingCart.id )
		 def criteria = Quantity.createCriteria()

		 def qtde = criteria.get{
		 	eq('shoppingCart',shoppingCart)
			projections {
			        sum "value"
			}
		 }

		out << ( (qtde)?:0)
	}

}
