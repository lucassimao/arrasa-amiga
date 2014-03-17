package br.com.arrasaamiga

class Estoque  {

	Produto produto
    String unidade
    int quantidade

    List entradas

    static belongsTo = Produto
    static hasMany = [entradas:EntradaEstoque]

    static mapping = {
    	entradas cascade: 'all-delete-orphan'
    }
    
    static constraints = {
    	produto(nullable:false)
    	unidade(nullable:false,blank:false,unique:'produto')
    	quantidade(min:0)
    }

    public static void removerItens(Set itens){

        itens.each{item->

            def estoque = Estoque.findByProdutoAndUnidade(item.produto, item.unidade)
            println "Removendo ${item.quantidade} de ${item.produto.nome} - ${item.unidade} ... "
            estoque.quantidade -= item.quantidade
            estoque.save()
        }
    }

    public static void reporItens(Set itens){
        
        itens.each{item->

            def estoque = Estoque.findByProdutoAndUnidade(item.produto, item.unidade)
            println "Repondo ${item.quantidade} de ${item.produto.nome} - ${item.unidade} ... "
            estoque.quantidade += item.quantidade
            estoque.save()
        }
    }



}
