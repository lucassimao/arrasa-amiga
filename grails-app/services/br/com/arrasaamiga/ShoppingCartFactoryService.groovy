package br.com.arrasaamiga

import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder

class ShoppingCartFactoryService {

    boolean transactional = true

    private String getSessionID(){
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

}
