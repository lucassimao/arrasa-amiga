package br.com.arrasaamiga

import spock.lang.Specification
import static org.junit.Assert.*
import grails.test.mixin.*
import grails.test.mixin.hibernate.HibernateTestMixin
import grails.test.mixin.gorm.Domain
import spock.lang.*
import static br.com.arrasaamiga.FormaPagamento.AVista
import static br.com.arrasaamiga.FormaPagamento.PagSeguro
import static br.com.arrasaamiga.ServicoCorreio.SEDEX
import static br.com.arrasaamiga.ServicoCorreio.PAC
import grails.plugin.springsecurity.SpringSecurityService
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.codehaus.groovy.grails.commons.spring.GrailsWebApplicationContext
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration
import org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor
import org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventListener
import org.codehaus.groovy.grails.orm.hibernate.EventTriggeringInterceptor
import org.codehaus.groovy.grails.orm.hibernate.HibernateDatastore

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(HibernateTestMixin)
@Domain([Cidade,Uf,Endereco,GrupoDeUsuario,MovimentoCaixa,Usuario,Produto,UsuarioGrupoDeUsuario,
    FotoProduto,GrupoDeProduto,Venda,Cliente,ShoppingCart,ItemVenda,Produto,])
class CaixaServiceSpec extends  Specification {

    static Usuario vendedor1 = null, vendedor2 = null
    static boolean fixtureOK = false;

    private Date str2Date(String data){
        return new Date().parse("dd/MM/yyyy H:m:s", data)
    }



    // metodo para desativar o autotimestamp das classes de dominio
    private void shouldTimestamp(Object domainObjectInstance, boolean shouldTimestamp) {
        ClosureEventTriggeringInterceptor closureInterceptor = grailsApplication.mainContext.getBean('eventTriggeringInterceptor')
        HibernateDatastore datastore = closureInterceptor.datastores.values().iterator().next()
        EventTriggeringInterceptor interceptor = datastore.getEventTriggeringInterceptor()
        ClosureEventListener listener = interceptor.findEventListener(domainObjectInstance)
        listener.shouldTimestamp = shouldTimestamp
    }

    // configurando vendas entre 1/12/2015 e 31/12/2015
    void setup() {
        if (fixtureOK)
            return

        shouldTimestamp(new Venda(),false)

        // evitando que a codificação desnecessaria da senha
        Usuario.metaClass.encodePassword = {}
        vendedor1 = new Usuario(username:'vendedor1',password:'123').save(flush:true,failOnError:true)
        vendedor2 = new Usuario(username:'vendedor2',password:'123').save(flush:true,failOnError:true)

        // VALIDO: venda feita pelo site, pagamento em dinheiro a ser feita na entrega
        def v1 = new Venda(status: StatusVenda.AguardandoPagamento,
                                dataEntrega: str2Date('05/12/2015 12:0:0'),cliente: new Cliente(nome:'Cliente 1'),
                                formaPagamento: AVista,servicoCorreio:null,vendedor:null)

        v1.dateCreated = str2Date('1/12/2015 20:0:0')
        v1.save(flush:true,failOnError:true)


        // INVALIDA: Venda OK, mas deve ser entregue em data fora do intervalo do caixa
        def v1a = new Venda(status: StatusVenda.AguardandoPagamento,
                                dataEntrega: str2Date('1/1/2016 12:0:0'),cliente: new Cliente(nome:'Cliente 1'),
                                formaPagamento: AVista,servicoCorreio:null,vendedor:null)

        v1a.dateCreated = str2Date('25/12/2015 20:0:0')
        v1a.save(flush:true,failOnError:true)

        // VALIDO: venda feita pelo site, pagamento em cartao realizado com sucesso, mesma cidade da loja
        def v2 = new Venda(status: StatusVenda.PagamentoRecebido,
                            transacaoPagSeguro: 'sdhsjhfjskhfkjhsdfhkjsdh',dataEntrega: str2Date('06/12/2015 12:0:0'),
                            cliente: new Cliente(nome:'Cliente 2'),formaPagamento: PagSeguro,servicoCorreio:null,vendedor:null)
        v2.dateCreated = str2Date('1/12/2015 12:0:0')
        v2.save(flush:true,failOnError:true)

        // INVALIDO: venda feita pelo site, pagamento em cartao NÃO  concluido, mesma cidade da loja
        def v2a = new Venda(status: StatusVenda.AguardandoPagamento,
                            transacaoPagSeguro:null,dataEntrega: str2Date('08/12/2015 12:0:0'),
                            cliente: new Cliente(nome:'Cliente 2a'), formaPagamento: PagSeguro,servicoCorreio: null,vendedor:null)
        v2a.dateCreated=str2Date('4/12/2015 12:0:0')
        v2a.save(flush:true,failOnError:true)


        // INVALIDO: VENDA OK, pelo site porem fora do intervalo do caixa aberto, mesma cidade
        def v2b = new Venda(status: StatusVenda.PagamentoRecebido,
                            transacaoPagSeguro: 'sdhsjhfjskhfkjhsdfhkjsdh',dataEntrega: str2Date('06/1/2016 12:0:0'),
                            cliente: new Cliente(nome:'Cliente 2'),formaPagamento: PagSeguro,servicoCorreio:null,vendedor:null)

        v2b.dateCreated = str2Date('31/12/2015 12:0:0')
        v2b.save(flush:true,failOnError:true)

        // VALIDO: venda feita pelo site, para outra cidade, pagamento em cartao realizado com sucesso
        def v3 = new Venda(status: StatusVenda.PagamentoRecebido,
                            transacaoPagSeguro: 'sdhsjhfjskhfkjhsdfhkjsdh',dataEntrega: null,
                            cliente: new Cliente(nome:'Cliente 3'),formaPagamento: PagSeguro,
                            servicoCorreio:SEDEX,vendedor:null)

        v3.dateCreated = str2Date('05/12/2015 12:0:0')
        v3.save(flush:true,failOnError:true)

        // INVALIDO: venda feita pelo site, para outra cidade, pagamento em cartao NÃO  concluido
        def v3a = new Venda(status: StatusVenda.AguardandoPagamento,
                            transacaoPagSeguro:null, dataEntrega: null,cliente: new Cliente(nome:'Cliente 3a'),
                            formaPagamento: PagSeguro, servicoCorreio: PAC,vendedor:null)

        v3a.dateCreated = str2Date('05/12/2015 20:0:0')
        v3a.save(flush:true,failOnError:true)


        // VALIDA: venda feita por vendedor, pagamento em dinheiro no momento da entrega
        def v4 = new Venda(status: StatusVenda.AguardandoPagamento, dataEntrega: str2Date('16/12/2015 12:0:0'),
                            cliente: new Cliente(nome:'Cliente 4'), formaPagamento: AVista,vendedor:vendedor1)

        v4.dateCreated = str2Date('10/12/2015 12:0:0')
        v4.save(flush:true,failOnError:true)

        // VALIDA: venda feita por vendedor, pagamento realizado antecipadamente por transferencia
        def v4a = new Venda(status: StatusVenda.PagamentoRecebido,
                            dataEntrega: str2Date('16/12/2015 22:0:0'), cliente: new Cliente(nome:'Cliente 4a'),
                            formaPagamento: AVista,vendedor:vendedor1)

        v4a.dateCreated = str2Date('15/12/2015 12:0:0')
        v4a.save(flush:true,failOnError:true)

        // INVALIDA: venda ok, mas fora do intervalo do caixa
        def v4b = new Venda(status: StatusVenda.PagamentoRecebido,
                            dataEntrega: str2Date('16/11/2015 22:0:0'), cliente: new Cliente(nome:'Cliente 4a'),
                            formaPagamento: AVista,vendedor:vendedor1)

        v4b.dateCreated = str2Date('10/11/2015 12:0:0')
        v4b.save(flush:true,failOnError:true)

        // VALIDO: venda feita por vendedor, pagamento feito no cartao pela maquineta atraves da maquineta
        def v5 = new Venda(status: StatusVenda.PagamentoRecebido,
                           dataEntrega: str2Date('08/12/2015 12:0:0'), cliente: new Cliente(nome:'Cliente 5'),
                            formaPagamento: PagSeguro,vendedor:vendedor2)

        v5.dateCreated = str2Date('1/12/2015 12:0:0')
        v5.save(flush:true,failOnError:true)

        // INVALIDO: venda feita por vendedor, clente pretende pagar no cartao mas ainda nao informou dados do cartao
        def v5a = new Venda(status: StatusVenda.AguardandoPagamento,
                          dataEntrega: str2Date('28/12/2015 12:0:0'), cliente: new Cliente(nome:'Cliente 5a'),
                            formaPagamento: PagSeguro,vendedor:vendedor2)

        v5a.dateCreated=str2Date('20/12/2015 12:0:0')
        v5a.save(flush:true,failOnError:true)

       shouldTimestamp(new Venda(),true)
       fixtureOK = true

    }


    void "Garantindo que os horarios do inicio e fim do caixa estao corretos"() {
        given:
            def caixaService =  new CaixaService()
            def inicio = caixaService.inicioCaixaAtual
            def fim = caixaService.fimCaixaAtual
        expect:

            inicio[Calendar.HOUR_OF_DAY] == 0
            inicio[Calendar.MINUTE] == 0
            inicio[Calendar.SECOND] == 0

            fim[Calendar.HOUR_OF_DAY] == 23
            fim[Calendar.MINUTE] == 59
            fim[Calendar.SECOND] == 59

    }

    void "Testando contagem de vendas validas no caixa"() {
		    given:
            def caixaService =  new CaixaService()
            def inicio = str2Date('1/12/2015 0:0:0')
            def fim = str2Date('31/12/2015 23:59:59')
      			def vendas = caixaService.getVendas(inicio,fim,null)

        expect: "Test execute Hibernate count query"
            assert Venda.count() == 12
            assert vendas.size() == 6
    }

    @Unroll
    void "testando quantidade de vendas #formaPagamento feitas por  #vendedor"(){
        given:
            def caixaService =  new CaixaService()
            def inicio = str2Date('01/12/2015 0:0:0')
            def fim = str2Date('31/12/2015 23:59:59')
        expect:
            def vendas = caixaService.getVendas(inicio,fim,vendedor)
            vendas.findAll{venda-> venda.formaPagamento.equals(formaPagamento) }.size() == quantidade
        where:
            vendedor  | formaPagamento           | quantidade
            vendedor1 | FormaPagamento.AVista    |  2
            vendedor1 | FormaPagamento.PagSeguro |  0
            vendedor2 | FormaPagamento.AVista    |  0
            vendedor2 | FormaPagamento.PagSeguro |  1
            null      | FormaPagamento.PagSeguro |  3
            null      | FormaPagamento.AVista    |  3


    }

    void "testar calculo de bonus"(){
         given:
            def caixaService =  new CaixaService()

            def inicio = str2Date('1/12/2015 0:0:0')
            def fim = str2Date('31/12/2015 23:59:59')

            Map<Date,Long> resumo = [:]
            (inicio..fim).each{data-> resumo[data] =0 }

            // 1º semana, conseguiu apenas 2 strikes
            resumo[inicio+2] = 51000 // 03/12/2015 : R$ 510
            resumo[inicio+3] = 49000 // 04/12/2015 : R$ 490

            //2º semana, conseguiu bonus com 3 strikes
            resumo[inicio+7] = 51000 // 08/12/2015 : R$ 510
            resumo[inicio+9] = 49000  // 10/12/2015 : R$ 490 ( recebe bonus de R$ 10 do dia 08/12)
            resumo[inicio+11] = 55100 // 12/12/2015 : R$ 551

            //3º semana, conseguiu bonus com 1 strike
            resumo[inicio+17] = 150000 // 18/12/2015 : R$ 1500

            //4º semana, conseguiu bonus com 3 strikes
            resumo[inicio+21] = 55000 // 22/12/2015 : R$ 550
            resumo[inicio+23] = 50000  // 24/12/2015: R$ 500 ( recebe bonus de R$ 50 do dia 22/12, mas n precisava)
            resumo[inicio+25] = 50000 // 26/12/2015 : R$ 500  ( recebe bonus de R$ 50 do dia 24/12, mas n presicava)
            resumo[inicio+27] = 45000 // 28/12/2015 : R$ 450  ( recebe bonus de R$ 50 do dia 26/12 mas ja nao precisa pq ja bateu a meta)

            // testando limites alem do intervalo do caixa
            resumo[inicio+28] = 49900  // 29/12/2015 : R$ 499
            resumo[inicio+29] = 50000  // 30/12/2015: R$ 500
            resumo[fim] = 58000        // 31/12/2015 :  R$ 580
            resumo[fim+1] = 42000      // 1/12/2016 :  R$ 420 // excedente do dia 31/12/2015 nao vale mais

        expect:
            def list = caixaService.calcularBonus(inicio,fim,resumo)
            list.size() == 3 // nº de bonus no intervalo do caixa

            def res = list.findAll{bonus->
                    Date start = Date.parse('dd/MM/yyyy',weekStart)
                    Date end = Date.parse('dd/MM/yyyy',weekEnd)

                    return bonus.weekStart == start && bonus.weekEnd == end
            }

            if (res.size() == 1){
                def bonus = res[0]
                assert bonus.strikeDates.size() == strikeDates.size()
                assert strikeDates.collect{Date.parse('dd/MM/yyyy',it)}.containsAll(bonus.strikeDates)

            }else{
                assert res.size() == 0
            }

        where:
            weekStart   | weekEnd     | strikeDates
            '08/12/2015'| '14/12/2015'| ['08/12/2015','10/12/2015','12/12/2015']
            '15/12/2015'| '21/12/2015'| ['18/12/2015']
            '22/12/2015'| '28/12/2015'| ['22/12/2015','24/12/2015','26/12/2015']
            '29/12/2015'| '31/12/2015'| []

    }

}
