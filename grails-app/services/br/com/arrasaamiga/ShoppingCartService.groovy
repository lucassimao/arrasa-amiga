package br.com.arrasaamiga

import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder
import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder

class ShoppingCartService {

    boolean transactional = false

	def getShoppingCart() {
		def session = RequestContextHolder.currentRequestAttributes().getSession()
		if (!session.shoppingCart){
			session.shoppingCart =  new ShoppingCart()
		}
		return session.shoppingCart

	}

    def addToShoppingCart(Produto produto, String unidade, Integer qtde) {

		def shoppingCart = getShoppingCart()

		def itemVenda = shoppingCart.itens.find{ itemVenda-> 
			itemVenda.produto.id == produto.id && itemVenda.unidade.equals(unidade) 
		}

		if (itemVenda){
			itemVenda.quantidade += qtde
		}else{
			
			itemVenda = new ItemVenda(produto:produto, unidade:unidade,quantidade:qtde)
			itemVenda.precoAVistaEmCentavos = produto.precoAVistaEmCentavos
			itemVenda.precoAPrazoEmCentavos = produto.precoAPrazoEmCentavos

			shoppingCart.addToItens(itemVenda)
		}

    }

    def removeFromShoppingCart(Produto produto, String unidade, Integer quantidade) {
		def shoppingCart = getShoppingCart()


		def itemVenda = shoppingCart.itens.find{ itemVenda-> 
			itemVenda.produto.id == produto.id && itemVenda.unidade.equals(unidade) 
		}

		if (itemVenda){
			itemVenda.quantidade -= quantidade

			if (itemVenda.quantidade == 0){
				shoppingCart.removeFromItens(itemVenda)
			}
		}
		
    }

    def checkout() {
		def session = RequestContextHolder.currentRequestAttributes().getSession()
		session.shoppingCart = null
    }

    Set getItens() {
		return getShoppingCart().itens
    }
}
