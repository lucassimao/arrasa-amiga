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

    def springSecurityService
    def caixaService

    @Secured(['ROLE_ADMIN','ROLE_VENDEDOR'])
    def index(String _inicio,String _fim) {

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


        def currentUser = springSecurityService.currentUser
        def vendedor = null
        // se o usuario atual nao for admin, consulta apenas as vendas feitas por ele
        // caso contrario consulta vendas de todos os vendedores
        if (!currentUser.isAdmin())
            vendedor = currentUser

        def vendas = caixaService.getVendas(start,end,vendedor)
        long valorTotal=0, totalAVista=0,totalAPrazo=0,totalTaxasPagSeguro=0


        def totaisDiarios = [:] // vendedor -> [ [data ,dinheiro, cartao], ... ]
        def mapResumoVendedores = [:] // username -> ['dinheiro':0L,'cartao':0L,'total':0L,'salario':0L]
        def resumoBonus = [:] // username -> [data: valor total vendido a vista]

        //inicializando os mapas
        def vendedores = Usuario.vendedores.list()*.username + 'site'

        vendedores.each{username->
            mapResumoVendedores[username] = ['dinheiro':0L,'cartao':0L,'total':0L,'salario':0L]
            resumoBonus[username] = [:]
            totaisDiarios[username] = [:]
        }



        (start..end).each{dt->
            resumoBonus.each{username,map->
                map[dt]= 0
            }
        }

        (start..end).step(7){inicio->
            Date fim = (inicio+6 < end)?inicio+6:end
            String intervalo = inicio.format('dd/MM') + ' - ' + fim.format('dd/MM')

            totaisDiarios.each{username,map->
                map[intervalo] = ['dinheiro':0L,'cartao':0L]
            }
        }

        vendas.each{ v->

            String username = (v.vendedor)?v.vendedor.username:'site'

            def data =  (v.dataEntrega)?: v.dateCreated
            data = new Date(data.time).clearTime() // apagando informações de hora,minuto e segundos

           // se a venda for a vista, o desconto tira o acrescimo de 10%
           long valorItensEmCentavos = 0
           String intervalo = getIntervalo(start,end,data)

           switch(v.formaPagamento) {
                case FormaPagamento.AVista:
                    valorItensEmCentavos = v._getValorItensAVista()

                    mapResumoVendedores[username]['dinheiro'] += valorItensEmCentavos
                    resumoBonus[username][data] += valorItensEmCentavos
                    totaisDiarios[username][intervalo]['dinheiro'] += valorItensEmCentavos
                    break
                case FormaPagamento.PagSeguro:
                    valorItensEmCentavos = v._getValorItensAPrazo()
                    totalTaxasPagSeguro += v.taxasPagSeguroEmCentavos

                    //long taxaDosProdutos = ( valorItensEmCentavos * v.taxasPagSeguroEmCentavos)/v._getValorTotal()
                    //valorItensEmCentavos -= taxaDosProdutos

                    mapResumoVendedores[username]['cartao'] += valorItensEmCentavos
                    totaisDiarios[username][intervalo]['cartao'] += valorItensEmCentavos
                    break
            }

            mapResumoVendedores[username]['total'] += valorItensEmCentavos
            // a porcentagem do salario eh sempre calculado em cima do valor a vista
            v.itensVenda.each{itemVenda->
                def bonus = itemVenda._getSubTotalAVista()*itemVenda.produto.bonus
                mapResumoVendedores[username]['salario'] += bonus
            }
            valorTotal += valorItensEmCentavos
        }

        mapResumoVendedores.each{ username, map->
            // removendo os dias que o vendedor vendeu menos de R$ 500
            def strikes = caixaService.calcularBonus(start,end,resumoBonus[username])
            if (strikes){
                map['strikes'] = strikes
            }

            map['historico'] = totaisDiarios[username]
        }


        def map = [vendedores:[:]]

        def c = Venda.createCriteria()
        Date maxVendaLastUpdated = c.get{ projections{max 'lastUpdated'}}

        c = MovimentoCaixa.createCriteria()
        Date maxMovimentoDateCreated = c.get{ projections{max 'dateCreated'}}

        map['last_updated'] = [maxVendaLastUpdated?.time,
                                maxMovimentoDateCreated?.time].max()

        if (currentUser.isAdmin()){
            map['vendedores'] = mapResumoVendedores
            map['total'] = valorTotal
            map['totalTaxasPagSeguro'] = totalTaxasPagSeguro
            map['movimentos'] = MovimentoCaixa.findAllByDataBetween(start,end)
        }
        else if (mapResumoVendedores[currentUser.username])
            map['vendedores']["${currentUser.username}"] =  mapResumoVendedores[currentUser.username]
        else
            map['vendedores']["${currentUser.username}"]= ['dinheiro':0L,'cartao':0L,'total':0L,'salario':0L]

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

    private String getIntervalo(Date start,Date end, Date dataVenda){
        String intervalo = null

        (start..end).step(7){inicio->
            Date fim = (inicio+6 < end)?inicio+6:end

            if (dataVenda in (inicio..fim)){
                intervalo = inicio.format('dd/MM') + ' - ' + fim.format('dd/MM')
                return
            }
        }
        if (intervalo)
            return intervalo
        else
            throw new IllegalArgumentException("${dataVenda} não esta entre ${start} e ${end}")
    }

}
