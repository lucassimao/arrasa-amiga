package br.com.arrasaamiga

import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder

class ShoppingCartService {

    boolean transactional = true

    def shoppingCartFactoryService

    def addToShoppingCart(Produto produto, String unidade, Integer qtde) {

		def shoppingCart = shoppingCartFactoryService.getShoppingCart()

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
		def shoppingCart = shoppingCartFactoryService.getShoppingCart()


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
		def shoppingCart = shoppingCartFactoryService.getShoppingCart()
		
		shoppingCart.itens = []

		shoppingCart.itens.each{itemVenda->
			itemVenda.delete()
		}

		shoppingCart.save()
    }

    Set getItens() {
		def shoppingCart = shoppingCartFactoryService.getShoppingCart()
		return shoppingCart.itens
    }
}
