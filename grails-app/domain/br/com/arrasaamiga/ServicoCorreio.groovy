package br.com.arrasaamiga

enum ServicoCorreio {

	SEDEX('40010'),PAC('41106')

	String codigoServico

	ServicoCorreio(String codigoServico){
		this.codigoServico = codigoServico
	} 
    
}
