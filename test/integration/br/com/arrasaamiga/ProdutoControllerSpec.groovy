package br.com.arrasaamiga

import grails.test.spock.IntegrationSpec
import org.codehaus.groovy.grails.web.mime.MimeType
import spock.lang.*

/**
 *
 */
class ProdutoControllerSpec extends IntegrationSpec {


    static boolean testDataGenerated = false


    def setup() {

            def produto1 = new Produto(descricao: 'Produto 1', nome: 'P1', keywords: ['limpeza', 'cabelo', 'hair', 'shampoo','parecido 2'],
                                        tipoUnitario: 'un', unidades: ['un'] as List)
            produto1.save(flush: true)

            def produto2 = new Produto(descricao: 'Produto 2', nome: 'P2', keywords: ['rosto', 'base', 'm.a.c', 'studio fix','pó','parecido 1'],
                                        tipoUnitario: 'un', unidades: ['un'] as List)
            produto2.save(flush: true)

            assert 2 == Produto.count()

            def e1 = new Estoque(produto: produto1, unidade: 'un', quantidade: 10)
            e1.save(flush: true)

            def e2 = new Estoque(produto: produto2, unidade: 'un', quantidade: 5)
            e2.save(flush: true)

            assert 2 == Estoque.count()

    }

    def cleanup() {
    }

    @Unroll
    void "test getTags"() {
         when:
            def controller = new ProdutoController()
            controller.params.term = termo
            controller.getTags()

        then:
            controller.response.json.size() == quantidadeDeResultados

        where:
            termo       | quantidadeDeResultados
            'lim'       | 1
            'limpeza'   | 1
            'p'         | 5
            'pó'        | 2
            'po'        | 2
            'parecido'  | 2

    }
}
