package br.com.arrasaamiga

import br.com.uol.pagseguro.domain.AccountCredentials
import br.com.uol.pagseguro.domain.PaymentRequest
import br.com.uol.pagseguro.domain.ShippingType
import br.com.uol.pagseguro.domain.Transaction
import br.com.uol.pagseguro.domain.PaymentMethodCode
import br.com.uol.pagseguro.domain.TransactionStatus
import br.com.uol.pagseguro.exception.PagSeguroServiceException
import br.com.uol.pagseguro.service.TransactionSearchService
import br.com.uol.pagseguro.service.NotificationService

import java.text.NumberFormat

class PagSeguroService {

    boolean transactional = true

    def getAccountCredentials(){
    	String token = '9A9DE1A43A2045DEBBD66D629FC4F76B'
    	def accountCredentials = new AccountCredentials('lsimaocosta@gmail.com',token)
    	return accountCredentials
    }

    public Transaction getTransaction(String transacaoPagSeguro){

     	if (transacaoPagSeguro){
	        try {  
	            // Realiza a busca  
	            return TransactionSearchService.searchByCode( getAccountCredentials(), transacaoPagSeguro)

	        } catch (PagSeguroServiceException e) {  
	            e.printStackTrace()
	            return null
	        }  
	  
    	}else{
    		throw new IllegalArgumentException('Transação inválida')
    	}   	
    }

    public StatusVenda getStatusTransacao(String transacaoPagSeguro){
    	Transaction transaction = getTransaction(transacaoPagSeguro)
    	return getStatusTransacao(transaction.status)
	}

    public StatusVenda getStatusTransacao(TransactionStatus status) {

        switch (status.value) {
            case 1:
                return StatusVenda.AguardandoPagamento
            case 2:
                return StatusVenda.EmAnalise
            case 3:
                return StatusVenda.PagamentoRecebido
            case 4:
                return StatusVenda.Disponivel
            case 5:
                return StatusVenda.EmDisputa
            case 6:
                return StatusVenda.Devolvida
            case 7:
                return StatusVenda.Cancelada
            default:
                throw new IllegalArgumentException("status esconhecido: ${status}")
        }

    }


    public String getDetalhesPagamento(String transacaoPagSeguro){
    	Transaction transaction = getTransaction(transacaoPagSeguro)
    	return getDetalhesPagamento(transaction)
    }


    public String getDetalhesPagamento(Transaction transaction){
    	int numParcelas = transaction.getInstallmentCount()
    	PaymentMethodCode paymentMethodCode = transaction.getPaymentMethod().getCode()

    	switch(paymentMethodCode.value){
    		case 101: return "Cartão de crédito VISA em ${numParcelas}X"
			case 102: return "Cartão de crédito MasterCard em ${numParcelas}X"
			case 103: return "Cartão de crédito American Express em ${numParcelas}X"
			case 104: return "Cartão de crédito Diners em ${numParcelas}X"
			case 105: return "Cartão de crédito Hipercard em ${numParcelas}X"
			case 106: return "Cartão de crédito Aura em ${numParcelas}X"
			case 107: return "Cartão de crédito Elo em ${numParcelas}X"
			case 108: return "Cartão de crédito PLENOCard em ${numParcelas}X"
			case 109: return "Cartão de crédito PersonalCard em ${numParcelas}X"
			case 110: return "Cartão de crédito JCB em ${numParcelas}X"
			case 111: return "Cartão de crédito Discover em ${numParcelas}X"
			case 112: return "Cartão de crédito BrasilCard em ${numParcelas}X"
			case 113: return "Cartão de crédito FORTBRASIL em ${numParcelas}X"
			case 114: return "Cartão de crédito CARDBAN em ${numParcelas}X"
			case 115: return "Cartão de crédito VALECARD em ${numParcelas}X"
			case 116: return "Cartão de crédito Cabal em ${numParcelas}X"
			case 117: return "Cartão de crédito Mais! em ${numParcelas}X"
			case 118: return "Cartão de crédito Avista em ${numParcelas}X"
			case 119: return "Cartão de crédito GRANDCARD em ${numParcelas}X"
			case 201: return "Boleto Bradesco"
			case 202: return "Boleto Santander"
			case 301: return "Transferência eletrônica de fundos do banco Bradesco"
			case 302: return "Transferência eletrônica de fundos do banco Itaú"
			case 303: return "Transferência eletrônica de fundos do Unibanco"
			case 304: return "Transferência eletrônica de fundos do Banco do Brasil"
			case 305: return "Transferência eletrônica de fundos do banco Real"
			case 306: return "Transferência eletrônica de fundos do banco Banrisul"
			case 401: return "Saldo de conta PagSeguro"
			case 501: return "Oi Paggo"
			case 701: return "Depósito em conta - Banco do Brasil"
			default:
				return "Pagamento codigo ${paymentMethodCode.value} ${paymentMethodCode}"
    	}

    }

    public Transaction checkTransaction(String notificationCode){
    	return NotificationService.checkTransaction( getAccountCredentials(), notificationCode)
    }

    public URL getPaymentURL(Venda venda) {

        if (!venda.id)
            throw new IllegalStateException("A venda deve estar salva!")

        if (venda.formaPagamento == FormaPagamento.AVista) {
            throw new IllegalStateException("Vendas a vista não devem requisitar URL de pagamento")
        }

        def formatter = NumberFormat.getInstance(Locale.US)
        formatter.setMinimumFractionDigits(2)

        def paymentRequest = new PaymentRequest()

        paymentRequest.setCurrency(br.com.uol.pagseguro.domain.Currency.BRL)

        // especificando os itens
        venda.itensVenda.each { item ->
            Double valorUnitario = item.precoAPrazoEmReais

            paymentRequest.addItem( item.id.toString(), item.produto.nome, item.quantidade,
                                    new BigDecimal(formatter.format(valorUnitario)),   null,   null )
        }

        // nome completo, email, DDD e número de telefone
        Cliente cliente = venda.cliente
        paymentRequest.setSender(cliente.nome, cliente.email, cliente.dddTelefone, cliente.telefone);

        // país, estado, cidade, bairro, CEP, rua, número, complemento
        Endereco endereco = cliente.endereco
        String complemento = endereco.complemento
        if (complemento?.length() > 60)
            complemento = complemento[0..<60]
        paymentRequest.setShippingAddress( "BRA", endereco.uf.sigla,endereco.cidade.nome, endereco.bairro,
                                            endereco.cep, complemento, "0", '' );



        paymentRequest.extraAmount = new BigDecimal(formatter.format(venda.freteEmReais))
        paymentRequest.setReference(venda.id.toString())

        paymentRequest.redirectURL = "http://www.arrasaamiga.com.br/pagSeguro/retorno/${venda.id}"
        paymentRequest.notificationURL = "http://www.arrasaamiga.com.br/pagSeguro/notificacoes/${venda.id}"
        paymentRequest.setShippingType(ShippingType.NOT_SPECIFIED)

        return paymentRequest.register(getAccountCredentials())
    }

}
