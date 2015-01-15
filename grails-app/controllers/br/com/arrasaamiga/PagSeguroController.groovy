package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import org.apache.commons.logging.LogFactory
import static org.springframework.http.HttpStatus.*

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Secured(['permitAll'])
class PagSeguroController {

    def pagSeguroService
    def emailService

    private static final vendaLogger = LogFactory.getLog('grails.app.domain.br.com.arrasaamiga.Venda')

    def index() {

        redirect(uri: '/')
    }

    @Secured(['isAuthenticated()'])
    def retorno() {

        def codigoTransacao = params.transacao
        def transacaoPagSeguro = pagSeguroService.getTransaction(codigoTransacao)

        def venda = Venda.get(params.id)
        boolean vendaJaEstavaCancelada = ( venda.status == StatusVenda.Cancelada ) // ja recebeu a notificacao de cancelamento

        vendaLogger.debug " * retorno: venda ${venda.id}; transacaoPagSeguro ${transacaoPagSeguro} "
        vendaLogger.debug " ** retorno: status anterior ${venda.status} "

        venda.transacaoPagSeguro = codigoTransacao
        venda.status = pagSeguroService.getStatusTransacao(transacaoPagSeguro.status)
        venda.descontoPagSeguroEmCentavos = transacaoPagSeguro.getDiscountAmount() * 100
        venda.taxasPagSeguroEmCentavos = transacaoPagSeguro.getFeeAmount() * 100
        venda.save(flush: true)

        vendaLogger.debug " *** retorno: novo status ${venda.status} "

        switch (venda.status) {
            case StatusVenda.PagamentoRecebido:

                vendaLogger.debug("**** retorno: venda/show/${venda.id} e apagando carrinho")
                session.shoppingCart = null
                redirect(controller: 'venda', action: 'show', id: venda.id)

                break
            case StatusVenda.Cancelada:
                redirect(controller: 'venda', action: 'cancelada')

                break
            default: // AguardandoPagamento ou EmAnalise
                redirect(controller: 'venda', action: 'aguardandoPagamento')
        }

    }

    def notificacoes() {
        def venda = Venda.get(params.id)


        if (venda == null) {
            render status: NOT_FOUND
            return
        }

        vendaLogger.debug("* notificacoes p/ venda ${venda.id} com status ${venda.status}")

        def notificationCode = params.notificationCode
        vendaLogger.debug("** notificacoes notificationCode ${notificationCode}")

        def transaction = pagSeguroService.checkTransaction(notificationCode)
        vendaLogger.debug("*** notificacoes transaction ${transaction.code} transacaoPagSeguro ${venda.transacaoPagSeguro}")

        venda.transacaoPagSeguro = transaction.code
        venda.status = pagSeguroService.getStatusTransacao(transaction.status)

        vendaLogger.debug "**** notificacoes novo status ${venda.status}"

        venda.save(flush:true)


        switch (venda.status) {
            case StatusVenda.PagamentoRecebido:

                vendaLogger.debug "***** notificacoes - confirmacao de pagamento da venda ${venda.id}"

                emailService.notificarAdministradores(venda)
                emailService.notificarCliente(venda)

                break
            case StatusVenda.Cancelada:

                vendaLogger.debug "***** notificacoes - cancelando venda ${venda.id}"
                Estoque.reporItens(venda.itensVenda)
                //TODO enviar email avisando cancelamento cancelando
                break

        }

        render status: OK
        return

    }
}
