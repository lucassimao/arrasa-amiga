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

    @Override
    protected List<Estoque> listAllResources(Map params){
        def c = Estoque.createCriteria()

        def results = c.list(params) {
            produto {
                eq('foraDeLinha', false)
                order('nome')
            }
        }
        return results
    }

    @Override
    public Integer countResources(){
        def c = Estoque.createCriteria()

        return c.get {
            projections {
                count('id')
            }
            produto {
                eq('foraDeLinha', false)
                order('nome')
            }
        }
    }

}
