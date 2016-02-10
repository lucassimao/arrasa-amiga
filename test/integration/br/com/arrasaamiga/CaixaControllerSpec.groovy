package br.com.arrasaamiga

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.web.mime.MimeType
import spock.lang.*
import static org.junit.Assert.*
import org.codehaus.groovy.grails.commons.spring.GrailsWebApplicationContext
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration
import org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor
import org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventListener
import org.codehaus.groovy.grails.orm.hibernate.EventTriggeringInterceptor
import org.codehaus.groovy.grails.orm.hibernate.HibernateDatastore
import static br.com.arrasaamiga.FormaPagamento.*
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.test.mixin.TestFor

/**
 *
 */
class CaixaControllerSpec extends Specification {


    def sessionFactory
    def grailsApplication
    def springSecurityService
    def vendaService
    def caixaService

    static Usuario vendedor1=null, vendedor2=null,admin=null
    static final double PORCENTUAL_VENDEDOR = 0.15

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

    @Unroll
    void "test calcular totais para o vendedor #username"(){
        when:
            def controller = new CaixaController()
            controller.caixaService = caixaService
            controller.springSecurityService = springSecurityService

            SpringSecurityUtils.reauthenticate username, '123'
            controller.request.method = 'GET'
            controller.index('01/12/2015','31/12/2015')

        then:
            def json = controller.response.json
            // assegurando que usuarios comuns somente tem acesso somente a suas informações
            json.site == null
            json.total == null
            json.totalTaxasPagSeguro == null
            json.vendedores != null
            json.vendedores[username] != null
            json.vendedores[username].total == total
            json.vendedores[username].dinheiro == totalAVista
            json.vendedores[username].cartao == totalAPrazo
            json.vendedores[username].salario == salario


        where:
            /* os valores negativos sao os proporcionais das taxas de parcelamento para o produto */
            username   | total           |  totalAVista | totalAPrazo   | salario
            'vendedor1'| 90000+22000     |   90000      | 22000         | 15*(90000+20000)/100
            'vendedor2'| 43395           |    0         | 43395         | (15*39450)/100

    }

    @Unroll
    void "test resultado da solicitação pelo Admin"(){
        when:
            def controller = new CaixaController()
            controller.caixaService = caixaService
            controller.springSecurityService = springSecurityService

            SpringSecurityUtils.reauthenticate 'admin', '123'
            controller.request.method = 'GET'
            controller.index('01/12/2015','31/12/2015')

        then:
            def json = controller.response.json
            // assegurando que usuarios comuns somente tem acesso somente a suas informações
            json.totalTaxasPagSeguro == 187+661+987+1561 // v2,v5,v7,v8
            json.vendedores != null
            json.vendedores[username] != null
            json.vendedores[username].total == total
            json.vendedores[username].dinheiro == totalAVista
            json.vendedores[username].cartao == totalAPrazo
            json.vendedores[username].salario == salario


        where:
            /* os valores negativos sao os proporcionais das taxas de parcelamento para o produto */
            username   | total             |  totalAVista | totalAPrazo |  salario
            'vendedor1'| 90000+22000       |   90000      | 22000       | (90000+20000)*15L/100
            'vendedor2'| 43395             |    0         | 43395       | (39450 *15L)/100
            'site'     | 40000+22000+12474 |  40000       | 22000+12474 | (40000 + 20000 + 11340 )*15L/100

    }

    void setup() {

        Estoque.executeUpdate('delete from ItemVenda')
        Estoque.executeUpdate('delete from Venda')
        Estoque.executeUpdate('delete from Estoque')
        Produto.executeUpdate('delete from Produto')
        Produto.executeUpdate('delete from Cliente')
        Produto.executeUpdate('delete from Usuario')
        Produto.executeUpdate('delete from Cidade')
        Produto.executeUpdate('delete from Uf')


        shouldTimestamp(new Venda(),false)

        def produto1 = new Produto(descricao: 'Produto 1',nome: 'P1',precoAVistaEmCentavos:10000,bonus:0.15,precoAPrazoEmCentavos:11000,
                                    tipoUnitario: 'un1', unidades: ['un'] as List).save(flush: true,failOnError:true)

        def produto2 = new Produto(descricao: 'Produto 2',nome: 'P2', precoAVistaEmCentavos:20000,bonus:0.15,precoAPrazoEmCentavos:22000,
                                    tipoUnitario: 'un', unidades: ['un2'] as List).save(flush: true,failOnError:true)

        def produto3 = new Produto(descricao: 'Produto 3',nome: 'P3', precoAVistaEmCentavos:1890,bonus:0.15,precoAPrazoEmCentavos:2079,
                                    tipoUnitario: 'un', unidades: ['un3'] as List).save(flush: true,failOnError:true)


        assert 3 == Produto.count()

        new Estoque(produto: produto1, unidade: 'un1', quantidade: 10).save(flush: true,failOnError:true)
        new Estoque(produto: produto2, unidade: 'un2', quantidade: 5).save(flush: true,failOnError:true)
        new Estoque(produto: produto3, unidade: 'un3', quantidade: 50).save(flush: true,failOnError:true)

        assert 3 == Estoque.count()



        def grupoVendedor = GrupoDeUsuario.findByAuthority('ROLE_VENDEDOR')
        def grupoAdmin = GrupoDeUsuario.findByAuthority('ROLE_ADMIN')

        vendedor1 = new Usuario(username:'vendedor1',password:'123',enabled:true,accountExpired:false,accountLocked:false,
            passwordExpired:false).save(failOnError:true)
        vendedor2 = new Usuario(username:'vendedor2',password:'123',enabled:true,accountExpired:false,accountLocked:false,
            passwordExpired:false).save(failOnError:true)
        admin = new Usuario(username:'admin',password:'123').save(flush:true,failOnError:true)

        assert 3 == Usuario.count()

        UsuarioGrupoDeUsuario.create vendedor1,grupoVendedor,true
        UsuarioGrupoDeUsuario.create vendedor2,grupoVendedor,true
        UsuarioGrupoDeUsuario.create admin,grupoAdmin,true

        new Uf(nome: 'Piaui', sigla: 'PI').save(flush: true)
        new Cidade(nome: 'Teresina', uf: Uf.piaui).save(flush: true)
        def saoRaimundo = new Cidade(nome: 'São Raimundo Nonato', uf: Uf.piaui).save(flush: true)


        // 1ª venda: feita pelo site, pagamento em dinheiro a ser feita na entrega
        def v1 = new Venda(status: StatusVenda.AguardandoPagamento,
                                dataEntrega: str2Date('05/12/2015 12:0:0'),freteEmCentavos:200,
                                cliente: new Cliente(nome:'Cliente 1',
                                endereco: new Endereco(cidade: Cidade.teresina, uf: Uf.piaui)),
                                formaPagamento: AVista,servicoCorreio:null,vendedor:null)

        v1.carrinho = new ShoppingCart()
        v1.carrinho.add(produto1,'un1',2)
        v1.carrinho.add(produto2,'un2',1)
        v1.dateCreated = str2Date('04/12/2015 12:0:0')

        vendaService.salvarVenda(v1,false)

        assert 8 == Estoque.findByProdutoAndUnidade(produto1,'un1').quantidade
        assert 4 == Estoque.findByProdutoAndUnidade(produto2,'un2').quantidade

        // 2ª venda: feita pelo site, pagamento em cartao realizado com sucesso, mesma cidade da loja
        def v2 = new Venda(status: StatusVenda.PagamentoRecebido, taxasPagSeguroEmCentavos:187,freteEmCentavos:200,
                            transacaoPagSeguro: 'sdhsjhfjskhfkjhsdfhkjsdh',dataEntrega: str2Date('10/12/2015 12:0:0'),
                            cliente: new Cliente(nome:'Cliente 2',endereco: new Endereco(cidade: Cidade.teresina, uf: Uf.piaui)),
                            formaPagamento: PagSeguro,servicoCorreio:null,vendedor:null)

        v2.dateCreated = str2Date('8/12/2015 12:0:0')
        v2.carrinho = new ShoppingCart()
        v2.carrinho.add(produto1,'un1',2)

        vendaService.salvarVenda(v2,false)
        assert 6 == Estoque.findByProdutoAndUnidade(produto1,'un1').quantidade
        assert 4 == Estoque.findByProdutoAndUnidade(produto2,'un2').quantidade

        // 3ª VENDA: feita por vendedor, pagamento em dinheiro no momento da entrega
        def v3 = new Venda(status: StatusVenda.AguardandoPagamento,
                            dataEntrega: str2Date('18/12/2015 12:0:0'), freteEmCentavos:200,
                            cliente: new Cliente(nome:'Cliente 3',endereco: new Endereco(cidade: Cidade.teresina, uf: Uf.piaui)),
                            formaPagamento: AVista,vendedor:vendedor1)
        v3.dateCreated =str2Date('14/12/2015 20:0:0')
        v3.carrinho = new ShoppingCart()
        v3.carrinho.add(produto1,'un1',3)
        v3.carrinho.add(produto2,'un2',2)

        vendaService.salvarVenda(v3,false)
        assert 3 == Estoque.findByProdutoAndUnidade(produto1,'un1').quantidade
        assert 2 == Estoque.findByProdutoAndUnidade(produto2,'un2').quantidade

        // 4ª VENDA: feita por vendedor, pagamento realizado antecipadamente por transferencia
        def v4 = new Venda(status: StatusVenda.PagamentoRecebido,
                            dataEntrega: str2Date('16/12/2015 22:0:0'), freteEmCentavos:200,
                            cliente: new Cliente(nome:'Cliente 4a',endereco: new Endereco(cidade: Cidade.teresina, uf: Uf.piaui)),
                            formaPagamento: AVista,vendedor:vendedor1)

        v4.dateCreated = str2Date('10/12/2015 20:0:0')
        v4.carrinho = new ShoppingCart()
        v4.carrinho.add(produto2,'un2',1)

        vendaService.salvarVenda(v4,false)
        assert 3 == Estoque.findByProdutoAndUnidade(produto1,'un1').quantidade
        assert 1 == Estoque.findByProdutoAndUnidade(produto2,'un2').quantidade

        // 5ª VENDA: feita por vendedor, pagamento feito no cartao pela maquineta atraves da maquineta
        def v5 = new Venda(status: StatusVenda.PagamentoRecebido, taxasPagSeguroEmCentavos:661,freteEmCentavos:200,
                           dataEntrega: str2Date('10/12/2015 12:0:0'), formaPagamento: PagSeguro,vendedor:vendedor1,
                           cliente: new Cliente(nome:'Cliente 5',endereco: new Endereco(cidade: Cidade.teresina, uf: Uf.piaui)))

        v5.dateCreated = str2Date('5/12/2015 20:0:0')
        v5.carrinho = new ShoppingCart()
        v5.carrinho.add(produto2,'un2',1)

        vendaService.salvarVenda(v5,false)
        assert 3 == Estoque.findByProdutoAndUnidade(produto1,'un1').quantidade
        assert 0 == Estoque.findByProdutoAndUnidade(produto2,'un2').quantidade


        // 6ª VENDA: feita por vendedor, cliente quer passar no cartao mas ainda entregou
        // os dados do cartao e portando a venda nao esta confirmada. Nao deve contar no caixa
        def v6 = new Venda(status: StatusVenda.AguardandoPagamento, taxasPagSeguroEmCentavos:0,freteEmCentavos:200,
                           dataEntrega: str2Date('20/12/2015 12:0:0'), formaPagamento: PagSeguro,vendedor:vendedor2,
                           cliente: new Cliente(nome:'Cliente 6',endereco: new Endereco(cidade: Cidade.teresina, uf: Uf.piaui)))

        v6.dateCreated = str2Date('5/12/2015 20:0:0')
        v6.carrinho = new ShoppingCart()
        v6.carrinho.add(produto3,'un3',9)

        vendaService.salvarVenda(v6,false)
        assert 3 == Estoque.findByProdutoAndUnidade(produto1,'un1').quantidade
        assert 0 == Estoque.findByProdutoAndUnidade(produto2,'un2').quantidade
        assert 41 == Estoque.findByProdutoAndUnidade(produto3,'un3').quantidade


        // 7ª VENDA: feita por vendedor, pagamento feito no cartao pela maquineta atraves da maquineta
        def v7 = new Venda(status: StatusVenda.PagamentoRecebido, taxasPagSeguroEmCentavos:987,freteEmCentavos:200,
                           dataEntrega: str2Date('20/12/2015 12:0:0'), formaPagamento: PagSeguro,vendedor:vendedor2,
                           cliente: new Cliente(nome:'Cliente 7',endereco: new Endereco(cidade: Cidade.teresina, uf: Uf.piaui)))

        v7.dateCreated = str2Date('5/12/2015 20:0:0')
        v7.carrinho = new ShoppingCart()
        v7.carrinho.add(produto3,'un3',5)
        v7.carrinho.add(produto1,'un1',3)

        vendaService.salvarVenda(v7,false)
        assert 0 == Estoque.findByProdutoAndUnidade(produto1,'un1').quantidade
        assert 0 == Estoque.findByProdutoAndUnidade(produto2,'un2').quantidade
        assert 36 == Estoque.findByProdutoAndUnidade(produto3,'un3').quantidade



        // 8ª VENDA: feita sem vendedor, parcelamento feito pelo site
        def v8 = new Venda(status: StatusVenda.PagamentoRecebido, taxasPagSeguroEmCentavos:1561,freteEmCentavos:1830,
                           formaPagamento: PagSeguro,carrinho:new ShoppingCart(),
                           cliente: new Cliente(nome:'Cliente 8',endereco: new Endereco(cidade: saoRaimundo, uf: Uf.piaui)))

        v8.dateCreated = str2Date('30/12/2015 20:0:0')
        v8.carrinho.add(produto3,'un3',6)

        vendaService.salvarVenda(v8,false)
        assert 0 == Estoque.findByProdutoAndUnidade(produto1,'un1').quantidade
        assert 0 == Estoque.findByProdutoAndUnidade(produto2,'un2').quantidade
        assert 30 == Estoque.findByProdutoAndUnidade(produto3,'un3').quantidade


        shouldTimestamp(new Venda(),true)

    }

}
