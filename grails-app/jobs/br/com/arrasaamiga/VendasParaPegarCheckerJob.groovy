package br.com.arrasaamiga

/**
 * Esse eh responsavel por checar as vendas cujos clientes ficaram
 * de buscar e ainda nao foram.
 */
class VendasParaPegarCheckerJob {

    def gcmService
    def caixaService

    static triggers = {
        simple repeatInterval: 60000l * 60 * 3 // 3 horas em milliseconds
    }

    def execute() {
        def c = Venda.createCriteria()
        def hoje = new Date()

        // se ainda nao for 8h da manha, nao cria nenhum alerta
        if( hoje[Calendar.HOUR_OF_DAY] < 8 ){
            log.debug('Ainda não são 8h. Abortando ...')
            return
        }

        int dia = hoje[Calendar.DAY_OF_MONTH]
        int mes = hoje[Calendar.MONTH] + 1 // o java atribui 0 a janeiro e 11 pra dezembro
        int ano = hoje[Calendar.YEAR]

        def vendas = c.list{
            isNotNull('vendedor')
            eq('flagClienteVaiBuscar',true)
            eq('flagClienteJaBuscou',false)
            sqlRestriction 'day(data_entrega) = ?',[dia]
            sqlRestriction 'month(data_entrega) = ?',[mes]
            sqlRestriction 'year(data_entrega) = ?',[ano]
        }
        if (vendas){
            log.debug("${vendas.size()} vendas ainda marcadas para buscar ... notificando")
            gcmService.relembrarVendedorDeClienteIrBuscarProduto(vendas)
        }else
            log.debug("Nenhuma venda marcada para buscar em ${hoje}")

    }
}
