package br.com.arrasaamiga

import br.com.arrasaamiga.excecoes.EstoqueException
import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory

@Transactional
class EstoqueService {

    public void removerItens(Set<ItemVenda> itens) throws EstoqueException {

        def vendaLogger = LogFactory.getLog('grails.app.domain.br.com.arrasaamiga.Venda')

        itens.each { ItemVenda item ->

            def estoque = Estoque.findByProdutoAndUnidade(item.produto, item.unidade)

            if (estoque == null) {
                def string = "Estoque inexistente para produto ${item.produto} e unidade ${item.unidade} "
                throw new EstoqueException(string,item.produto,item.unidade)
            }

            if (estoque.quantidade < item.quantidade) {
                def string = "Estoque insuficiente: há apenas ${estoque.quantidade} ${item.produto.nome}(${item.unidade}) em estoque "
                throw new EstoqueException(string,item.produto,item.unidade)
            }

            vendaLogger.debug "Removendo ${item.quantidade} de ${item.produto.nome} - ${item.unidade} ... "
            estoque.quantidade -= item.quantidade
            estoque.save()
        }
    }

    public void reporItens(Set<ItemVenda> itens) throws EstoqueException {

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
