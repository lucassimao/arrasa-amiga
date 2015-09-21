package br.com.arrasaamiga

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController


@Secured(['ROLE_ADMIN'])
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

                def results = c.list() {
                    produto {
                        eq('foraDeLinha', false)
                        order('nome')
                    }
                }
                respond results
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