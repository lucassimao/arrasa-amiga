package br.com.arrasaamiga

import org.apache.commons.logging.LogFactory

class Estoque {

    Produto produto
    String unidade
    int quantidade

    List entradas

    static belongsTo = Produto
    static hasMany = [entradas: EntradaEstoque]

    static mapping = {
        entradas cascade: 'all-delete-orphan'
    }

    static constraints = {
        produto(nullable: false)
        unidade(nullable: false, blank: false)
        quantidade(min: 0)
    }


    public static void removerItens(Set itens) {

        def vendaLogger = LogFactory.getLog('grails.app.domain.br.com.arrasaamiga.Venda')

        itens.each { ItemVenda item ->

            def estoque = Estoque.findByProdutoAndUnidade(item.produto, item.unidade)

            if (estoque == null)
                throw new IllegalArgumentException("Estoque inexistente para produto ${item.produto} e unidade ${item.unidade} ")

            if (estoque.quantidade < item.quantidade)
                throw new IllegalArgumentException("Tentativa de retirar ${item.quantidade} itens, mas so existe ${estoque.quantidade} ")

            vendaLogger.debug "Removendo ${item.quantidade} de ${item.produto.nome} - ${item.unidade} ... "
            estoque.quantidade -= item.quantidade
            estoque.save(flush: true)
        }
    }

    public static void reporItens(Set itens) {

        def vendaLogger = LogFactory.getLog('grails.app.domain.br.com.arrasaamiga.Venda')

        itens.each { item ->

            def estoque = Estoque.findByProdutoAndUnidade(item.produto, item.unidade)

            if (estoque == null)
                throw new IllegalArgumentException("Estoque inexistente para produto ${item.produto} e unidade ${item.unidade} ")

            vendaLogger.debug "Repondo ${item.quantidade} de ${item.produto.nome} - ${item.unidade} ... "
            estoque.quantidade += item.quantidade
            estoque.save(flush: true)
        }
    }


}
