package br.com.arrasaamiga

import grails.util.Holders

class Venda {

    Date dateCreated,lastUpdated
    Cliente cliente
    boolean flagClienteVaiBuscar = false
    boolean flagClienteJaBuscou = false
    int abatimentoEmCentavos = 0
    int freteEmCentavos = 0
    int descontoPagSeguroEmCentavos = 0
    int taxasPagSeguroEmCentavos = 0 // taxa de parcelamento
    FormaPagamento formaPagamento
    StatusVenda status
    Date dataEntrega
    String transacaoPagSeguro
    ShoppingCart carrinho
    ServicoCorreio servicoCorreio
    String codigoRastreio
    Usuario vendedor
    TurnoEntrega turnoEntrega
    String detalhesPagamento

    transient def pagSeguroService
    transient def correiosService
    transient def gcmService

    static transients = ['urlRastreioCorreios', 'valorTotal', 'valorItensAPrazo', 'valorItensAVista',
                         'descontoEmReais', 'freteEmReais', 'descontoParaCompraAVista',
                         'descontoPagSeguroEmReais','itensVenda','taxasPagSeguroEmReais','desconto','frete']

    static hasMany = ['anexos':String]

    static constraints = {
        freteEmCentavos(min: 0)
        lastUpdated(nullable:true)
        codigoRastreio(blank: true, nullable: true)
        descontoPagSeguroEmCentavos(min: 0)
        taxasPagSeguroEmCentavos(min: 0)
        abatimentoEmCentavos(min:0)
        formaPagamento(nullable: false)
        status(nullable: false)
        cliente(nullable: false)
        dataEntrega(nullable: true)
        transacaoPagSeguro(blank: true, nullable: true)
        carrinho(nullable: true)
        servicoCorreio(nullable: true)
        vendedor(nullable: true)
        turnoEntrega(nullable: true)
        detalhesPagamento(blank: true,nullable: true,maxSize:100000)
    }

    static mapping = {
        autoTimestamp true
        cliente cascade: 'save-update'
        carrinho cascade: 'all'
    }

    def beforeInsert(){
        if (this.formaPagamento == FormaPagamento.AVista) {
            this.detalhesPagamento = 'Pagamento em dinheiro no momento do recebimento do produto'
        }else{
            this.detalhesPagamento = 'Transação não foi finalizada. Cliente ainda não concluiu a compra'
        }
    }

    def afterUpdate(){
        //TODO Verificar se continua sendo chamado mais de 1 vez em versoes > 2.5.4 do grails
        def config = Holders.config
        if (config.useGcmService)
            gcmService.notificarAtualizacao(lastUpdated.time,this)
    }

    def afterInsert(){
        def config = Holders.config
        if (config.useGcmService)
            gcmService.notificarAtualizacao(lastUpdated.time,this)
    }

    def afterDelete(){
        def config = Holders.config
        if (config.useGcmService)
            gcmService.notificarExclusao(Venda.class,this.id)
    }

    def setLastUpdated(Date dt){
        if (dt){
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.set(Calendar.MILLISECOND, 0);
            this.lastUpdated = c.time
        } else this.lastUpdated=null
    }

    /**
     * Retorna o valor total que o cliente pagará pelo pedido
     *
     */
    @Deprecated
    public Double getValorTotal() {
        BigDecimal valorItensAPrazo = new BigDecimal(getValorItensAPrazo().toString())
        BigDecimal freteEmReais = new BigDecimal(getFreteEmReais().toString())
        BigDecimal descontoEmReais = new BigDecimal(getDescontoEmReais().toString())

        return (valorItensAPrazo + freteEmReais - descontoEmReais).doubleValue()
    }

    @Deprecated
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

    @Deprecated
    public Double getDescontoEmReais() {

        if (this.formaPagamento.equals(FormaPagamento.AVista)) {

            return getDescontoParaCompraAVista()

        } else if (this.descontoPagSeguroEmCentavos > 0) { // caso o cliente pague via boleto bancário

            return getDescontoPagSeguroEmReais()

        } else {
            return 0
        }
    }

    @Deprecated
    public Double getDescontoParaCompraAVista() {
        def valorItensAPrazo = new BigDecimal(getValorItensAPrazo().toString())
        def valorItensAVista = new BigDecimal(getValorItensAVista().toString())

        def descontoEmReais = valorItensAPrazo - valorItensAVista
        return descontoEmReais.doubleValue()
    }

    /*
     *  Esse desconto existe para pagamentos via boleto
     *  @Deprecated deve ser removido
     */
    @Deprecated
    public Double getDescontoPagSeguroEmReais() {
        return this.descontoPagSeguroEmCentavos / 100.0
    }

    def getItensVenda() {
        return this.carrinho.itens
    }

    @Deprecated
    public Double getValorItensAPrazo() {
        return this.carrinho.getValorTotalAPrazo()
    }

    @Deprecated
    public Double getValorItensAVista() {
        return this.carrinho.getValorTotalAVista()
    }

    public String getUrlRastreioCorreios() {
        return correiosService.getTrackingURL(codigoRastreio)
    }

    /* novos metodos que tratam de moeda com tipo inteiro, tudo em centavos */


    public long getDesconto() {

        if (this.formaPagamento.equals(FormaPagamento.AVista))
            return _getDescontoParaCompraAVista() + abatimentoEmCentavos
         else
            return descontoPagSeguroEmCentavos
    }

    public long _getValorItensAPrazo() {
        return this.carrinho._getValorTotalAPrazo()
    }

    public Double _getValorItensAVista() {
        return this.carrinho._getValorTotalAVista()
    }

    public long _getDescontoParaCompraAVista() {
        long valorItensAPrazo = _getValorItensAPrazo()
        long valorItensAVista = _getValorItensAVista()
        long descontoEmCentavos = valorItensAPrazo - valorItensAVista

        return descontoEmCentavos
    }

    public long _getValorTotal() {
        return _getValorItensAPrazo() + this.freteEmCentavos - getDesconto()
    }

}
