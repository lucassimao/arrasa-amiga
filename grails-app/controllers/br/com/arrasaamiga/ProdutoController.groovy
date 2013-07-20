package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException

class ProdutoController {


    def index(Long id){
        def produtoInstance = Produto.get(id)
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
            return
        }

        [produtoInstance: produtoInstance]
    }

}
