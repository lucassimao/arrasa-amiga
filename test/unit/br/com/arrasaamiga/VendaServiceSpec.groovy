package br.com.arrasaamiga

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(VendaService)
@Mock([Feriado,Venda])
class VendaServiceSpec extends Specification {

    @Shared Date hoje = new Date()

    def setup() {
        hoje.clearTime()
        Feriado.list()*.delete(flush:true)
        Venda.list()*.delete(flush:true)
    }

    def cleanup() {
    }

    void "teste getProximosDiasDeEntrega"() {
        given:
            mockDomain(Feriado, [[descricao: 'feriado 1', inicio: hoje + 1, fim: hoje + 1],
                                 [descricao: 'feriado 2', inicio: hoje + 3, fim: hoje + 5]])

            assertEquals Feriado.count(), 2
            def proximosDiasDeEntrega = null
        when:
            proximosDiasDeEntrega = service.getProximosDiasDeEntrega()
        then:
            proximosDiasDeEntrega.each {Date dia->
                assertFalse dia[Calendar.DAY_OF_WEEK] in [Calendar.SATURDAY,Calendar.SUNDAY]

                Feriado.list().each{feriado->
                    assertFalse dia in (feriado.inicio..feriado.fim)
                }
            }


    }

    void "salvando venda - sem notificar por email"(){
        given:
            def venda = Mock(Venda)
            def estoqueService = Mock(EstoqueService)
            def emailService = Mock(EmailService)

            service.estoqueService = estoqueService
            service.emailService = emailService

            def itensVenda = [new ItemVenda(id: 1),new ItemVenda(id: 2)] as Set
            venda.getItensVenda() >> itensVenda
        when:
            service.salvarVenda(venda, false )
        then:
            1 * venda.save(['failOnError':true])
            1 * estoqueService.removerItens(itensVenda)
            0 * emailService.notificarAdministradores(_)
            0 * emailService.notificarCliente(_)
    }

    @Unroll
    void "salvando venda - notificando por email com forma de pagamento #formaPagamento"(){
        given:
            def venda = Mock(Venda)
            def estoqueService = Mock(EstoqueService)
            def emailService = Mock(EmailService)

            service.estoqueService = estoqueService
            service.emailService = emailService

            def itensVenda = [new ItemVenda(id: 1),new ItemVenda(id: 2)] as Set
            venda.getItensVenda() >> itensVenda
        when:
            venda.formaPagamento >> formaPagamento
            service.salvarVenda(venda, true )
        then:
            1 * venda.save(['failOnError':true])
            1 * estoqueService.removerItens(itensVenda)
            1 * emailService.notificarAdministradores(venda)

            if (formaPagamento.equals(FormaPagamento.AVista))
                1 * emailService.notificarCliente(venda)
            else
                0 * emailService.notificarCliente(venda)
        where:
            formaPagamento   << [ FormaPagamento.AVista, FormaPagamento.PagSeguro ]
    }

    @Unroll
    void "test isDataEntregaValida #data #isValid"(){
        given:
            mockDomain(Feriado, [[descricao: 'feriado 1', inicio: hoje + 2, fim: hoje + 4],
                                 [descricao: 'feriado 2', inicio: hoje + 6, fim: hoje + 7]])

            assertEquals Feriado.count(), 2
        expect:
            isValid == service.isDataEntregaValida(data)
        where:
            data    | isValid
            hoje-1  | false
            hoje-5  | false
            hoje+2  | false
            hoje+3  | false
            hoje+4  | false
            hoje+6  | false
            hoje+7  | false
    }

}
