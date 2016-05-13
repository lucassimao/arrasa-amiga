package br.com.arrasaamiga

import grails.util.Holders

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
    double stars
    double bonus

    boolean foraDeLinha
    Boolean visivel

    Date dateCreated
    Date lastUpdated

		static hasMany = [fotos:FotoProduto,unidades:String,keywords:String,grupos:GrupoDeProduto]

		static transients = ['precoAVistaEmReais','precoAPrazoEmReais','estoques','grupoPadrao',
                        'produtosRelacionados','quantidadeEmEstoque','nomeAsURL','multiUnidade',
                        'descontoAVistaEmReais','novidade']

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

		transient def gcmService
		transient def grailsApplication

    static mapping = {
        fotos cascade: 'all-delete-orphan'
        keywords cascade: 'delete'
    }

		def afterUpdate(){
				def config = Holders.config
				if (config.useGcmService)
					gcmService.notificarAtualizacao()
    }

    def afterInsert(){
				def config = Holders.config
				if (config.useGcmService)
					gcmService.notificarAtualizacao()
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

    /**
     * Implementa o conceito de grupo padrao
     *
     * Um produto pode estar em vários grupos de produtos (menus na pagina principal)
     *
     * Caso questionado um único grupo a qual ele pertença,
     * ele retorna o grupo mais especializado, mais profundo na hierarquia dos grupos a qual ele pertence
     *
     * Caso haja empate entre grupos quanto ao criterio anterior,
     * esses são ordenados em ordem crescente pelo nome, e o que ficar em primeiro eh retornado
     *
     */
    public GrupoDeProduto getGrupoPadrao(){
        def map = [:]

        this.grupos.each{grupo->
            int profundidade = grupo.ancestrais.size()

            if (!map[profundidade]){
                map[profundidade] = []
            }
            map[profundidade] << grupo
        }

        if (map.keySet()){

            int deeper = map.keySet().max()

            if (map[deeper].size() == 1)
                return map[deeper][0]
            else
                return map[deeper].min{g1, g2-> g1.nome.compareTo(g2.nome)}

        }else
            return null

    }


    public Set getProdutosRelacionados(int quantidade){
        def grupoPadrao = this.grupoPadrao
        Set gruposIds = []

        if (grupoPadrao){
            gruposIds += grupoPadrao.ancestrais*.id
            gruposIds << grupoPadrao.id
        }

        if (gruposIds){

            def produtoID = this.id
            def criteria = Produto.createCriteria()
            def produtos = criteria.listDistinct {
                eq("visivel", true)
                ne('id',produtoID) // removendo o produto atual da lista

                grupos{
                    inList("id", gruposIds)
                }

                maxResults(quantidade)
                order("ordem", "asc")
            }

            return produtos

        }else
            return []

    }

    public BigDecimal calcularValorParcela(int numeroParcelas){
        int valorParceladoEmCentavos = this.precoAPrazoEmCentavos/numeroParcelas
        return new BigDecimal(valorParceladoEmCentavos/100.0)
    }

    public boolean isNovidade(){
        def hoje = new Date()
        return ( this.dateCreated && ( (hoje - this.dateCreated) < 30) )
    }

}
