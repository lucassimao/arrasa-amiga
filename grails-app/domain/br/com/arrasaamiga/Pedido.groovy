package br.com.arrasaamiga

class Pedido {

	String descricao
	int quantidade
	Date dataPedido
    Date dataRecebimento

	int valorEmCentavosDeDolar
	int valorEmCentavosDeReais

	String link
	String codigoRastreio

	StatusPedido status


    static transients = ['valorEmDolar','valorEmReais']

    static constraints = {
    	descricao(blank:false,nullable:false)
    	quantidade(min:1)
    	dataPedido(nullable:false)
        dataRecebimento(nullable:true)
    	valorEmCentavosDeDolar(min:0)
    	valorEmCentavosDeReais(min:0)
    	link(blank:true,nullable:true)
    	codigoRastreio(blank:true,nullable:true)
    	status(nullable:false)
    }

    public void setValorEmDolar(double valor){
        this.valorEmCentavosDeDolar = valor*100
    }

    public double getValorEmDolar(){
        return this.valorEmCentavosDeDolar / 100.0
    }

    public void setValorEmReais(double valor){
        this.valorEmCentavosDeReais = valor*100
    }

    public double getValorEmReais(){
        return this.valorEmCentavosDeReais / 100.0
    }

}
