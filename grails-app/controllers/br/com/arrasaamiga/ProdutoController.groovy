package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.*

class ProdutoController {

    def shoppingCartService

    def show(Long id){
        def produtoInstance = Produto.get(id)
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
            return
        }

        [produtoInstance: produtoInstance,qtdeIitensCarrinho: shoppingCartService.getItems()?.size()]
    }



}
