package br.com.arrasaamiga

import br.com.uol.pagseguro.domain.*
import java.text.NumberFormat

class Venda {


	List itensVenda
	Date dateCreated
	Cliente cliente

	int freteEmCentavos
	int subTotalItensEmCentavos
    int descontoEmCentavos
    int taxasPagSeguroEmCentavos

	FormaPagamento formaPagamento

    StatusVenda status

    Date dataEntrega

    String transacaoPagSeguro

    ShoppingCart carrinho

    def pagSeguroService
   

	static hasMany = [itensVenda:ItemVenda]
	static transients = ['valorTotal','paymentURL','pagSeguroService','mailService',
                        'detalhesPagamento','subTotalItensEmReais','descontoEmReais','freteEmReais']

    static constraints = {
    	freteEmCentavos(min:0)
        subTotalItensEmCentavos(min:0)
        descontoEmCentavos(min:0)
        taxasPagSeguroEmCentavos(min:0)
    	itensVenda(nullable:false)
    	formaPagamento(nullable:false)
        status(nullable:false)
        cliente(nullable:false)
        dataEntrega(nullable:true)
        transacaoPagSeguro(blank:true,nullable:true)
        carrinho(nullable:true)
    }

    static mapping = {
        autoTimestamp true
    }

    public Double getValorTotal(){
    	return getSubTotalItensEmReais() + getFreteEmReais()  - getDescontoEmReais()
    }

    public Double getSubTotalItensEmReais(){
        return this.subTotalItensEmCentavos / 100.0
    }

    public Double getFreteEmReais(){
        return this.freteEmCentavos / 100.0
    }

    public Double getDescontoEmReais(){
        return this.descontoEmCentavos / 100.0
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
            
            Double valorUnitario = (formaPagamento == FormaPagamento.PagSeguro)? item.precoAPrazoEmReais : item.precoAVistaEmReais

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
            cliente.endereco.complemento    
        );  



        paymentRequest.extraAmount = new BigDecimal(formatter.format(this.freteEmReais) )
        paymentRequest.setReference(this.id.toString())

        paymentRequest.redirectURL = "http://www.arrasaamiga.com.br/pagSeguro/retorno/${this.id}"
        paymentRequest.notificationURL =  "http://www.arrasaamiga.com.br/pagSeguro/notificacoes/${this.id}"

        paymentRequest.setShippingType(ShippingType.NOT_SPECIFIED)


        return paymentRequest.register(pagSeguroService.accountCredentials);
    }

}
