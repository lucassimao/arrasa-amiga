package br.com.arrasaamiga

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController


@Secured(['ROLE_ADMIN'])
class EstoqueController extends RestfulController {

    static responseFormats = ['html', 'json']


    EstoqueController() {
        super(Estoque)
    }

    /**
     * So é acessado se tiver o token de autenticação incluido no request
     *
     * @return
     */
    def index(int max) {

        withFormat {
            json {
                def c = Estoque.createCriteria()

                def results = c.list() {

                    produto {
                        eq('foraDeLinha', false)
                        order('nome')
                    }

                    projections {
                        produto {
                            property('id')
                            property('nome')
                            property('precoAVistaEmCentavos')
                            property('precoAPrazoEmCentavos')
                        }

                        property('unidade')
                        property('quantidade')
                    }
                }
                render results as JSON
            }
            '*' {
                params.max = Math.min(max ?: 10, 100)
                def c = Estoque.createCriteria()

                def results = c.list(params) {
                    produto {
                        eq('foraDeLinha', false)
                        order('nome')
                    }
                }
                respond results, model: [estoqueInstanceTotal: Estoque.count()]
            }
        }

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

        estoqueInstance.entradas << new EntradaEstoque(pedido: pedido, dataEntrada: new Date())


        if (!estoqueInstance.save(flush: true) && pedido.save(flush: true)) {
            render(view: "entrada", model: [estoqueInstance: estoqueInstance, pedidosEmAberto: Pedido.findAllByStatus(StatusPedido.Aguardando)])
            return
        }

        flash.message = 'Estoque atualizado'
        redirect(action: "show", id: estoqueInstance.id)
    }

}