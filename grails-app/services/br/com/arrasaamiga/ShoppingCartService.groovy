package br.com.arrasaamiga

import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder

class ShoppingCartService {

    boolean transactional = true

    public String getSessionID(){
    	def session = RequestContextHolder.currentRequestAttributes().getSession()
		return session.id    	
    }

    private ShoppingCart createShoppingCart() {
		def sessionID = getSessionID()

		def shoppingCart = new ShoppingCart(sessionID:sessionID)
		shoppingCart.save()
		
		return shoppingCart
    }

	def getShoppingCart() {
		def sessionID = getSessionID()
		def shoppingCart = ShoppingCart.findBySessionIDAndCheckedOut(sessionID, false)
		
		if (!shoppingCart) {
			shoppingCart = createShoppingCart()
		}
		
		return shoppingCart
	}

    def addToShoppingCart(Produto produto, String unidade, Integer qtde) {
		def shoppingCart = getShoppingCart()

		def itemVenda = shoppingCart.itens.find{ itemVenda-> 
			itemVenda.produto.id == produto.id && itemVenda.unidade.equals(unidade) 
		}

		if (itemVenda){
			
			itemVenda.quantidade += qtde
			itemVenda.save()

		}else{
			
			itemVenda = new ItemVenda(produto:produto, unidade:unidade,quantidade:qtde)
			itemVenda.precoAVistaEmCentavos = produto.precoAVistaEmCentavos
			itemVenda.precoAPrazoEmCentavos = produto.precoAPrazoEmCentavos

			itemVenda.save()

			shoppingCart.addToItens(itemVenda)
			shoppingCart.save()
		}

    }

    def removeFromShoppingCart(Produto produto, String unidade, Integer quantidade) {
		def shoppingCart = getShoppingCart()


		if (!shoppingCart) {
			return
		}

		def itemVenda = shoppingCart.itens.find{ itemVenda-> 
			itemVenda.produto.id == produto.id && itemVenda.unidade.equals(unidade) 
		}

		if (itemVenda){
			itemVenda.quantidade -= quantidade
			itemVenda.save()

			if (itemVenda.quantidade == 0){
				shoppingCart.removeFromItens(itemVenda)
				itemVenda.delete()

				shoppingCart.save(flush:true)
			}
		}
		
    }

    def emptyShoppingCart() {
		def shoppingCart = getShoppingCart()
		
		shoppingCart.itens = []
		shoppingCart.itens.each{itemVenda->
			itemVenda.delete()
		}

		shoppingCart.save()
    }

    Set getItens() {
		def shoppingCart = getShoppingCart()
		return shoppingCart.itens
    }

    List checkOut() {
		def shoppingCart = getShoppingCart()
		
		List checkedOutItems = []

		shoppingCart.itens.each { item ->
			checkedOutItems.add(item)
		}
		
		
		shoppingCart.save()
		
		return checkedOutItems
    }
}
