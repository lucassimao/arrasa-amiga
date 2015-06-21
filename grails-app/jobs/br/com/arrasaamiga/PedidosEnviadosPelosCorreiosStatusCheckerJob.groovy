package br.com.arrasaamiga

/**
 * Esse ob eh responsavel por checar o status de envio de todos os produtos enviados
 * atraves dos correios e enviar um relatorio diario p/ o email do administrador da loja
 * com o status dos pedidos enviados
 */
class PedidosEnviadosPelosCorreiosStatusCheckerJob {

    def emailService
    def correiosService

    static triggers = {
        simple repeatInterval: 60000l * 60 * 24 // 1 dia em milliseconds
    }


    def execute() {
        def pedidosEnviados = Venda.findAllByServicoCorreioIsNotNullAndCodigoRastreioIsNotNullAndStatus(StatusVenda.PagamentoRecebido)

        List histories = []
        pedidosEnviados.each {Venda v->
            def history = correiosService.getTranckingHistory(v.codigoRastreio)
            def trackingHistory = new TrackingHistory(venda: v, history: history)
            histories << trackingHistory
        }

        emailService.enviarRelatorioDePedidosEnviadosPorCorreios(histories)
    }
}
