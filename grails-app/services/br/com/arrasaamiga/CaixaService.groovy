package br.com.arrasaamiga

import static java.util.Calendar.*
import br.com.arrasaamiga.caixa.Bonus


class CaixaService {

    def springSecurityService

    def getInicioCaixaAtual(){
        final int DIA_INICIAL = 4
        def inicio = new Date()
        inicio.clearTime()

        if (inicio[Calendar.DAY_OF_MONTH] < DIA_INICIAL){
            inicio[Calendar.MONTH] -=1
        }

        inicio[Calendar.DAY_OF_MONTH] = DIA_INICIAL

        return inicio
    }

    def getFimCaixaAtual(){
        def inicio = getInicioCaixaAtual()
        def fim = new Date(inicio.time)
        fim[Calendar.MONTH] +=1
        fim[Calendar.DAY_OF_MONTH] -= 1
        fim.set(hourOfDay: 23, minute: 59, second: 59)

        return fim
    }

    /**
     * @param _inicio data inicial
     * @param _fim data final
     * @param vendedor quem fez a venda. Se for null consulta independente de vendedor
     * @param formaPagamento forma de pagamento do item na venda. Caso seja nulo consulta
     *                        independente de forma de pagamento
     *
     * @return valor total das vendas em centavos
     * @author Lucas Simao
     * @since 05/01/2016
     */
    def getVendas(Date inicio, Date fim,Usuario vendedor){

        def criteria = Venda.createCriteria()

        inicio.clearTime()
        fim.set(hourOfDay: 23, minute: 59, second: 59)

        def vendas = criteria.list{

            or{
                and{
                    // vendas sem data de entrega sao feitas por clientes fora da cidade da loja
                    isNull("dataEntrega")
                    between("dateCreated", inicio, fim)
                }
                and{
                    between("dataEntrega", inicio, fim)
                }
            }

            if (vendedor){
                eq('vendedor',vendedor)
            }

            or{
                and{
                    eq('formaPagamento',FormaPagamento.PagSeguro)
                    'in'('status',[StatusVenda.PagamentoRecebido,StatusVenda.Entregue,StatusVenda.Disponivel])
                }
                and{
                    eq('formaPagamento',FormaPagamento.AVista)
                    'in'('status',[StatusVenda.AguardandoPagamento,StatusVenda.Entregue,StatusVenda.PagamentoRecebido])
                }
            }

        }

        return vendas
    }

    /* dado um mapa associando um determinado dia ao total de vendas a vista feitas
     * retorna um objeto Bonus caso a meta tenha sido batido em alguma semana
     *
     * strikes sao os dias que colaboraram para alcançar a meta.
     * O algoritmo analisa o mapa em intervalos de 7 dias. Se dentro desse 7 dias
     * ele encontrar dias em que foi vendido mais de R$ 500, e somando todos eles
     * o valor total for >= R$ 1500, entao há bonus na semana
     *
     * @param resumo map<dia,quantidade vendida no dia>
     */
    List<Bonus> calcularBonus(Date _inicio,Date _fim,Map<Date,Long> resumo){

        final long META = 150000L // R$ 1500

        def weekStart = new Date(_inicio.time)
        weekStart.clearTime()

        def fim = new Date(_fim.time)
        fim.set(hourOfDay: 23, minute: 59, second: 59)


        Date weekEnd = weekStart + 6
        List<Bonus> strikes = []

        while (weekStart <= fim) {
            long total = 0L
            long excedente = 0L
            List dias = []
            Map<Date,Long> totaisDaSemana = resumo.findAll{ it.key>= weekStart && it.key<= weekEnd }.sort()

            totaisDaSemana.each{ dia, totalAVistaEmCentavos ->

                if (total < META && (totalAVistaEmCentavos+excedente) >= 50000 ){
                    total +=  totalAVistaEmCentavos
                    dias << dia
                    excedente = (total % (50000*dias.size()))
                }
            }

            if (total >= META)
                strikes << new Bonus(weekStart: weekStart, weekEnd: weekEnd, strikeDates: dias)

            // calculando o intervalo da semana seguinte
            weekStart = weekEnd +1
            weekEnd += 7
            if (weekEnd>fim)
                weekEnd = fim
        }

        return strikes
    }



    def getResumo(Date start, Date end){

        def currentUser = springSecurityService.currentUser
        def vendedor = null
        // se o usuario atual nao for admin, consulta apenas as vendas feitas por ele
        // caso contrario consulta vendas de todos os vendedores
        if (!currentUser.isAdmin())
            vendedor = currentUser

        def vendas = getVendas(start,end,vendedor)
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
            def strikes = calcularBonus(start,end,resumoBonus[username])
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

        return map
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
