package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NO_CONTENT

@Secured(['ROLE_ADMIN'])
class VendaController extends RestfulController {

    static responseFormats = ['html', 'json']
    static allowedMethods = [marcarEntregue: 'POST']


    VendaController() {
        super(Venda)
    }

    def setTrackingCode(Long id, String trackingCode) {

        if (trackingCode) {
            def v = Venda.load(id)
            v.codigoRastreio = trackingCode
            v.save(flush: true)
            flash.message = 'Código de rastreio atualizado com sucesso'
        }

        redirect(action: 'list', params: [offset: params.offset, max: params.max])

    }


    def showFull(Long id) {

        def venda = Venda.get(id)

        if (venda) {
            [numeroPedido: String.format("%05d", venda.id), venda: venda, cliente: venda.cliente]

        } else {
            println "Erro ao carregar venda ${id}"
            render 'Venda não encontrada'
        }

    }

    def marcarEntregue(Long id) {
       def v = Venda.get(id)
        v.status = StatusVenda.Entregue

        v.save(flush: true)

        flash.message = 'A venda foi marcada como entregue'

        if (params.offset && params.max) {
            redirect(action: 'index', params: [offset: params.offset, max: params.max])
        } else {
            redirect(action: 'index')
        }
    }

    @Secured(['permitAll'])
    def cancelada() {}

    @Secured(['permitAll'])
    def aguardandoPagamento() {}


    def index(int max) {

        withFormat {
            json {
                respond Venda.findAllByStatusInList([StatusVenda.PagamentoRecebido, StatusVenda.AguardandoPagamento])
            }
            '*' {
                params.max = Math.min(max ?: 10, 1000)
                params.sort = 'dateCreated'
                params.order = 'desc'

                respond Venda.list(params), model: [vendaInstanceTotal: Venda.count()]
            }
        }
    }

}