package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.NotTransactional
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static br.com.arrasaamiga.StatusVenda.*


@Secured(['ROLE_ADMIN','ROLE_VENDEDOR'])
class SyncController  {

    def index() {

        withFormat {
            json {
                def results = [:]

                def vendaLastUpdated = params.vendaLastUpdated?:0
                def dt = new Date(Long.valueOf(vendaLastUpdated))
                results['vendas'] = Venda.findAllByLastUpdatedGreaterThanAndStatusInList(dt,
                                        [PagamentoRecebido,AguardandoPagamento])

                def estoqueLastUpdated = params.estoqueLastUpdated?:0
                dt = new Date(Long.valueOf(estoqueLastUpdated))
                results['estoques'] = Estoque.findAllByLastUpdatedGreaterThan(dt)

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
