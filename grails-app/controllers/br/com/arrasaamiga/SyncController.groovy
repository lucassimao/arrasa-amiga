package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.NotTransactional
import static org.springframework.http.HttpStatus.OK
import static br.com.arrasaamiga.StatusVenda.*


@Secured(['ROLE_ADMIN','ROLE_VENDEDOR'])
@NotTransactional
class SyncController  {

    def index() {

        withFormat {
            json {
                def results = [:]

                def criteria = Venda.createCriteria()
                results['vendas'] = criteria.list{
                    'in'('status',[PagamentoRecebido,AguardandoPagamento])
                    if (params.vendaLastUpdated)
                        gt('lastUpdated',new Date(params.vendaLastUpdated.toLong()))
                }

                criteria = Estoque.createCriteria()
                results['estoques'] = criteria.list{
                    if (params.estoqueLastUpdated)
                        gt('lastUpdated',new Date(params.estoqueLastUpdated.toLong()))
                }

                if (results['vendas'] || results['estoques'] )
                    respond results
                else
                    render status:NO_CONTENT

            }
            '*' {
                render status:NO_CONTENT
            }
        }
    }

}
