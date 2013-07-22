package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException

class ProdutoController {

    def shoppingCartService

    def index(Long id){
        def produtoInstance = Produto.get(id)
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
            return
        }

        [produtoInstance: produtoInstance,qtdeIitensCarrinho: shoppingCartService.getItems()?.size()]
    }

    def addToShoppingCart(Long id,Integer quantidade){

        def produtoInstance = Produto.get(id)

        if (produtoInstance) {
            produtoInstance.addQuantityToShoppingCart(quantidade)
            flash.message = "${produtoInstance.nome} adicionado(a) ao seu carrinho de compras"
            render 'ok'
        }else{
             flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
            return           
        }        
    }

}
