package br.com.arrasaamiga

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.spock.IntegrationSpec
import org.codehaus.groovy.grails.commons.InstanceFactoryBean
import spock.lang.Unroll

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull

/**
 *
 */
class FecharVendaSpec extends IntegrationSpec {


    def sessionFactory


    def doWithSpring = {
        def springSecurityServiceMock = mockFor(SpringSecurityService)
        springSecurityServiceMock.demandExplicit.getCurrentUser(1..10){-> return Usuario.load(1) }

        def emailServiceMock = mockFor(EmailService)
        emailServiceMock.demandExplicit.notificarAdministradores(1){->  }
        emailServiceMock.demandExplicit.notificarCliente(1){->  }

        springSecurityService(InstanceFactoryBean, springSecurityServiceMock.createMock(), SpringSecurityService)
        emailService(InstanceFactoryBean, emailServiceMock.createMock(), EmailService)
    }



    protected void setup() {

        Estoque.executeUpdate('delete from Estoque')
        Produto.executeUpdate('delete from Produto')
        Produto.executeUpdate('delete from Cliente')


        def cliente = new Cliente(nome:'Cliente teste').save(flush: true)
        assert 1 == Cliente.count()

        def produto1 = new Produto(descricao: 'Produto 1', nome: 'P1', tipoUnitario: 'un', unidades: ['un'] as List)
        produto1.save(flush: true)

        def produto2 = new Produto(descricao: 'Produto 2', nome: 'P2', tipoUnitario: 'un', unidades: ['un'] as List)
        produto2.save(flush: true)

        assert 2 == Produto.count()

        new Estoque(produto: produto1, unidade: 'un', quantidade: 10).save(flush: true)
        new Estoque(produto: produto2, unidade: 'un', quantidade: 5).save(flush: true)

        assert 2 == Estoque.count()
    }


    void "test fechar venda a vista "() {
        given:
            def produto = Produto.first()


        expect:
            Estoque.findByProdutoAndUnidade(produto,'un').quantidade == 10

        when:
            def controller = new ShoppingCartController()
            controller.request.method = 'POST'

            controller.params.id = produto.id
            controller.params.unidade = 'un'
            controller.params.quantidade = 3

            controller.add()

        then:
            controller.session.shoppingCart.getQuantidade(produto,'un') == 3
            controller.response.reset()


        when:
            controller.params.dataEntrega = controller.proximosDiasDeEntrega[0].time
            controller.params.formaPagamento = FormaPagamento.AVista.name()

            controller.fecharVenda()

        then:
            Estoque.findByProdutoAndUnidade(produto,'un').quantidade == 7


    }



}
