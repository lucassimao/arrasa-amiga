package br.com.arrasaamiga

import br.com.arrasaamiga.ItemVenda
import br.com.arrasaamiga.Produto


class ProdutoStarUpdaterJob {

    def sessionFactory

    static triggers = {
        simple repeatInterval: 60000l * 60 * 24 // 1 dia em milliseconds
    }

    def execute() {
        String sql = "select count(i.produto) from ItemVenda i group by i.produto order by count(i.produto) desc"
        def quantidadeDoProdutoMaisVendido = ItemVenda.executeQuery(sql, [max: 1])

        log.debug("Atualizando as entrelas de cada produto ... ")

        Produto.list().each { produto ->
            def quantidadeVendida = ItemVenda.countByProduto(produto)
            produto.stars = (quantidadeVendida * 5.0) / quantidadeDoProdutoMaisVendido
            produto.save()
        }

        def hibernateSession = sessionFactory.getCurrentSession()
        assert hibernateSession != null
        hibernateSession.flush()
    }
}
