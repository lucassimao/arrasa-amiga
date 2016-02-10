package br.com.arrasaamiga.financeiro


import br.com.arrasaamiga.*
import grails.test.mixin.*
import spock.lang.*

@TestFor(TransactionSummaryController)
@Mock([TransactionSummary,Venda])
class TransactionSummaryControllerSpec extends Specification {

    static Venda v1, v2
    def caixaService

    def populateValidParams(params) {
        assert params != null
        params["code"] = '4543895843958904385094850984354'
        params["valorBrutoEmCentavos"] = 9900
        params["valorLiquidoEmCentavos"] = 9000
        params["descontoEmCentavos"] = 0
        params["taxaParcelamentoEmCentavos"] = 900
        params["status"] = StatusVenda.PagamentoRecebido
        params["detalhes"] = 'Porra nenhuma'

    }

    void setup(){

        caixaService = Mock(CaixaService)
        caixaService.getInicioCaixaAtual() >> Date.parse('dd/MM/yyyy','01/12/2015')
        caixaService.getFimCaixaAtual() >> Date.parse('dd/MM/yyyy','31/12/2015')

        controller.caixaService = caixaService


        v1 = new Venda(dataEntrega: new Date().parse('dd/MM/yyyy','10/12/2015'),
                transacaoPagSeguro:null,
                 formaPagamento:FormaPagamento.PagSeguro,status:StatusVenda.PagamentoRecebido,
                 cliente:new Cliente(nome:'Cliente 1')).save(flush:true,failOnError:true)


        v2 = new Venda(dataEntrega: new Date().parse('dd/MM/yyyy','12/12/2015'),
                transacaoPagSeguro:null,
                 formaPagamento:FormaPagamento.PagSeguro,status:StatusVenda.PagamentoRecebido,
                 cliente:new Cliente(nome:'Cliente 3')).save(flush:true,failOnError:true)

        // não conta: é a vista
        new Venda(dataEntrega: new Date().parse('dd/MM/yyyy','11/12/2015'),
                transacaoPagSeguro:null,
                 formaPagamento:FormaPagamento.AVista,status:StatusVenda.Entregue,
                 cliente:new Cliente(nome:'Cliente 2')).save(flush:true,failOnError:true)

        // não conta, ja possui transacao
        new Venda(dataEntrega: new Date().parse('dd/MM/yyyy','10/12/2015'),
                transacaoPagSeguro:'dfjkldjfkldjfkldsjks',
                 formaPagamento:FormaPagamento.PagSeguro,status:StatusVenda.Entregue,
                 cliente:new Cliente(nome:'Cliente 4')).save(flush:true,failOnError:true)

        // não conta, ainda nao passou cartao
        new Venda(dataEntrega: new Date().parse('dd/MM/yyyy','10/12/2015'),
                transacaoPagSeguro:null,
                 formaPagamento:FormaPagamento.PagSeguro,status:StatusVenda.AguardandoPagamento,
                 cliente:new Cliente(nome:'Cliente 41')).save(flush:true,failOnError:true)

    }

    void "Test the index action returns the correct model"() {
        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.transactionSummaryInstanceList
            model.transactionSummaryInstanceCount == 0
    }


    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def transactionSummary = new TransactionSummary(params)
            controller.show(transactionSummary)

        then:"A model is populated containing the domain instance"
            model.transactionSummaryInstance == transactionSummary
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def transactionSummary = new TransactionSummary(params)
            controller.edit(transactionSummary)

        then:"A model is populated containing the domain instance"
            model.transactionSummaryInstance == transactionSummary
            model.vendasSemTransacoes != null
            model.vendasSemTransacoes.size() == 2
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null,null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/transactionSummary/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action sem informar a venda"
            response.reset()
            def transactionSummary = new TransactionSummary()
            transactionSummary.validate()
            def mock = Mock(PagSeguroService)
            mock.getDetalhesPagamento(_) >> 'Detalhes VIDA LOKA'

            controller.pagSeguroService = mock
            controller.update(transactionSummary,null)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.transactionSummaryInstance == transactionSummary


        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            transactionSummary = new TransactionSummary(params).save(flush: true)
        then:
            v1.transacaoPagSeguro == null
            v1.detalhesPagamento != 'Detalhes VIDA LOKA'
            v1.descontoPagSeguroEmCentavos == 0L
            v1.taxasPagSeguroEmCentavos == 0L

        when:
            controller.update(transactionSummary, v1)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/transactionSummary/index"
            v1.transacaoPagSeguro == transactionSummary.code
            v1.detalhesPagamento == 'Detalhes VIDA LOKA'
            v1.status == transactionSummary.status
            v1.descontoPagSeguroEmCentavos == transactionSummary.descontoEmCentavos
            v1.taxasPagSeguroEmCentavos == transactionSummary.taxaParcelamentoEmCentavos
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/transactionSummary/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def transactionSummary = new TransactionSummary(params).save(flush: true)

        then:"It exists"
            TransactionSummary.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(transactionSummary)

        then:"The instance is deleted"
            TransactionSummary.count() == 0
            response.redirectedUrl == '/transactionSummary/index'
            flash.message != null
    }
}
