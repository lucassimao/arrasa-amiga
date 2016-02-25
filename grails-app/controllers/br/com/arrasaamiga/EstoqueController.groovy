package br.com.arrasaamiga

import grails.converters.JSON
import static org.springframework.http.HttpStatus.NO_CONTENT
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController


@Secured(['ROLE_ADMIN','ROLE_VENDEDOR'])
class EstoqueController extends RestfulController {

    static responseFormats = ['html', 'json']


    EstoqueController() {
        super(Estoque)
    }

    /**
     * So é acessado se tiver o token de autenticação incluido no request
     *
     * @return
     */
    def index(int max) {

        withFormat {
            json {
                def c = Estoque.createCriteria()

                Date lastUpdated = null
                if (params.lastUpdated)
                    lastUpdated =  new Date(params.lastUpdated.toLong())

                if (lastUpdated == null || Estoque.countByLastUpdatedGreaterThan(lastUpdated) > 0 ){
                    def results = c.list() {
                        produto {
                            eq('foraDeLinha', false)
                            order('nome')
                        }
                    }
                    respond results
                }else{
                    render status:NO_CONTENT
                }
            }
            '*' {
                params.max = Math.min(max ?: 10, 100)
                def c = Estoque.createCriteria()

                def results = c.list(params) {
                    produto {
                        eq('foraDeLinha', false)
                        order('nome')
                    }
                }
                respond results, model: [estoqueInstanceTotal: Estoque.count()]
            }
        }

    }
}
