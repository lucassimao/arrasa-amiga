package br.com.arrasaamiga

class Pedido {

	String descricao
	int quantidade
	Date dataPedido
    Date dataRecebimento

	int valorEmCentavosDeReais
    int freteEmCentavosDeReais

	String link
	String codigoRastreio

	StatusPedido status

    def correiosService


    static transients = ['valorEmReais','freteEmReais','iofEmReais',
                        'custoTotalEmReais','custoUnitarioEmReais','urlRastreioCorreios']

    static constraints = {
    	descricao(blank:false,nullable:false)
    	quantidade(min:1)
    	dataPedido(nullable:false)
        dataRecebimento(nullable:true)
    	valorEmCentavosDeReais(min:0)
        freteEmCentavosDeReais(min:0)
    	link(blank:true,nullable:true)
    	codigoRastreio(blank:true,nullable:true)
    	status(nullable:false)
    }

    public void setLink(String link){
        if (link && !link.startsWith("http://")){
            this.link = "http://" + link
        }else{
            this.link = link
        }
    }

    public void setFreteEmReais(double valor){
        this.freteEmCentavosDeReais = valor*100
    }

    public double getFreteEmReais(){
        return this.freteEmCentavosDeReais / 100.0
    }


    public void setValorEmReais(double valor){
        this.valorEmCentavosDeReais = valor*100
    }

    public double getValorEmReais(){
        return this.valorEmCentavosDeReais / 100.0
    }

    public Double getIofEmReais(){
        return  (getValorEmReais() + getFreteEmReais()) *(0.0638)
    }

    public Double getCustoTotalEmReais(){
        return getValorEmReais() + getFreteEmReais() + getIofEmReais()
    }


    public Double getCustoUnitarioEmReais(){
        return getCustoTotalEmReais()/quantidade
    }

    public String getUrlRastreioCorreios(){
        return correiosService.getTrackingURL(codigoRastreio)
    }

}
