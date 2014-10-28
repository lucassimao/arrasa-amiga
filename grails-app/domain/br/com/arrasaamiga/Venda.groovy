package br.com.arrasaamiga

class Venda {


    Date dateCreated
    Cliente cliente
    int freteEmCentavos = 0
    int descontoPagSeguroEmCentavos = 0
    int taxasPagSeguroEmCentavos = 0
    FormaPagamento formaPagamento
    StatusVenda status
    Date dataEntrega
    String transacaoPagSeguro
    ShoppingCart carrinho
    ServicoCorreio servicoCorreio
    String codigoRastreio
    Usuario vendedor
    TurnoEntrega turnoEntrega

    transient def pagSeguroService
    transient def correiosService


    static transients = ['urlRastreioCorreios', 'valorTotal', 'valorItensAPrazo', 'valorItensAVista',
                         'descontoEmReais', 'freteEmReais', 'descontoParaCompraAVista', 'descontoPagSeguroEmReais',
                         'detalhesPagamento', 'itensVenda']

    static constraints = {
        freteEmCentavos(min: 0)
        codigoRastreio(blank: true, nullable: true)
        descontoPagSeguroEmCentavos(min: 0)
        taxasPagSeguroEmCentavos(min: 0)
        formaPagamento(nullable: false)
        status(nullable: false)
        cliente(nullable: false)
        dataEntrega(nullable: true)
        transacaoPagSeguro(blank: true, nullable: true)
        carrinho(nullable: true)
        servicoCorreio(nullable: true)
        vendedor(nullable: true)
        turnoEntrega(nullable: true)
    }

    static mapping = {
        autoTimestamp true
        cliente cascade: 'save-update'
        carrinho cascade: 'save-update'
    }

    /* Atualizando o estoque dos itens comprados */

    def afterInsert() {

        if (this.formaPagamento.equals(FormaPagamento.AVista)) {

            log.debug("Venda #${this.id} removendo itens ")
            Estoque.withNewSession { session ->
                Estoque.removerItens(this.itensVenda)
            }
        }
    }

    def afterDelete() {

        Estoque.withNewSession { session ->

            if (this.formaPagamento.equals(FormaPagamento.AVista) ||
                    ( formaPagamento.equals(FormaPagamento.PagSeguro) && this.status == StatusVenda.PagamentoRecebido)) {

                log.debug("Venda #${this.id} repondo itens ")
                Estoque.reporItens(this.itensVenda)
            }
        }
    }

    public Double getValorTotal() {
        BigDecimal valorItensAPrazo = new BigDecimal(getValorItensAPrazo().toString())
        BigDecimal freteEmReais = new BigDecimal(getFreteEmReais().toString())
        BigDecimal descontoEmReais = new BigDecimal(getDescontoEmReais().toString())

        return (valorItensAPrazo + freteEmReais - descontoEmReais).doubleValue()
    }

    public Double getValorItensAPrazo() {
        return this.carrinho.getValorTotalAPrazo()
    }

    public Double getValorItensAVista() {
        return this.carrinho.getValorTotalAVista()
    }


    public Double getFreteEmReais() {
        if (cliente.isDentroDaAreaDeEntregaRapida())
            return 2d
        else {

            if (this.freteEmCentavos > 0) {
                return this.freteEmCentavos / 100.0
            } else {
                return correiosService.calcularFrete(this.cliente?.endereco?.cep, this.servicoCorreio)
            }
        }
    }

    public Double getDescontoEmReais() {

        if (this.formaPagamento.equals(FormaPagamento.AVista)) {

            return getDescontoParaCompraAVista()

        } else if (this.descontoPagSeguroEmCentavos > 0) { // caso o cliente pague via boleto bancário

            return getDescontoPagSeguroEmReais()

        } else {
            return 0
        }
    }

    public String getDetalhesPagamento() {

        if (this.formaPagamento == FormaPagamento.AVista) {
            return 'Pagamento em dinheiro no momento do recebimento do produto'

        } else {

            if (this.transacaoPagSeguro) {
                return pagSeguroService.getDetalhesPagamento(transacaoPagSeguro)
            } else {
                return 'Transação não foi finalizada. Cliente não concluiu compra'
            }
        }
    }
    /*
     *  Esse desconto existe para pagamentos via boleto
     *
     */

    public Double getDescontoPagSeguroEmReais() {
        return this.descontoPagSeguroEmCentavos / 100.0
    }

    def getItensVenda() {
        return this.carrinho.itens
    }

    /*
     *
     * método utilitário
     *
     */

    public Double getDescontoParaCompraAVista() {
        def valorItensAPrazo = new BigDecimal(getValorItensAPrazo().toString())
        def valorItensAVista = new BigDecimal(getValorItensAVista().toString())

        def descontoEmCentavos = valorItensAPrazo - valorItensAVista

        return descontoEmCentavos.doubleValue()
    }


    public String getUrlRastreioCorreios() {
        return correiosService.getTrackingURL(codigoRastreio)
    }


}
