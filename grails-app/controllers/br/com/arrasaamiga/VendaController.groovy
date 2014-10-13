package br.com.arrasaamiga

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.*
import org.codehaus.groovy.grails.web.servlet.HttpHeaders

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Secured(['IS_AUTHENTICATED_FULLY'])
class VendaController extends RestfulController {

    static responseFormats = ['html', 'json']


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

    def marcarComoEntregue(Long id) {
        def v = Venda.get(id)
        v.status = StatusVenda.Entregue

        v.save(flush: true)

        flash.message = 'A venda foi marcada como entregue'

        if (params.offset && params.max) {
            redirect(action: 'list', params: [offset: params.offset, max: params.max])
        } else {
            redirect(action: 'list')
        }


    }


    def cancelada() { }
    def aguardandoPagamento() { }



    def index() {
        withFormat {
            html { redirect(action: "list", params: params) }
            json {
                respond Venda.findAllByStatusInList([StatusVenda.PagamentoRecebido, StatusVenda.AguardandoPagamento])
            }
        }
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 1000)
        params.sort = 'dateCreated'
        params.order = 'desc'

        [vendaInstanceList: Venda.list(params), vendaInstanceTotal: Venda.count()]
    }

}