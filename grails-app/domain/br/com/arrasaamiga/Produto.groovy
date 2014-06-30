package br.com.arrasaamiga

class Produto {

	String nome
    String descricao
    String marca

    String fotoMiniatura
	List fotos    

    String tipoUnitario
    List unidades    

    int precoAVistaEmCentavos
    int precoAPrazoEmCentavos

    int ordem
    Boolean visivel

    Date dateCreated
    Date lastUpdated

	static hasMany = [fotos:FotoProduto,unidades:String,keywords:String,grupos:GrupoDeProduto]
	static transients = ['precoAVistaEmReais','precoAPrazoEmReais','estoques','quantidadeEmEstoque','nomeAsURL','multiUnidade','descontoAVistaEmReais']

    static constraints = {
    	nome nullable:false,blank:false
    	descricao nullable:false,blank:false,maxSize:100000
        tipoUnitario nullable:false,blank:false
        fotoMiniatura nullable:true,blank:true
        marca nullable:true,blank:true
    	precoAVistaEmCentavos min:0
        precoAPrazoEmCentavos min:0
        ordem min:0
        visivel nullable:true
        dateCreated nullable: true
        lastUpdated nullable:true

    }

    static mapping = {
        fotos cascade: 'all-delete-orphan'
    }


    public Double getPrecoAVistaEmReais(){
    	return this.precoAVistaEmCentavos/100.0
    }

    public void setPrecoAVistaEmReais(Double precoEmReais){
        this.precoAVistaEmCentavos = 100*precoEmReais
    } 

    public Double getPrecoAPrazoEmReais(){
        return this.precoAPrazoEmCentavos/100.0
    }

    public void setPrecoAPrazoEmReais(Double precoEmReais){
        this.precoAPrazoEmCentavos = 100*precoEmReais
    } 

    public Double getDescontoAVistaEmReais(){
        return ( this.precoAPrazoEmCentavos - this.precoAVistaEmCentavos )/ 100.0
    }

    public List getEstoques(){
        return Estoque.findAllByProduto(this)
    }

    public int getQuantidadeEmEstoque(String unidade){
        
        if (this.unidades?.contains(unidade)){
            def estoque = Estoque.findByProdutoAndUnidade(this,unidade)
            
            if (estoque){
                return estoque.quantidade
            }else{
                throw new IllegalStateException("Não existe estoque do produto ${this} com a unidade ${unidade} cadastrada no sistema! wtf !!!")
            }

        }else{
            throw new IllegalArgumentException("A unidade ${unidade} não existe para o produto ${this}")
        }

    }

    public String getNomeAsURL(){
       return "/" + this.nome.replace(',','').split(' ').join('-') + "-" + this.id
    }

    public boolean isMultiUnidade(){
        return this.unidades?.size() > 1
    }


}
