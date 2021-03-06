package br.com.arrasaamiga

import br.com.arrasaamiga.excecoes.EstoqueException
import br.com.uol.pagseguro.domain.Error
import br.com.uol.pagseguro.exception.PagSeguroServiceException
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.annotation.Propagation
import org.springframework.validation.FieldError

@Secured(['permitAll'])
class ShoppingCartController {

    def springSecurityService
    def vendaService
    def pagSeguroService


    static allowedMethods = [add: "POST", removerProduto: "POST", updateDeliveryAddress: 'POST']
    def beforeInterceptor = [action: this.&apagarPedidosNaoConcluidos, only: ['add','fecharVenda','removerProduto']]

    def index() {
        def shoppingCart = getShoppingCart()
        def total = shoppingCart.valorTotalAPrazo
        ['valorTotal': total, itens: shoppingCart.itens]
    }

    def add(Long id, Integer quantidade, String unidade) {

        def produtoInstance = Produto.get(id)
        def shoppingCart = getShoppingCart()

        if (!produtoInstance) {
            flash.message = "Produto ${id} desconhecido"
            redirect(action: 'index', absolute: true)
            return
        }

        if (!produtoInstance.unidades.contains(unidade)) {
            flash.message = "${produtoInstance.nome} não contem a unidade ${unidade}"
            redirect(action: 'index', absolute: true)
            return
        }

        if (quantidade <= 0) {
            flash.message = "Quantidade inválida: ${quantidade}"
            redirect(action: 'index', absolute: true)
            return
        }

        int qtdeEmEstoque = produtoInstance.getQuantidadeEmEstoque(unidade)

        if (qtdeEmEstoque == 0) {
            flash.message = "Este produto esta em falta temporariamente ;-("
            redirect(action: 'index', absolute: true)
            return
        }

        def qtdeAnterior = shoppingCart.getQuantidade(produtoInstance, unidade)
        // quantidade desse item que o cliente ja adicionou no carrinho

        if ((qtdeAnterior + quantidade) > qtdeEmEstoque) {

            flash.message = "Há apenas ${qtdeEmEstoque}  ${produtoInstance.nome} em estoque para esse item. Você ja incluiu ${qtdeAnterior}"

            if (params.origem) {
                redirect(url: params.origem, absolute: true)
            } else {
                redirect(uri: produtoInstance.nomeAsURL, absolute: true)
            }
            return
        }

        shoppingCart.add(produtoInstance, unidade, quantidade)

        if (qtdeAnterior == 0) {
            flash.message = " ${quantidade} ${produtoInstance.nome} adicionados(as) ao seu carrinho de compras"
        } else {
            flash.message = "Mais ${quantidade} ${produtoInstance.nome} adicionados(as) ao seu carrinho de compras"
        }

        redirect(action: "index")
    }


    def removerProduto(Long id, String unidade, Integer quantidade) {
        def produto = Produto.get(id)
        def shoppingCart = getShoppingCart()

        if (!produto.unidades.contains(unidade)) {
            flash.message = "${produto.nome} não contem a unidade ${unidade}"
            redirect(action: 'index')
            return
        }

        if (quantidade > 0) {
            shoppingCart.remove(produto, unidade, quantidade)
            flash.message = "${quantidade} ${produto.nome} removido(a) do seu carrinho de compras"
        } else {
            flash.message = 'Informe uma quantidade válida'
        }


        redirect(action: "index")
    }

    @Secured(['isAuthenticated()'])
    def recalcularTotais() {

        def venda = new Venda()
        venda.carrinho = getShoppingCart()
        venda.cliente = Cliente.findByUsuario(springSecurityService.currentUser)
        venda.formaPagamento = (params.formaPagamento) ? FormaPagamento.valueOf(params.formaPagamento) : FormaPagamento.PagSeguro

        if (!venda.cliente.isDentroDaAreaDeEntregaRapida()) {
            venda.servicoCorreio = (params.servicoCorreio) ? ServicoCorreio.valueOf(params.servicoCorreio) : ServicoCorreio.PAC
        }

        render(template: 'totalVendaDetalhes', model: [venda: venda])
    }

    @Secured(['isAuthenticated()'])
    def confirmAddress() {
        def user = springSecurityService.currentUser
        ['cliente': Cliente.findByUsuario(user)]
    }


    @Secured(['isAuthenticated()'])
    def updateDeliveryAddress(ClienteCommand command) {

        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)
        cliente.properties = params

        command.validate()
        command.endereco.validate()

        if (command.hasErrors() || command.endereco.hasErrors()) {

            command.errors.allErrors.each { FieldError error ->
                String field = error.field
                String code = error.code
                cliente.errors.rejectValue(field, code)
            }

            command.endereco.errors.allErrors.each { FieldError error ->
                String field = error.field
                String code = error.code
                cliente.endereco.errors.rejectValue(field, code)
            }
        }

        // verifica se foi enviada alguma atualização nos dados do cliente, pois ele esta vindo de /shoppingCart/confirmAddress
        if (!cliente.hasErrors() && !cliente.endereco.hasErrors()) {
            cliente.save(flush: true)
            redirect(action: 'checkout')

        } else {
            render view: 'confirmAddress', model: [cliente: cliente]
            return
        }
    }


    @Secured(['isAuthenticated()'])
    def checkout() {

        def shoppingCart = getShoppingCart()
        def itens = shoppingCart.itens

        if (!itens) {
            flash.message = message(code: "shoppingCart.empty")
            redirect(action: 'index')
            return
        }

        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)

        def venda = new Venda()
        venda.carrinho = shoppingCart
        venda.formaPagamento = FormaPagamento.AVista
        venda.cliente = cliente
        venda.carrinho = shoppingCart

        [venda: venda, diasDeEntrega: vendaService.getProximosDiasDeEntrega()]
    }

    @Secured(['isAuthenticated()'])
    def fecharVenda() {


        def vendaLogger = LogFactory.getLog('grails.app.domain.br.com.arrasaamiga.Venda')
        def sessionShoppingCart = getShoppingCart()

        if (!sessionShoppingCart.itens) {
            flash.message = message(code: "shoppingCart.empty")
            redirect(action: 'index')
            return
        }

        def venda = new Venda(params)
        venda.carrinho = new ShoppingCart()
        // clonando o carrinho da sessão antes de salvar, p/ evitar que submissões sucessivas da mesma venda
        sessionShoppingCart.itens.each { ItemVenda item ->
            venda.carrinho.add(item.produto, item.unidade, item.quantidade)
        }
        venda.status = StatusVenda.AguardandoPagamento
        venda.cliente = Cliente.findByUsuario(springSecurityService.currentUser)

        def model = [venda: venda, diasDeEntrega: vendaService.getProximosDiasDeEntrega()]

        // para clientes que nao são de Teresina, o pagamento deve ser necessariamente via PagSeguro
        if (!venda.cliente.isDentroDaAreaDeEntregaRapida() && venda.formaPagamento != FormaPagamento.PagSeguro) {

            flash.message = message(code: 'shoppingCart.formaPagamento.invalida')
            render(view: "checkout", model: model)
            return
        }

        if (venda.cliente.isDentroDaAreaDeEntregaRapida()) {

            try {
                venda.dataEntrega = new Date(Long.valueOf(params.dataEntrega))
                venda.clearErrors()
            } catch (Exception e) {
                flash.messageDataEntrega = message(code: 'shoppingCart.dataEntrega.invalida')
                redirect(action: 'checkout')
                return
            }

            if (!vendaService.isDataEntregaValida(venda.dataEntrega)) {
                flash.messageDataEntrega = message(code: 'shoppingCart.dataEntrega.naoPermitida')
                render(view: "checkout", model: model)
                return
            }

        }

        try {

            vendaService.salvarVenda(venda)

            switch (venda.formaPagamento) {
                case FormaPagamento.AVista:
                    session.shoppingCart = null
                    redirect(action: 'show', controller: 'venda', id: venda.id)
                    break

                case FormaPagamento.PagSeguro:
                    def paymentURL = pagSeguroService.getPaymentURL(venda)
                    vendaLogger.debug("venda pelo pagseguro #${venda.id} salva e redirecionando para ${paymentURL} ...")
                    redirect(url: paymentURL)
            }

        } catch (EstoqueException e) {
            e.printStackTrace()
            // cria um novo carrinho e atualiza a sessão
            def novoShoppingCart = new ShoppingCart()
            venda.itensVenda.each { item ->
                if (!(item.produto.equals(e.produto) && item.unidade.equals(e.unidade)))
                    novoShoppingCart.add(item.produto, item.unidade, item.quantidade)
            }
            session.shoppingCart = novoShoppingCart
            flash.message = e.message
            redirect(action: 'checkout')
            return

        } catch (PagSeguroServiceException e) {
            e.printStackTrace()
            flash.message = e.message
            vendaLogger.debug "Erro ao tentar ir para o pagseguro : cliente ${venda.cliente.id} ", e
            redirect(action: 'checkout')
        } catch (Exception e) {
            e.printStackTrace()
            flash.message = e.message
            redirect(action: 'checkout')
        }

    }

    private ShoppingCart getShoppingCart() {
        if (!session.shoppingCart) {
            session.shoppingCart = new ShoppingCart()
        }
        return session.shoppingCart
    }

    /**
     * Este método é um interceptor executado antes das actions add e fecharVenda.
     * Isso é necessário pra evitar que tentativas prévias de compra feitas
     * por um cliente impeça o mesmo de adquirir um produto
     */
    protected void apagarPedidosNaoConcluidos() {
        def user = springSecurityService.currentUser

        if (user != null) {
            def cliente = Cliente.findByUsuario(user)
            vendaService.excluirVendasNaoFinalizadas(cliente)
        }
    }


}
