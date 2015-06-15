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

        hoje = new GregorianCalendar(2015, Calendar.JUNE, 14).time // eh um domingo
        assertEquals hoje[Calendar.DAY_OF_WEEK], Calendar.SUNDAY

        mockDomain(Feriado, [[descricao: 'feriado 1', inicio: hoje + 1, fim: hoje + 1], // segunda feira
                             [descricao: 'feriado 2', inicio: hoje + 3, fim: hoje + 5]]) // quarta, quinta e sexta

        assertEquals Feriado.count(), 2
    }

    def cleanup() {
    }

    void "teste getProximosDiasDeEntrega"() {
        given:
        def expectDay1 = hoje + 2 // terça
        def expectDay2 = hoje + 8 // segunda da outra semana
        def expectDay3 = hoje + 9 // terça da outra semana

        when:
            def expectedDeliveryDates = [expectDay1, expectDay2, expectDay3]

        then:
            expectedDeliveryDates.equals( service.getProximosDiasDeEntrega() )

    }
}
