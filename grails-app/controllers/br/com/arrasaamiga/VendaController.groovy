package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.servlet.HttpHeaders
import org.apache.commons.logging.LogFactory
import  br.com.arrasaamiga.excecoes.EstoqueException
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.OK


@Secured(['ROLE_ADMIN','ROLE_VENDEDOR'])
class VendaController extends RestfulController {

    static responseFormats = ['html', 'json']
    static allowedMethods = [marcarEntregue: 'POST',addAnexo:'POST']

    def vendaService
    def estoqueService

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

        redirect(action: 'index', params: [offset: params.offset, max: params.max])

    }

    def showPedidosEnviadosPelosCorreios(int max) {
        params.max = Math.min(max ?: 10, 1000)
        params.sort = 'dateCreated'
        params.order = 'desc'

        respond Venda.findAllByServicoCorreioIsNotNullAndStatus(StatusVenda.PagamentoRecebido, params),
                model: [vendaInstanceTotal: Venda.countByServicoCorreioIsNotNullAndStatus(StatusVenda.PagamentoRecebido)]
    }

    def setEncomendaEntregue(int id){
        def venda = Venda.get(id)
        if (venda){
            venda.status = StatusVenda.Entregue
            venda.save(flush:true)
            render(text:"Venda #${venda.id} entregue!")
        }
    }

    def handleEstoqueException(EstoqueException e){
        def vendaLogger = LogFactory.getLog('grails.app.domain.br.com.arrasaamiga.Venda')

        render text: e.message, status: BAD_REQUEST
        vendaLogger.debug e
    }

    @Transactional
    @Override
    def update() {
        if(handleReadOnly()) {
            return
        }

        Venda instance = queryForResource(params.id)

        if (instance == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        /* verificando se a atualização envolve os itens da venda
         * caso contenha alterações no atributo, devolve os itens atuais para
         * mais tarde remover os itens enviados pela requisição
         */
        def json = getObjectToBind().JSON
        def updateShoppingCart = json.containsKey('carrinho')
        if(updateShoppingCart)
            estoqueService.reporItens(instance.itensVenda)

        instance.properties = json
        if (instance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond instance.errors, view:'edit' // STATUS CODE 422
            return
        }

        // forçando atualização, uma vez que
        // atualizacoes no obj cliente nao atualizam
        // o last_updated da venda
        instance.lastUpdated = new Date()
        instance.save flush:true

        if(updateShoppingCart)
            estoqueService.removerItens(instance.itensVenda)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: "${resourceClassName}.label".toString(),
                                default: resourceClassName), instance.id])
                redirect instance
            }
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        g.createLink(
                                resource: this.controllerName, action: 'show',id: instance.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond instance, [status: OK]
            }
        }
    }


    @Transactional
    @Override
    def save() {
        if (handleReadOnly()) {
            return
        }
        def instance = createResource()

        instance.validate()
        if (instance.hasErrors()) {
            respond instance.errors, view: 'create' // STATUS CODE 422
            return
        }

        vendaService.salvarVenda(instance, false)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: "${resourceName}.label".toString(), default: resourceClassName), instance.id])
                redirect instance
            }
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        g.createLink(
                                resource: this.controllerName, action: 'show', id: instance.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null))
                respond instance, [status: CREATED]
            }
        }
    }

    @Transactional
    @Override
    def delete() {
        if (handleReadOnly()) {
            return
        }

        def instance = queryForResource(params.id)
        if (instance == null) {
            notFound()
            return
        }
        // assim que e venda eh excluida, o campo
        // lastUpdated eh atualizado p/ refletir no cliente
        // a exclusao recente
        vendaService.excluirVenda(instance)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: "${resourceClassName}.label".toString(), default: resourceClassName), instance.id])
                redirect action: "index", method: "GET"
            }
            '*' {
                render status: NO_CONTENT
            } // NO CONTENT STATUS CODE
        }
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

    @Secured(['isAuthenticated()'])
    def show(long id) {
        respond Venda.get(id)
    }

    /**
      * esse metodo eh chamado pela action index
      * para listar todos os objetos a serem exibidos na view.
      * Redefini para acrescentar a ordenação pela data de criação da venda
      */
    protected List<Venda> listAllResources(Map params){
        if (!params['sort'])
            params['sort'] = 'dateCreated'
        if (!params['order'])
            params['order'] = 'desc'
        return super.listAllResources(params)
    }


}
