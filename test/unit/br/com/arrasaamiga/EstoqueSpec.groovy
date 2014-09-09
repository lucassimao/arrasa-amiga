package br.com.arrasaamiga

import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(DomainClassUnitTestMixin)
class EstoqueSpec extends Specification {

    def setup() {
        mockDomain(Produto, [
                [nome: "Produto1", id: 1, descricao: 'Produto 1', tipoUnitario: 'un'],
                [nome: "Produto2", id: 2, descricao: 'Produto 2', tipoUnitario: 'un']])

        assertEquals Produto.count(), 2
        def produto1 = Produto.first()
        def produto2 = Produto.last()

        mockDomain(Estoque, [
                [unidade: "un-1a", quantidade: 10, produto: produto1],
                [unidade: "un-1b", quantidade: 20, produto: produto1],
                [unidade: "un-2", quantidade: 30, produto: produto2]])

        assertEquals  3,Estoque.count()
    }

    def cleanup() {
    }

    void "Testando a retirada de produtos do estoque"() {

        setup:
            def produto = Produto.get(idProduto)
            def item = new ItemVenda(unidade: unidade, quantidade: quantidadeVendida, produto: produto)

        when:
            Estoque.removerItens([item] as Set)

        then:
            def estoque = Estoque.findByProdutoAndUnidade(produto, unidade)
            estoque != null
            quantidadeFinalEmEstoque == estoque.quantidade

        where:
            unidade | quantidadeVendida | idProduto || quantidadeFinalEmEstoque
            'un-1a' | 2                 | 1         ||  8
            'un-1a' | 5                 | 1         ||  5
            'un-2'  | 5                 | 2         ||  25
            'un-2'  | 15                | 2         ||  15
    }

    void "testando a reposição de produtos no estoque"() {

        setup:
            def produto = Produto.get(idProduto)
            def item = new ItemVenda(unidade: unidade, quantidade: quantidadeVendida, produto: produto)

        when:
            Estoque.reporItens([item] as Set)

        then:
            def estoque = Estoque.findByProdutoAndUnidade(produto, unidade)
            estoque != null
            quantidadeEmEstoque == estoque.quantidade

        where:
            unidade | quantidadeVendida | idProduto || quantidadeEmEstoque
            'un-1a' | 2                 | 1         ||  12
            'un-2'  | 5                 | 2         ||  35
    }

    void "testando a retirada de quantidades maiores do que as disponiveis no estoque"() {
        given:
            def item1 = new ItemVenda(unidade: 'un-1a', quantidade: 20, produto: Produto.load(1))

        when:
            Estoque.removerItens([item1] as Set)

        then:
            thrown(IllegalArgumentException)
            def e1 = Estoque.findByProdutoAndUnidade(Produto.load(1), 'un-1a')
            10 == e1.quantidade
    }

    void "testando a retirada de items que nao existem no estoque"() {
        given:
            def item1 = new ItemVenda(unidade: 'unidade-inexistente', quantidade: 20, produto: Produto.load(1))

        when:
            Estoque.removerItens([item1] as Set)

        then:
            thrown(IllegalArgumentException)
    }

    void "testando reposição de itens que nao existem no estoque"() {
        given:
            def item1 = new ItemVenda(unidade: 'unidade-inexistente', quantidade: 20, produto: Produto.load(1))

        when:
            Estoque.reporItens([item1] as Set)

        then:
            thrown(IllegalArgumentException)
    }
}
