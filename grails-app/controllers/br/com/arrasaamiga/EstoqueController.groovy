package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured
import grails.converters.JSON



class EstoqueController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @Secured(['ROLE_ADMIN'])
    def index() {
        redirect(action: "list", params: params)
    }


    def listAsJson(){
        def c = Estoque.createCriteria()

        def results = c.list () {

            produto{
                and{
                    eq('visivel',true) 
                }
                order('nome')
            }

            projections{
                property('id')
                produto{
                    property('nome')
                }

                property('unidade')
                property('quantidade')
            }
        }       

        render results as JSON

    }

    @Secured(['ROLE_ADMIN'])
    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        def c = Estoque.createCriteria()

        def results = c.list (params) {
            produto{
                and{
                    eq('visivel',true)
                    order('nome') 
                }
            }
        }


        [estoqueInstanceList: results , estoqueInstanceTotal: Estoque.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def show(Long id) {
        def estoqueInstance = Estoque.get(id)
        if (!estoqueInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'estoque.label', default: 'Estoque'), id])
            redirect(action: "list")
            return
        }

        [estoqueInstance: estoqueInstance]
    }

    @Secured(['ROLE_ADMIN'])
    def entrada(Long id) {
        def estoqueInstance = Estoque.get(id)

        if (!estoqueInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'estoque.label', default: 'Estoque'), id])
            redirect(action: "list")
            return
        }

        [estoqueInstance: estoqueInstance, pedidosEmAberto: Pedido.findAllByStatus(StatusPedido.Aguardando)]
    }
    

    @Secured(['ROLE_ADMIN'])
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
