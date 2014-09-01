package br.com.arrasaamiga

import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification

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

        mockDomain(Estoque, [
                [unidade: "un-1a", quantidade: 10, produto: Produto.get(1)],
                [unidade: "un-1b", quantidade: 20, produto: Produto.get(1)],
                [unidade: "un-2", quantidade: 30, produto: Produto.get(2)]])

        assertEquals Estoque.count(), 3
    }

    def cleanup() {
    }

    void "Testando a retirada de produtos do estoque"() {

        given:
        def item1 = new ItemVenda(unidade: 'un-1a', quantidade: 2, produto: Produto.load(1))
        def item2 = new ItemVenda(unidade: 'un-2', quantidade: 5, produto: Produto.load(2))

        when:
        Estoque.removerItens([item1, item2] as Set)

        then:
        def e1 = Estoque.findByProdutoAndUnidade(Produto.load(1), 'un-1a')
        assertNotNull e1
        assertEquals 8, e1.quantidade

        def e2 = Estoque.findByProdutoAndUnidade(Produto.load(2), 'un-2')
        assertNotNull e2
        assertEquals 25, e2.quantidade
    }

    void "testando a reposição de produtos no estoque"() {
        given:
        def item1 = new ItemVenda(unidade: 'un-1a', quantidade: 2, produto: Produto.load(1))
        def item2 = new ItemVenda(unidade: 'un-2', quantidade: 5, produto: Produto.load(2))

        when:
        Estoque.reporItens([item1, item2] as Set)

        then:
        def e1 = Estoque.findByProdutoAndUnidade(Produto.load(1), 'un-1a')
        assertNotNull e1
        assertEquals 12, e1.quantidade

        def e2 = Estoque.findByProdutoAndUnidade(Produto.load(2), 'un-2')
        assertNotNull e2
        assertEquals 35, e2.quantidade
    }

    void "testando a retirada de quantidades maiores do que as disponiveis no estoque"() {
        given:
        def item1 = new ItemVenda(unidade: 'un-1a', quantidade: 20, produto: Produto.load(1))

        when:
        Estoque.removerItens([item1] as Set)

        then:
        thrown(IllegalArgumentException)
        def e1 = Estoque.findByProdutoAndUnidade(Produto.load(1), 'un-1a')
        assertNotNull e1
        assertEquals 10, e1.quantidade
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
