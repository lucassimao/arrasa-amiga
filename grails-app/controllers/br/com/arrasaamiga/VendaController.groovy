package br.com.arrasaamiga

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.*

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Secured(['permitAll'])
class VendaController extends RestfulController {

    static responseFormats = ['html', 'json']


    VendaController() {
        super(Venda)
    }


    @Secured(['isAuthenticated()'])
    def show(Long id) {

        def venda = Venda.get(id)

        withFormat {
            html {
                if (venda) {
                    [numeroPedido   : String.format("%05d", venda.id), venda: venda,
                     enderecoEntrega: venda.cliente.endereco, cliente: venda.cliente]

                } else {
                    println "Erro ao carregar venda ${id}"
                    render 'Venda não encontrada'
                }
            }
            json {
                if (venda)
                    respond venda
                else
                    render status: NOT_FOUND
            }
        }

    }

    /*
     *   TODO verificar se a nova versão corrigiu o bug do metodo update
    */

    def update() {

        def venda = Venda.get(params.id)
        venda.properties = request

        if (venda.hasErrors()) {
            println venda.errors
            respond venda.errors

        } else {
            venda.save(flush: true)
            render status: OK

        }

    }

    def save() {
        request.withFormat {

            json {
                def json = request.JSON

                def venda = new Venda()
                venda.dataEntrega = new Date(json.dataEntrega)
                venda.cliente = new Cliente(json.cliente)
                venda.cliente.endereco.cidade = Cidade.teresina
                venda.cliente.endereco.uf = Uf.piaui
                venda.vendedor = Usuario.findByUsername(json.vendedor)
                venda.formaPagamento = FormaPagamento.valueOf(json.formaPagamento)

                if (venda.formaPagamento == FormaPagamento.JaPagou)
                    venda.status = StatusVenda.PagamentoRecebido
                else
                    venda.status = StatusVenda.AguardandoPagamento


                JSON.parse(json.itens).each { obj ->
                    def estoque = Estoque.get(obj.estoqueId)
                    addToShoppingCart(estoque.produto, estoque.unidade, obj.quantidade)
                }

                venda.carrinho = getShoppingCart()
                venda.carrinho.checkedOut = true

                venda.save(flush: true)
                render status: OK
            }
        }

    }


    @Secured(['IS_AUTHENTICATED_FULLY'])
    def setTrackingCode(Long id, String trackingCode) {

        if (trackingCode) {
            def v = Venda.load(id)
            v.codigoRastreio = trackingCode
            v.save(flush: true)
            flash.message = 'Código de rastreio atualizado com sucesso'
        }

        redirect(action: 'list', params: [offset: params.offset, max: params.max])

    }


    @Secured(['IS_AUTHENTICATED_FULLY'])
    def showFull(Long id) {

        def venda = Venda.get(id)

        if (venda) {
            [numeroPedido: String.format("%05d", venda.id), venda: venda, cliente: venda.cliente]

        } else {
            println "Erro ao carregar venda ${id}"
            render 'Venda não encontrada'
        }

    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def marcarComoEntregue(Long id) {
        def v = Venda.get(id)
        v.status = StatusVenda.Entregue

        v.save(flush: true)

        flash.message = 'A venda foi marcada como entregue'

        if (params.offset && params.max) {
            redirect(action: 'list', params: [offset: params.offset, max: params.max])
        } else {
            redirect(action: 'list')
        }


    }


    def cancelada() {

    }

    def index() {
        withFormat {
            html { redirect(action: "list", params: params) }
            json {
                respond Venda.findAllByStatusInList([StatusVenda.Entregue, StatusVenda.AguardandoPagamento])
            }
        }

    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = 'dateCreated'
        params.order = 'desc'
        respond Venda.list(params), model: [vendaInstanceTotal: Venda.count()]
    }

    private void addToShoppingCart(Produto produto, String unidade, Integer qtde) {

        def shoppingCart = getShoppingCart()

        def itemVenda = shoppingCart.itens.find { itemVenda ->
            itemVenda.produto.id == produto.id && itemVenda.unidade.equals(unidade)
        }

        if (itemVenda) {
            itemVenda.quantidade += qtde
        } else {

            itemVenda = new ItemVenda(produto: produto, unidade: unidade, quantidade: qtde)
            itemVenda.precoAVistaEmCentavos = produto.precoAVistaEmCentavos
            itemVenda.precoAPrazoEmCentavos = produto.precoAPrazoEmCentavos

            shoppingCart.addToItens(itemVenda)
        }

    }

    private ShoppingCart getShoppingCart() {
        if (!session.shoppingCart) {
            session.shoppingCart = new ShoppingCart()
        }
        return session.shoppingCart
    }


}