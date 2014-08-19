package br.com.arrasaamiga

import br.com.uol.pagseguro.domain.AccountCredentials
import br.com.uol.pagseguro.domain.Transaction
import br.com.uol.pagseguro.service.NotificationService
import br.com.uol.pagseguro.exception.PagSeguroServiceException
import br.com.uol.pagseguro.service.TransactionSearchService  
import grails.plugin.springsecurity.annotation.Secured


@Secured(['permitAll'])
class PagSeguroController {

    def pagSeguroService
    def emailService

    def index() {

    	redirect(uri:'/')
    }

    @Secured(['isAuthenticated()'])
    def retorno(){

    	def codigoTransacao = params.transacao
        def transacaoPagSeguro = pagSeguroService.getTransaction(codigoTransacao)

        def venda = Venda.get(params.id)

        venda.transacaoPagSeguro = codigoTransacao
        venda.status = StatusVenda.fromPagSeguroTransactionStatus(transacaoPagSeguro.status)
        venda.descontoPagSeguroEmCentavos = transacaoPagSeguro.getDiscountAmount() * 100
        venda.taxasPagSeguroEmCentavos = transacaoPagSeguro.getFeeAmount() * 100
        venda.save(flush:true)



        if (venda.status != StatusVenda.Cancelada){

            session.shoppingCart = null
            redirect(controller:'venda',action:'show',id:venda.id)
            return

        }else{

            Estoque.reporItens(venda.itensVenda)
            redirect(controller:'venda',action:'cancelada')
            return           
        }
    }

    def notificacoes(){
        def venda = Venda.get(params.id)

        // proteção, caso essa venda ja tenha sido cancelada ñ faz mais nada
        if (venda.status?.equals(StatusVenda.Cancelada)){ 
            return 
        }


        def notificationCode = params.notificationCode
        
        def transaction = pagSeguroService.checkTransaction(notificationCode)

        if (!venda.transacaoPagSeguro){
            venda.transacaoPagSeguro = transaction.code
        }

        venda.status = StatusVenda.fromPagSeguroTransactionStatus(transaction.status)
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

            default:
                println "Status da venda #${venda.id} alterado para ${venda.status}"
        }

    }
}
