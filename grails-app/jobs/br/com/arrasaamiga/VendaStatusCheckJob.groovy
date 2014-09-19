package br.com.arrasaamiga

/**
 * de hora em hora, verifica se existe alguma compra feita cuja forma de pagamento
 * é pelo pagseguro mas que o cliente nao concluiu
 */
class VendaStatusCheckJob {

    static triggers = {
        simple repeatInterval: 60000l * 60 * 1 // 1 hora em milliseconds
    }

    def execute() {
        def vendas = Venda.findAllByFormaPagamentoAndStatus(FormaPagamento.PagSeguro,StatusVenda.AguardandoPagamento)
        vendas*.delete(flush:true)
    }
}
