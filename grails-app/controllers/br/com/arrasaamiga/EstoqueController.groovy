package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
class EstoqueController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [estoqueInstanceList: Estoque.list(params), estoqueInstanceTotal: Estoque.count()]
    }

    def show(Long id) {
        def estoqueInstance = Estoque.get(id)
        if (!estoqueInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'estoque.label', default: 'Estoque'), id])
            redirect(action: "list")
            return
        }

        [estoqueInstance: estoqueInstance]
    }

    def entrada(Long id) {
        def estoqueInstance = Estoque.get(id)

        if (!estoqueInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'estoque.label', default: 'Estoque'), id])
            redirect(action: "list")
            return
        }

        [estoqueInstance: estoqueInstance, pedidosEmAberto: Pedido.findAllByStatus(StatusPedido.Aguardando)]
    }

    def update(Long id, Long version) {
        def estoqueInstance = Estoque.get(id)

        if (!estoqueInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'estoque.label', default: 'Estoque'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (estoqueInstance.version > version) {
                estoqueInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'estoque.label', default: 'Estoque')] as Object[],
                          "Another user has updated this Estoque while you were editing")
                render(view: "edit", model: [estoqueInstance: estoqueInstance])
                return
            }
        }

        def pedido = Pedido.get(params.pedido.id)

        estoqueInstance.quantidade += pedido.quantidade
        pedido.status = StatusPedido.Recebido
        pedido.dataRecebimento = new Date()

        estoqueInstance.entradas << new EntradaEstoque(pedido:pedido,dataEntrada: new Date())


        if (!estoqueInstance.save(flush: true) && pedido.save(flush:true)) {
            render(view: "entrada", model: [estoqueInstance: estoqueInstance, pedidosEmAberto: Pedido.findAllByStatus(StatusPedido.Aguardando)])
            return
        }

        flash.message = 'Estoque atualizado'
        redirect(action: "show", id: estoqueInstance.id)
    }

}
