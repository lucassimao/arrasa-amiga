package br.com.arrasaamiga

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(VendaService)
class VendaServiceSpec extends Specification {

    Date hoje

    def setup() {

        hoje = new Date()

        mockDomain(Feriado, [[descricao: 'feriado 1', inicio: hoje + 1, fim: hoje + 1],
                             [descricao: 'feriado 2', inicio: hoje + 3, fim: hoje + 5]])

        assertEquals Feriado.count(), 2
    }

    def cleanup() {
    }

    void "teste getProximosDiasDeEntrega"() {
        given:
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
}
