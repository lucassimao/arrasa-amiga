package br.com.arrasaamiga

import grails.converters.JSON
import org.codehaus.groovy.grails.web.mime.MimeType

import javax.servlet.http.HttpServletResponse

/**
 *
 */

class VendaControllerTests extends GroovyTestCase {

    def sessionFactory


    protected void setUp() {

        Estoque.executeUpdate('delete Estoque')
        Estoque.executeUpdate('delete Produto')

        def produto1 = new Produto(descricao: 'Produto 1', nome: 'P1', tipoUnitario: 'un', unidades: ['un'] as List)
        produto1.save(flush: true)

        def produto2 = new Produto(descricao: 'Produto 2', nome: 'P2', tipoUnitario: 'un', unidades: ['un'] as List)
        produto2.save(flush: true)

        assertEquals 2, Produto.count()

        def e1 = new Estoque(produto: produto1, unidade: 'un', quantidade: 10)
        e1.save(flush: true)

        def e2 = new Estoque(produto: produto2, unidade: 'un', quantidade: 5)
        e2.save(flush: true)

        assertEquals 2, Estoque.count()

        new Uf(nome: 'Piaui', sigla: 'PI').save(flush: true)
        new Cidade(nome: 'Teresina', uf: Uf.piaui).save(flush: true)

        assertNotNull Uf.piaui
        assertNotNull Cidade.teresina

    }

    void testSalvarVendaEmFormatoJSON() {

        def produto1 = Produto.findByNome('P1')
        assertEquals 10, Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade

        def produto2 = Produto.findByNome('P2')
        assertEquals 5, Estoque.findByProdutoAndUnidade(produto2, 'un').quantidade

        Date amanha = new Date() + 1

        def controller = new VendaController()

        controller.request.method = 'POST'
        controller.request.contentType = MimeType.JSON
        //controller.response.format = 'json'
        def carrinho = [itens: [[produto: [id: produto1.id], unidade: 'un', quantidade: 2],
                                [produto: [id: produto2.id], unidade: 'un', quantidade: 4]]]

        controller.request.json = [formaPagamento: 'AVista', status: 'AguardandoPagamento', carrinho: carrinho,
                                   dataEntrega   : amanha.format("yyyy-MM-dd'T'hh:mm:ss'Z'"), cliente: [nome: 'Cliente Teste']] as JSON
        controller.save()

        sessionFactory.currentSession.flush()
        sessionFactory.currentSession.clear()

        assertEquals controller.response.contentAsString, HttpServletResponse.SC_CREATED, controller.response.status
        assertEquals 1, Venda.count()
        assertEquals 2, Venda.first().itensVenda.size()
        assertEquals formatarData(amanha), formatarData(Venda.first().dataEntrega)
        assertEquals 8, Estoque.findByProdutoAndUnidade(produto1, 'un').quantidade
        assertEquals 1, Estoque.findByProdutoAndUnidade(produto2, 'un').quantidade

    }

    void testAtualizarDataDeEntregaEmFormatoJSON() {
        def produto1 = Produto.findByNome('P1')
        def produto2 = Produto.findByNome('P2')
        def amanha = new Date() + 1

        def venda = new Venda(formaPagamento: FormaPagamento.AVista, status: StatusVenda.AguardandoPagamento, carrinho: new ShoppingCart(), dataEntrega: amanha)
        venda.carrinho.addToItens(new ItemVenda(produto: produto1, unidade: 'un', quantidade: 3))
        venda.carrinho.addToItens(new ItemVenda(produto: produto2, unidade: 'un', quantidade: 1))
        venda.cliente = new Cliente(nome: 'Cliente xpto')
        venda.save(flush: true)

        assertEquals 1, Venda.count()
        assertEquals formatarData(amanha), formatarData(Venda.first().dataEntrega)


        def controller = new VendaController()
        Date proximaSemana = new Date() + 7

        // atulaizando a da ata de entrega
        controller.request.method = 'PUT'
        controller.request.contentType = MimeType.JSON
        controller.params.id = Venda.first().id
        controller.request.json = [dataEntrega: formatarData(proximaSemana)] as JSON
        controller.update()

        sessionFactory.currentSession.flush()
        sessionFactory.currentSession.clear()

        assertEquals controller.response.contentAsString, HttpServletResponse.SC_OK, controller.response.status
        assertEquals 1, Venda.count()
        assertEquals formatarData(proximaSemana), formatarData(Venda.first().dataEntrega)

    }

    void testAtualizarClienteEmFormatoJSON() {
        def produto1 = Produto.findByNome('P1')
        def produto2 = Produto.findByNome('P2')

        def venda = new Venda(formaPagamento: FormaPagamento.AVista, status: StatusVenda.AguardandoPagamento, carrinho: new ShoppingCart())
        venda.carrinho.addToItens(new ItemVenda(produto: produto1, unidade: 'un', quantidade: 3))
        venda.carrinho.addToItens(new ItemVenda(produto: produto2, unidade: 'un', quantidade: 1))
        venda.cliente = new Cliente(nome: 'Cliente xpto')
        venda.save(flush: true)

        assertEquals 1, Venda.count()


        def controller = new VendaController()
        def novoNome = 'Novo nome p/ o cliente'
        String dddTelefone = '86', telefone = '32206522',
               celular = '88353101', dddCelular = '99', cep = '64023620', complemento = 'complemento xpto'

        // atulaizando o nome do cliente
        controller.request.method = 'PUT'
        controller.request.contentType = MimeType.JSON
        controller.params.id = Venda.first().id
        controller.request.json = [cliente: [nome    : novoNome, dddCelular: dddCelular, celular: celular,
                                             telefone: telefone, dddTelefone: dddTelefone,
                                             endereco: [cep: cep, complemento: complemento, cidade: [Cidade.teresina.id], uf: [Uf.piaui.id]]]] as JSON
        controller.update()

        sessionFactory.currentSession.flush()
        sessionFactory.currentSession.clear()

        assertEquals controller.response.contentAsString, HttpServletResponse.SC_OK, controller.response.status
        assertEquals 1, Venda.count()
        assertEquals novoNome, Venda.first().cliente.nome
        assertEquals dddTelefone, Venda.first().cliente.dddTelefone
        assertEquals telefone, Venda.first().cliente.telefone
        assertEquals celular, Venda.first().cliente.celular
        assertEquals dddCelular, Venda.first().cliente.dddCelular
        assertEquals cep, Venda.first().cliente.endereco.cep
        assertEquals complemento, Venda.first().cliente.endereco.complemento

    }


    void testMarcarVendaComoEntregue() {
        def produto1 = Produto.findByNome('P1')
        def produto2 = Produto.findByNome('P2')
        def amanha = new Date() + 1

        def venda = new Venda(formaPagamento: FormaPagamento.AVista, status: StatusVenda.AguardandoPagamento, carrinho: new ShoppingCart(), dataEntrega: amanha)
        venda.carrinho.addToItens(new ItemVenda(produto: produto1, unidade: 'un', quantidade: 3))
        venda.carrinho.addToItens(new ItemVenda(produto: produto2, unidade: 'un', quantidade: 1))
        venda.cliente = new Cliente(nome: 'Cliente xpto')
        venda.save(flush: true)

        assertEquals 1, Venda.count()
        assertEquals StatusVenda.AguardandoPagamento, Venda.first().status


        def controller = new VendaController()

        // atualizando o nome do cliente
        controller.request.method = 'POST'
        controller.params.id = Venda.first().id
        controller.marcarEntregue()

        sessionFactory.currentSession.flush()
        sessionFactory.currentSession.clear()

        assertEquals 1, Venda.count()
        assertEquals StatusVenda.Entregue, Venda.first().status


    }


    protected String formatarData(Date data) {
        data.format("yyyy-MM-dd'T'hh:mm:ss'Z'")
    }
}
