package br.com.arrasaamiga

import br.com.arrasaamiga.excecoes.EstoqueException
import br.com.uol.pagseguro.domain.Error
import br.com.uol.pagseguro.exception.PagSeguroServiceException
import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

@Transactional
class VendaService {

    def emailService
    def pagSeguroService
    def vendaLogger = LogFactory.getLog('grails.app.domain.br.com.arrasaamiga.Venda')

    def salvarVenda(Venda venda) throws EstoqueException, PagSeguroServiceException, Exception {

        def vendaLogger = LogFactory.getLog('grails.app.domain.br.com.arrasaamiga.Venda')

        try {
            venda.save(failOnError: true)
            vendaLogger.debug("venda pagseguro #${venda.id} salva")
            emailService.notificarAdministradores(venda)

            if (venda.formaPagamento.equals(FormaPagamento.AVista)) {
                emailService.notificarCliente(venda)
            }

        } catch (PagSeguroServiceException e) {

            vendaLogger.debug "Erro ao tentar ir para o pagseguro : cliente ${venda.cliente.id} "
            Iterator itr = e.getErrorList().iterator();

            while (itr.hasNext()) {
                Error error = (Error) itr.next();
                vendaLogger.debug "Código do erro: ${error.getCode()}";
                vendaLogger.debug "Msg do erro: ${error.getMessage()}";
            }

            venda.delete(flush: true) // não deu pra mandar pro pag seguro ... exclui venda
            throw e

        } catch (EstoqueException e) {
            vendaLogger.error("Erro ao salvar venda pelo pagseguro", e)
            throw e
        } catch (Exception e) {

            vendaLogger.debug "Erro ao tentar ir para o pagseguro : venda #${venda.id} ", e
            venda.delete(flush: true) // não deu pra mandar pro pag seguro ... exclui venda
            throw e
        }

    }

    def getProximosDiasDeEntrega() {

        def hoje = new Date()
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

        diasDeEntraga.add(segunda)
        diasDeEntraga.add(quarta)
        diasDeEntraga.add(sexta)

        return diasDeEntraga.sort()
    }

    def  isDataEntregaValida(Date dataEscolhida) {
        List datasDeEntrega = getProximosDiasDeEntrega()

        def calDiaEntrega = Calendar.getInstance()
        calDiaEntrega.time = dataEscolhida


        boolean dataSelecionadaCorretamente = datasDeEntrega.any { dia ->

            def cal2 = Calendar.getInstance()
            cal2.time = dia

            boolean sameDay = calDiaEntrega.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    calDiaEntrega.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)

            return sameDay
        }
    }
}
