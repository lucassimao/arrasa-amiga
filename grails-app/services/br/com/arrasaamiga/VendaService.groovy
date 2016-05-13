package br.com.arrasaamiga

import grails.transaction.NotTransactional
import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory
import static br.com.arrasaamiga.StatusVenda.PagamentoRecebido
import static br.com.arrasaamiga.StatusVenda.AguardandoPagamento

@Transactional
class VendaService {

    def emailService
    def estoqueService
    def vendaLogger = LogFactory.getLog('grails.app.domain.br.com.arrasaamiga.Venda')

    def salvarVenda(Venda venda, boolean notificarPorEmail = true) {

        venda.save(failOnError: true)
        vendaLogger.debug("venda #${venda.id} salva! removendo itens do estoque ...")
        estoqueService.removerItens(venda.itensVenda)

        if (notificarPorEmail) {
            emailService.notificarAdministradores(venda)

            if (venda.formaPagamento.equals(FormaPagamento.AVista)) {
                emailService.notificarCliente(venda)
            }
        }

    }

    private void excluirVendasNaoFinalizadas(Cliente cliente) {
        def result = Venda.findAllByClienteAndStatusAndFormaPagamento(cliente, StatusVenda.AguardandoPagamento, FormaPagamento.PagSeguro)
        result?.each { Venda venda ->
            excluirVenda(venda)
        }
    }

    def excluirVenda(Venda venda) {
        venda.delete()
        vendaLogger.debug("Excluido venda #${venda.id}:  repondo itens ")
        estoqueService.reporItens(venda.itensVenda)
    }

    @NotTransactional
    def getProximosDiasDeEntrega() {

        def hoje = new Date()
        hoje.clearTime()

        def diasDeEntraga = []

        Date segunda, quarta, sexta

        switch (hoje.toCalendar().get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                segunda = (hoje + 1)
                quarta = (hoje + 3)
                sexta = (hoje + 5)
                break
            case Calendar.MONDAY:
                segunda = (hoje + 7)
                quarta = (hoje + 2)
                sexta = (hoje + 4)

                break
            case Calendar.TUESDAY:
                segunda = (hoje + 6)
                quarta = (hoje + 1)
                sexta = (hoje + 3)
                break

            case Calendar.WEDNESDAY:
                segunda = (hoje + 5)
                quarta = (hoje + 7)
                sexta = (hoje + 2)
                break

            case Calendar.THURSDAY:
                segunda = (hoje + 4)
                quarta = (hoje + 6)
                sexta = (hoje + 1)
                break

            case Calendar.FRIDAY:
                segunda = (hoje + 3)
                quarta = (hoje + 5)
                sexta = (hoje + 7)
                break

            case Calendar.SATURDAY:
                segunda = (hoje + 2)
                quarta = (hoje + 4)
                sexta = (hoje + 5)
                break
            default:
                throw new Exception("Data não identificada! ${hoje}")

        }

        def proximosFeriados = Feriado.findAllByInicioGreaterThan(hoje)

        for (Date data : [segunda, quarta, sexta]) {

            while (proximosFeriados.any { feriado -> data in (feriado.inicio..feriado.fim) } ||
                    diasDeEntraga.contains(data) ||
                    data[Calendar.DAY_OF_WEEK] in [Calendar.SATURDAY, Calendar.SUNDAY]) {

                ++data
            }

            diasDeEntraga << data
        }

        return diasDeEntraga.sort()
    }

    @NotTransactional
    def isDataEntregaValida(Date dataEscolhida) {
        List datasDeEntrega = getProximosDiasDeEntrega()

        def calDiaEntrega = Calendar.getInstance()
        calDiaEntrega.time = dataEscolhida


        boolean dataSelecionadaCorretamente = datasDeEntrega.any { dia ->

            def cal2 = Calendar.getInstance()
            cal2.time = dia

            boolean sameDay = calDiaEntrega[Calendar.YEAR] == cal2[Calendar.YEAR] &&
                              calDiaEntrega[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]

            return sameDay
        }

        return dataSelecionadaCorretamente
    }
}
