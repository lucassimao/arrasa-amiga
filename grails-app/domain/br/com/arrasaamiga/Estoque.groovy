package br.com.arrasaamiga

import br.com.arrasaamiga.excecoes.EstoqueException
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
        unidade sqlType: 'varchar(200) BINARY' //TODO dar um jeito de implementar comparação case sensitive das unidades
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
                throw new EstoqueException("Estoque inexistente para produto ${item.produto} e unidade ${item.unidade} ",item.produto,item.unidade)

            if (estoque.quantidade < item.quantidade)
                throw new EstoqueException("Estoque insuficiente: há apenas ${estoque.quantidade} ${item.produto.nome}(${item.unidade}) em estoque ",item.produto,item.unidade)

            vendaLogger.debug "Removendo ${item.quantidade} de ${item.produto.nome} - ${item.unidade} ... "
            estoque.quantidade -= item.quantidade
            estoque.save()
        }
    }

    public static void reporItens(Set itens) {

        def vendaLogger = LogFactory.getLog('grails.app.domain.br.com.arrasaamiga.Venda')

        itens.each {ItemVenda item ->

            def estoque = Estoque.findByProdutoAndUnidade(item.produto, item.unidade)

            if (estoque == null)
                throw new EstoqueException("Estoque inexistente para produto ${item.produto} e unidade ${item.unidade} ",item.produto,item.unidade)

            vendaLogger.debug "Repondo ${item.quantidade} de ${item.produto.nome} - ${item.unidade} ... "
            estoque.quantidade += item.quantidade
            estoque.save()
        }
    }


}
