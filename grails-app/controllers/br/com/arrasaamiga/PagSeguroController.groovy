package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import org.apache.commons.logging.LogFactory

@Secured(['permitAll'])
class PagSeguroController {

    def pagSeguroService
    def emailService

    private static final vendaLogger = LogFactory.getLog('grails.app.domain.br.com.arrasaamiga.Venda')

    def index() {

    	redirect(uri:'/')
    }

    @Secured(['isAuthenticated()'])
    def retorno(){

    	def codigoTransacao = params.transacao
        def transacaoPagSeguro = pagSeguroService.getTransaction(codigoTransacao)

        def venda = Venda.get(params.id)


        vendaLogger.debug " * retorno: venda ${venda.id}; transacaoPagSeguro ${transacaoPagSeguro} "
        vendaLogger.debug " ** retorno: status anterior ${venda.status} "

        venda.transacaoPagSeguro = codigoTransacao
        venda.status = pagSeguroService.getStatusTransacao(transacaoPagSeguro.status)
        venda.descontoPagSeguroEmCentavos = transacaoPagSeguro.getDiscountAmount() * 100
        venda.taxasPagSeguroEmCentavos = transacaoPagSeguro.getFeeAmount() * 100
        venda.save(flush:true)

        vendaLogger.debug " *** retorno: novo status ${venda.status} "



        if (venda.status != StatusVenda.Cancelada){
            vendaLogger.debug("**** retorno: venda/show/${venda.id} e apagando carrinho")

            session.shoppingCart = null
            redirect(controller:'venda',action:'show',id:venda.id)
            return

        }else{
            vendaLogger.debug("**** retorno: repondo os itens ${venda.itensVenda}")

            Estoque.reporItens(venda.itensVenda)
            redirect(controller:'venda',action:'cancelada')
            return           
        }
    }

    def notificacoes(){
        def venda = Venda.get(params.id)

        vendaLogger.debug("* notificacoes p/ venda ${venda.id} com status ${venda.status}")

        // proteção, caso essa venda ja tenha sido cancelada ñ faz mais nada
        if (venda.status?.equals(StatusVenda.Cancelada)){
            render text: 'ok'
            return 
        }


        def notificationCode = params.notificationCode
        vendaLogger.debug("** notificacoes notificationCode ${notificationCode}")

        def transaction = pagSeguroService.checkTransaction(notificationCode)
        vendaLogger.debug("*** notificacoes transaction ${transaction.code} transacaoPagSeguro ${venda.transacaoPagSeguro}")

        if (!venda.transacaoPagSeguro){
            venda.transacaoPagSeguro = transaction.code
        }

        venda.status = pagSeguroService.getStatusTransacao(transaction.status)
        vendaLogger.debug "**** notificacoes novo status ${venda.status}"
        venda.save(flush:true)


        switch(venda.status){
            case StatusVenda.PagamentoRecebido:
                emailService.notificarAdministradores(venda)
                emailService.notificarCliente(venda)
                break

            case StatusVenda.Cancelada:
                emailService.notificarAdministradores(venda)
                emailService.notificarCliente(venda)
                Estoque.reporItens(venda.itensVenda)
                break
        }

        vendaLogger.debug "Status da venda #${venda.id} alterado para ${venda.status}"

    }
}
