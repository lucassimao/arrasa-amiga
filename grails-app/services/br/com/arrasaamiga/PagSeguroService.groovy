package br.com.arrasaamiga

import br.com.uol.pagseguro.domain.AccountCredentials
import br.com.uol.pagseguro.domain.Transaction
import br.com.uol.pagseguro.domain.PaymentMethodCode
import br.com.uol.pagseguro.exception.PagSeguroServiceException
import br.com.uol.pagseguro.service.TransactionSearchService


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
    	return StatusVenda.fromPagSeguroTransactionStatus(transaction.status)

	}


    public String getDetalhesPagamento(String transacaoPagSeguro){


    	Transaction transaction = getTransaction(transacaoPagSeguro)
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

}
