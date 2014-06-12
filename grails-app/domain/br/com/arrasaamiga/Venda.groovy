package br.com.arrasaamiga

import br.com.uol.pagseguro.domain.*
import java.text.NumberFormat

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

    def pagSeguroService
    def correiosService
   

	static transients = ['urlRastreioCorreios','valorTotal','taxaEntregaEmReais','valorItensAPrazo', 'valorItensAVista',
                         'descontoEmReais','freteEmReais','descontoParaCompraAVista','descontoPagSeguroEmReais',
                         'paymentURL','pagSeguroService','detalhesPagamento','itensVenda','correiosService']

    static constraints = {
    	freteEmCentavos(min:0)
        codigoRastreio(blank:true,nullable:true)
        descontoPagSeguroEmCentavos(min:0)
        taxasPagSeguroEmCentavos(min:0)
    	formaPagamento(nullable:false)
        status(nullable:false)
        cliente(nullable:false)
        dataEntrega(nullable:true)
        transacaoPagSeguro(blank:true,nullable:true)
        carrinho(nullable:true)
        servicoCorreio(nullable:true)
    }

    static mapping = {
        autoTimestamp true
    }

    def beforeInsert(){
        this.freteEmCentavos = getFreteEmReais() * 100
    }

    public Double getValorTotal(){
        def valorItensAPrazo = new BigDecimal(getValorItensAPrazo().toString())
        def freteEmCentavos = new BigDecimal(getFreteEmReais().toString())
        def descontoEmCentavos = new BigDecimal(getDescontoEmReais().toString())
        def taxaEntregaEmCentavos = new BigDecimal(getTaxaEntregaEmReais().toString())

        return (valorItensAPrazo + freteEmCentavos  - descontoEmCentavos + taxaEntregaEmCentavos).doubleValue()
    }

    public Double getValorItensAPrazo(){
        return this.carrinho.getValorTotalAPrazo()
    }

    public Double getValorItensAVista(){
        return this.carrinho.getValorTotalAVista()
    }


    public Double getFreteEmReais(){
        if (cliente.isDentroDaAreaDeEntregaRapida())
            return 0
        else{

            if (this.freteEmCentavos > 0){
                
                return this.freteEmCentavos/100.0
            
            }else{

                return correiosService.calcularFrete(this.cliente?.endereco?.cep, this.servicoCorreio)

            }
        }
    }        

    public Double getDescontoEmReais(){

        if (this.formaPagamento.equals(FormaPagamento.AVista)){

            return getDescontoParaCompraAVista()

        }else if (this.descontoPagSeguroEmCentavos > 0){ // caso o cliente pague via boleto bancário

            return getDescontoPagSeguroEmReais()

        }else{
            return 0
        }
    }

    public Double getTaxaEntregaEmReais(){
        
        if (cliente.isDentroDaAreaDeEntregaRapida()){
            return 2
        }else{
            return 0
        }
        
    }

    public String getDetalhesPagamento(){

        if (this.formaPagamento == FormaPagamento.AVista){
            return 'Pagamento em dinheiro no momento do recebimento do produto'
        
        }else{

            if (this.transacaoPagSeguro){

                return pagSeguroService.getDetalhesPagamento(transacaoPagSeguro)

            }else{

                return 'Transação não foi finalizada. Cliente não concluiu compra'

            }

        }

    }
    /*
     *  Esse desconto existe para pagamentos via boleto
     *
     */
    public Double getDescontoPagSeguroEmReais(){
        return this.descontoPagSeguroEmCentavos/100.0
    }

    def getItensVenda(){
        return this.carrinho.itens
    } 

    /*
     *
     * método utilitário
     *
     */
    public Double getDescontoParaCompraAVista(){
        def valorItensAPrazo = new BigDecimal( getValorItensAPrazo().toString() )
        def valorItensAVista = new BigDecimal( getValorItensAVista().toString() )

        def descontoEmCentavos = valorItensAPrazo - valorItensAVista

        return descontoEmCentavos.doubleValue()
    }

    public URL getPaymentURL(){

        if (!this.id)
            throw new IllegalStateException("A venda deve estar salva!")

        if (this.formaPagamento == FormaPagamento.AVista){
            throw new IllegalStateException("Vendas a vista não devem requisitar URL de pagamento")
        }

        def formatter = NumberFormat.getInstance(Locale.US)
        formatter.setMinimumFractionDigits(2)

        def paymentRequest = new  PaymentRequest()

        paymentRequest.setCurrency(Currency.BRL) 

        // especificando os itens
        this.itensVenda.each{item->
            
            Double valorUnitario = item.precoAPrazoEmReais

            paymentRequest.addItem(
                item.id.toString(),
                item.produto.nome,
                item.quantidade,
                new BigDecimal(formatter.format(valorUnitario)),
                null,
                null  
            )
        }


        // nome completo, email, DDD e número de telefone
        paymentRequest.setSender(this.cliente.nome, this.cliente.email, this.cliente.dddTelefone, this.cliente.telefone);  

        // país, estado, cidade, bairro, CEP, rua, número, complemento
        paymentRequest.setShippingAddress(  
            "BRA",   
            cliente.endereco.uf.sigla,   
            cliente.endereco.cidade.nome,   
            cliente.endereco.bairro,  
            cliente.endereco.cep,    
            cliente.endereco.complemento,    
            "0",   
            '' 
        );  



        paymentRequest.extraAmount = new BigDecimal(formatter.format(this.freteEmReais + this.taxaEntregaEmReais) ) 
        paymentRequest.setReference(this.id.toString())

        paymentRequest.redirectURL = "http://www.arrasaamiga.com.br/pagSeguro/retorno/${this.id}"
        paymentRequest.notificationURL =  "http://www.arrasaamiga.com.br/pagSeguro/notificacoes/${this.id}"

        paymentRequest.setShippingType(ShippingType.NOT_SPECIFIED)


        return paymentRequest.register(pagSeguroService.accountCredentials);
    }


    public String getUrlRastreioCorreios(){
        return correiosService.getTrackingURL(codigoRastreio)
    }



}
