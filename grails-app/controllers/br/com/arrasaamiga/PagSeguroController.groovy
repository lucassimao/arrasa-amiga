package br.com.arrasaamiga

import br.com.uol.pagseguro.domain.AccountCredentials
import br.com.uol.pagseguro.domain.Transaction
import br.com.uol.pagseguro.exception.PagSeguroServiceException
import br.com.uol.pagseguro.service.TransactionSearchService  

import grails.plugins.springsecurity.Secured


class PagSeguroController {

    def pagSeguroService

    def index() {

    	redirect(uri:'/')
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def retorno(){

    	def codigoTransacao = params.transacao
        def transacaoPagSeguro = pagSeguroService.getTransaction(codigoTransacao)

        def venda = Venda.get(params.id)

        venda.transacaoPagSeguro = codigoTransacao
        venda.status = StatusVenda.fromPagSeguroTransactionStatus(transacaoPagSeguro.status)
        venda.descontoPagSeguroEmCentavos = transacaoPagSeguro.getDiscountAmount() * 100
        venda.taxasPagSeguroEmCentavos = transacaoPagSeguro.getFeeAmount() * 100
        venda.save()



        if (venda.status != StatusVenda.Cancelada){

            if (!venda.carrinho.checkedOut){
                
                venda.carrinho.checkedOut = true
                venda.carrinho.save()
            
            }

            redirect(controller:'venda',action:'show',id:venda.id)
            return

        }else{
             redirect(controller:'venda',action:'cancelada')
            return           
        }


    }
}
