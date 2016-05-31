package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.*
import java.text.ParseException
import br.com.arrasaamiga.caixa.Bonus

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])
class CaixaController {

    static allowedMethods = [index: 'GET']
    static responseFormats = ['json']

    def caixaService

    @Secured(['ROLE_ADMIN','ROLE_VENDEDOR'])
    def index(String _inicio,String _fim) {

        if (params.lastUpdated){
            Date lastUpdated = new Date(params.lastUpdated.toLong())

            def c = Venda.createCriteria()
            int qtdeVendas = c.count {
                gt('lastUpdated',lastUpdated)
            }

            c = MovimentoCaixa.createCriteria()
            int qtdeMovimentos = c.count {
                gt('dateCreated',lastUpdated)
            }

            if (qtdeVendas == 0 && qtdeMovimentos==0){
                render status: NO_CONTENT
                return
            }
        }

        Date start = null, end = null
        try{
            start = (_inicio)? Date.parse('dd/MM/yyyy',_inicio):caixaService.getInicioCaixaAtual()
            start.clearTime()

            end = (_fim)?  Date.parse('dd/MM/yyyy',_fim) : caixaService.getFimCaixaAtual()
            end.set(hourOfDay: 23, minute: 59, second: 59)

        }catch(ParseException e){
            render(status: BAD_REQUEST, text: e.message)
            return
        }

        def map = caixaService.getResumo(start,end)
        render map as JSON
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'caixa.label', default: 'Caixa'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
