package br.com.arrasaamiga

import static java.util.Calendar.*
import br.com.arrasaamiga.caixa.Bonus


class Caixa {


    Date inicio, fim

    static hasMany = [movimentos: MovimentoCaixa]

    static constraints = {
        inicio(nullable:false)
        fim(nullable:false)
    }

    static mapping = {}

    public void setInicio(Date date){
        this.inicio = (date != null)?date.clearTime():null
    }

    public void setFim(Date date){
        this.fim = date
        if (date)
            this.fim.set(hourOfDay: 23, minute: 59, second: 59)
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
    def getVendas(Usuario vendedor){

        def criteria = Venda.createCriteria()

        this.inicio.clearTime()
        this.fim.set(hourOfDay: 23, minute: 59, second: 59)

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
    List<Bonus> calcularBonus(Map<Date,Long> resumo){

        final long META = 150000L // R$ 1500

        def weekStart = new Date(this.inicio.time)
        def fim = new Date(this.fim.time)

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


}
