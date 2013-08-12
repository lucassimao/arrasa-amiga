package br.com.arrasaamiga

import br.com.uol.pagseguro.domain.*

class PagSeguroController {

	def token = '9A9DE1A43A2045DEBBD66D629FC4F76B'
	
    def index() { }

    def retorno(){
    	def transacao = params.transacao

    	def accountCredentials = new AccountCredentials('lsimaocosta@gmail.com',token)
    	
    	def paymentRequest = new  PaymentRequest()
    	paymentRequest.setCurrency(Currency.BRL) 


    	// id, descrição, quantidade, valor unitário, peso e valor de frete
		paymentRequest.addItem(  
		    "0001",  
		    "Notebook Prata",   
		    new Integer(1),   
		    new BigDecimal("2430.00"),   
		    new Long(1000),   
		    null  
		);  
		      
		paymentRequest.addItem(  
		    "0002",  
		    "Notebook Rosa",   
		    new Integer(2),   
		    new BigDecimal("2560.00"),   
		    new Long(750),   
		    null  
		); 

		paymentRequest.setReference("REF1234");  

		paymentRequest.setShippingType(ShippingType.SEDEX);  // NOT_SPECIFIED ou PAC

		// país, estado, cidade, bairro, CEP, rua, número, complemento
		paymentRequest.setShippingAddress(  
		    "BRA",   
		    "SP",   
		    "São Paulo",   
		    "Jardim Paulistano",   
		    "01452002",   
		    "Av. Brig. Faria Lima",   
		    "1384",   
		    "5o andar"  
		);  

		// nome completo, email, DDD e número de telefone
		paymentRequest.setSender("José Comprador", "comprador@uol.com.br", "11", "56273440");  

		URL paymentURL = paymentRequest.register(accountCredentials);  




    }
}
