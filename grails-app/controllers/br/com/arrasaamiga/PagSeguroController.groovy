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
    def estoqueService

    private static final vendaLogger = LogFactory.getLog('grails.app.domain.br.com.arrasaamiga.Venda')

    def index() {

        redirect(uri: '/')
    }

    @Secured(['isAuthenticated()'])
    def retorno() {

        def codigoTransacao = params.transacao
        def transacaoPagSeguro = pagSeguroService.getTransaction(codigoTransacao)

        vendaLogger.debug " * retorno: venda ${params.id}; transacaoPagSeguro ${transacaoPagSeguro} "
        def status = pagSeguroService.getStatusTransacao(transacaoPagSeguro.status)

        switch (status) {
            case StatusVenda.PagamentoRecebido:
                vendaLogger.debug("**** retorno: venda/show/${params.id} e apagando carrinho")
                session.shoppingCart = null
                redirect(controller: 'venda', action: 'show', id: params.id)

                break
            case StatusVenda.Cancelada:
                vendaLogger.debug("**** retorno: Vena ${params.id} cancelada")
                redirect(controller: 'venda', action: 'cancelada')

                break
            case StatusVenda.AguardandoPagamento:
            case StatusVenda.EmAnalise:
                redirect(controller: 'venda', action: 'aguardandoPagamento')
                break
            default:
                break
        }

    }

    def notificacoes() {


        def notificationCode = params.notificationCode
        vendaLogger.debug("* notificacoes notificationCode ${notificationCode}")

        def transacaoPagSeguro = pagSeguroService.checkTransaction(notificationCode)
        vendaLogger.debug("** notificacoes transacaoPagSeguro ${transacaoPagSeguro.code} ")

        def venda = Venda.get(params.id)
        if (venda == null) {
            render status: NOT_FOUND
            return
        }


        StatusVenda statusTransacao = pagSeguroService.getStatusTransacao(transacaoPagSeguro.status)
        /**
         * controlando o caso em que notificações concorrentes atualizam o status da Venda
         *
         * Somente prosegue com a atualização do status da venda se a notificação
         * trouxer algum status novo, mais avançado do que o atuals
         */
        if (venda.status.ordinal() > statusTransacao.ordinal() ) {
            render status: OK
            return
        }else{
            vendaLogger.debug "!! PagSeguro enviou notificação com status ${statusTransacao}, mas venda #${venda.id} ja tem status ${venda.status}"
        }

        vendaLogger.debug("*** notificacoes p/ venda ${venda.id} com status ${venda.status}")

        venda.transacaoPagSeguro = transacaoPagSeguro.code
        venda.status = statusTransacao
        venda.descontoPagSeguroEmCentavos = transacaoPagSeguro.getDiscountAmount() * 100
        venda.taxasPagSeguroEmCentavos = transacaoPagSeguro.getFeeAmount() * 100

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
                estoqueService.reporItens(venda.itensVenda)
                emailService.notificarCancelamento(venda)
                break

        }

        render status: OK
        return

    }
}
