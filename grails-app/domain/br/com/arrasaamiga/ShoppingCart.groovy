package br.com.arrasaamiga

class ShoppingCart {

    static hasMany = [itens: ItemVenda]
    Date dateCreated
    Date lastUpdated

    static mapping = {
        itens lazy: false, cascade: 'all-delete-orphan'
    }

    static transients = ['valorTotalAPrazo', 'valorTotalAVista']

    public int getQuantidade(Produto produto, String unidade) {

        def set = this.itens.findAll { itemVenda ->
            itemVenda.produto.id == produto.id && itemVenda.unidade.equals(unidade)
        }

        return set.sum(0) { it.quantidade }
    }

    @Deprecated
    public Double getValorTotalAPrazo() {
        int total = 0

        this.itens?.each { itemVenda ->

            total += itemVenda.precoAPrazoEmCentavos * itemVenda.quantidade
        }

        return total / 100.0

    }

    @Deprecated
    public Double getValorTotalAVista() {
        int total = 0

        this.itens?.each { itemVenda ->
            total += itemVenda.precoAVistaEmCentavos * itemVenda.quantidade
        }

        return total / 100.0

    }

    public void remove(Produto produto, String unidade){
        def itemVenda = this.itens.find { itemVenda ->
            itemVenda.produto.id == produto.id && itemVenda.unidade.equals(unidade)
        }

        if(itemVenda) this.removeFromItens(itemVenda)

    }

    public void remove(Produto produto, String unidade, Integer quantidade) {

        def itemVenda = this.itens.find { itemVenda ->
            itemVenda.produto.id == produto.id && itemVenda.unidade.equals(unidade)
        }

        if (itemVenda) {
            itemVenda.quantidade -= quantidade

            if (itemVenda.quantidade == 0) {
                this.removeFromItens(itemVenda)
            }
        }
    }

    public void add(Produto produto, String unidade, Integer qtde) {


        def itemVenda = this.itens.find { ItemVenda itemVenda ->
            ( itemVenda.produto.id == produto.id ) && ( itemVenda.unidade.equals(unidade) )
        }

        if (itemVenda) {
            itemVenda.quantidade += qtde
        } else {

            itemVenda = new ItemVenda(produto: produto, unidade: unidade, quantidade: qtde)
            itemVenda.precoAVistaEmCentavos = produto.precoAVistaEmCentavos
            itemVenda.precoAPrazoEmCentavos = produto.precoAPrazoEmCentavos

            this.addToItens(itemVenda)
        }

    }


    /** metodos novos, tratam valores monetarios como centavos */

    public long _getValorTotalAPrazo() {
        long total = 0

        this.itens?.each { itemVenda ->

            total += itemVenda.precoAPrazoEmCentavos * itemVenda.quantidade
        }

        return total

    }

    public long _getValorTotalAVista() {
        int total = 0

        this.itens?.each { itemVenda ->
            total += itemVenda.precoAVistaEmCentavos * itemVenda.quantidade
        }

        return total

    }


}
