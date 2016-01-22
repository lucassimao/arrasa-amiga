package br.com.arrasaamiga

import br.com.uol.pagseguro.service.*
import br.com.uol.pagseguro.enums.*
import br.com.arrasaamiga.financeiro.TransactionSummary

class PagSeguroTransactionsSeekerJob {


    static triggers = {
        simple repeatInterval: 60000l * 60 * 24 // 1 dia em milliseconds
    }

    def pagSeguroService
    def emailService

    def execute() {

        def caixa = Caixa.last()
        def inicio = caixa.inicio
        def fim = caixa.fim
        def vendas = caixa.getVendas()

        try{
            
            def listTransactionSummaries = pagSeguroService.searchTransactions(inicio,fim)

            // br.com.uol.pagseguro.domain.TransactionSummary
            for(def transactionSummary: listTransactionSummaries){

                // ignorando transações ja identificadas pelo sistema
                if (vendas.find{v-> v.transacaoPagSeguro?.equals(transactionSummary.code)})
                    continue

                def transaction = TransactionSummary.findByCode(transactionSummary.code)
                if (transaction){

                    transaction.code = transactionSummary.code
                    transaction.status = pagSeguroService.getStatusTransacao(transactionSummary.status)

                }else{

                    transaction = new TransactionSummary()
                    transaction.code = transactionSummary.code
                    transaction.detalhes = transactionSummary.type.description
                    transaction.status = pagSeguroService.getStatusTransacao(transactionSummary.status)
                    transaction.valorBrutoEmCentavos = (transactionSummary.grossAmount * 100).longValue()
                    transaction.valorLiquidoEmCentavos = (transactionSummary.netAmount * 100).longValue()
                    transaction.descontoEmCentavos = (transactionSummary.discountAmount * 100).longValue()
                    transaction.taxaParcelamentoEmCentavos = (transactionSummary.feeAmount * 100).longValue()
                    transaction.valorExtraEmCentavos = (transactionSummary.extraAmount * 100).longValue()
                }

                transaction.save()

            }

            emailService.notificarTransacoesPagSeguroDesconhecidas()

        }catch(Exception e){
            e.printStackTrace()
        }

    }
}
