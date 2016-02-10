package br.com.arrasaamiga.financeiro

import grails.plugin.springsecurity.annotation.Secured
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import br.com.arrasaamiga.*

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])
class TransactionSummaryController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def pagSeguroService
    def caixaService

    private def getVendaSemTransacoes(){
        def inicio = caixaService.inicioCaixaAtual
        def fim = caixaService.fimCaixaAtual

        def criteria = Venda.createCriteria()
        return criteria.list{

            between("dataEntrega", inicio, fim)
            eq('formaPagamento',FormaPagamento.PagSeguro)
            eq('status',StatusVenda.PagamentoRecebido)

        }
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TransactionSummary.list(params), model:[transactionSummaryInstanceCount: TransactionSummary.count()]
    }

    def show(TransactionSummary transactionSummaryInstance) {
        respond transactionSummaryInstance
    }

    def edit(TransactionSummary transactionSummaryInstance) {
        respond transactionSummaryInstance, model:['vendasSemTransacoes': getVendaSemTransacoes()]
    }

    @Transactional
    def update(TransactionSummary transactionSummaryInstance,Venda venda) {
        if (transactionSummaryInstance == null) {
            notFound()
            return
        }

        if (transactionSummaryInstance.hasErrors()) {
            respond transactionSummaryInstance.errors, view:'edit'
            return
        }

        if (!venda){
            flash.error = 'A venda deve ser selecionada'
            respond transactionSummaryInstance, view:'edit', model:['vendasSemTransacoes': getVendaSemTransacoes()]
            return
        }


        def transacao =  pagSeguroService.getTransaction(transactionSummaryInstance.code)

        venda.transacaoPagSeguro = transactionSummaryInstance.code
        venda.detalhesPagamento = pagSeguroService.getDetalhesPagamento(transacao)
        venda.status = transactionSummaryInstance.status
        venda.descontoPagSeguroEmCentavos = transactionSummaryInstance.descontoEmCentavos
        venda.taxasPagSeguroEmCentavos = transactionSummaryInstance.taxaParcelamentoEmCentavos
        transactionSummaryInstance.delete()

        request.withFormat {
            form multipartForm {
                flash.message = 'Transação associada com sucesso!'
                redirect action:'index', method: "GET"
            }
            '*'{ render status: OK }
        }
    }

    @Transactional
    def delete(TransactionSummary transactionSummaryInstance) {

        if (transactionSummaryInstance == null) {
            notFound()
            return
        }

        transactionSummaryInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'TransactionSummary.label', default: 'TransactionSummary'), transactionSummaryInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'transactionSummary.label', default: 'TransactionSummary'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
