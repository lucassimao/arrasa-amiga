package br.com.arrasaamiga

class Produto extends com.metasieve.shoppingcart.Shoppable  {

	String nome
    String descricao

    String fotoMiniatura
	List fotos    

    String tipoUnitario
    List unidades    

    int precoAVistaEmCentavos
    int precoAPrazoEmCentavos


	static hasMany = [fotos:String,unidades:String]
	static transients = ['precoAVistaEmReais','precoAPrazoEmReais']

    static constraints = {
    	nome(nullable:false,blank:false)
    	descricao(nullable:false,blank:false,maxSize:100000)
        tipoUnitario(nullable:false,blank:false)
        fotoMiniatura(nullable:false,blank:false)
    	precoAVistaEmCentavos(min:0)
        precoAPrazoEmCentavos(min:0)
    }


    public Double getPrecoAVistaEmReais(){
    	return this.precoAVistaEmCentavos/100
    }

    public void setPrecoAVistaEmReais(Double precoEmReais){
        this.precoAVistaEmCentavos = 100*precoEmReais
    } 

    public Double getPrecoAPrazoEmReais(){
        return this.precoAVistaEmCentavos/100
    }

    public void setPrecoAPrazoEmReais(Double precoEmReais){
        this.precoAVistaEmCentavos = 100*precoEmReais
    } 


}
