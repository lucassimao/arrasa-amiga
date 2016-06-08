package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.NotTransactional
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static br.com.arrasaamiga.StatusVenda.*


@Secured(['ROLE_ADMIN','ROLE_VENDEDOR'])
class SyncController  {

    def caixaService

    def index() {

        withFormat {
            json {
                def results = [:]

                if (params.vendasLastUpdated){
                    def start = caixaService.inicioCaixaAtual
                    def end = caixaService.fimCaixaAtual

                    results['caixa'] = caixaService.getResumo(start,end)

                    def dt = new Date(Long.valueOf(params.vendasLastUpdated))
                    def criteria = Venda.createCriteria()
                    
                    results['vendas'] = criteria.list{
                        ge('lastUpdated',dt)
                        'in'('status',[PagamentoRecebido,AguardandoPagamento])
                    }
                }

                if (params.estoquesLastUpdated){
                    def dt = new Date(Long.valueOf(params.estoquesLastUpdated))
                    results['estoques'] = Estoque.findAllByLastUpdatedGreaterThanEquals(dt)
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
