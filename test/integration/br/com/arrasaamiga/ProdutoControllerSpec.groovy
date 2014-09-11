package br.com.arrasaamiga

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.test.spock.IntegrationSpec
import org.codehaus.groovy.grails.web.mime.MimeType
import spock.lang.*
import static org.junit.Assert.*

/**
 *
 */
class ProdutoControllerSpec extends IntegrationSpec {


    def sessionFactory


    protected void setup() {

        Estoque.executeUpdate('delete from Estoque')
        Produto.executeUpdate('delete from Produto')

        def produto1 = new Produto(descricao: 'Produto 1', nome: 'P1', keywords: ['limpeza', 'cabelo', 'hair', 'shampoo', 'parecido 2'],
                tipoUnitario: 'un', unidades: ['un'] as List)
        produto1.save(flush: true)

        def produto2 = new Produto(descricao: 'Produto 2', nome: 'P2', keywords: ['rosto', 'base', 'm.a.c', 'studio fix', 'pó', 'parecido 1'],
                tipoUnitario: 'un', unidades: ['un'] as List)
        produto2.save(flush: true)

        assert 2 == Produto.count()

        new Estoque(produto: produto1, unidade: 'un', quantidade: 10).save(flush: true)
        new Estoque(produto: produto2, unidade: 'un', quantidade: 5).save(flush: true)

        assert 2 == Estoque.count()
    }


    void "test atualizar produto"() {
        given:
            def produto = Produto.first()
            assertNull produto.fotos

            def fotos = ['UN' : ['fotoUN1', 'fotoUN22'],
                         'Caixa' : ['fotoCaixa1', 'fotoCaixa2', 'fotoCaixa3'],
                         'Lote': ['fotoLote1', 'fotoLote2', 'fotoLote3', 'fotoLote4']]
            def comentarios = ['fotoUN1' : 'Comentario 1',
                               'fotoUN22' : 'Comentario 2',
                               'fotoCaixa1' : 'Comentario 3',
                               'fotoLote1': 'Comentario 4',
                               'fotoLote4' : 'Comentario 5']

            def novoNome = 'Produto 1 - Atualizado'
            def novasUnidades = ['UN', 'Caixa', 'Lote']
            def novoValorAVistaEmCentavos = 1299
            def novoValorAPrazoEmCentavos = 2199

        when:
            def controller = new ProdutoController()
            controller.request.method = 'POST'
            controller.params.id = produto.id

            controller.params.nome = novoNome
            controller.params.unidades = (novasUnidades as JSON).toString()
            controller.params.precoAVistaEmReais = '12.99'
            controller.params.precoAPrazoEmReais = '21.99'
            controller.params.fotoComentario = (comentarios as JSON).toString()
            controller.params.fotosUnidades = (fotos as JSON).toString()

            controller.update()
            sessionFactory.currentSession.flush()
            sessionFactory.currentSession.clear()

        then:
            controller.response.redirectedUrl.matches('.*/show/\\d+$')
            def updatedProduto = Produto.findByNome(novoNome)
            assertNotNull produto
            produto.id == updatedProduto.id

            updatedProduto.precoAVistaEmCentavos == novoValorAVistaEmCentavos
            updatedProduto.precoAPrazoEmCentavos == novoValorAPrazoEmCentavos

            def estoques = Estoque.findAllByProduto(updatedProduto)
            estoques.size() == 3

            updatedProduto.unidades.containsAll(novasUnidades)
            assertNull Estoque.findByProdutoAndUnidade(updatedProduto,'un')

            updatedProduto.unidades.each { unidade ->
                unidade in novasUnidades

                def fotosDaUnidade = updatedProduto.fotos.findAll { FotoProduto fotoProduto -> fotoProduto.unidade.equals(unidade) }
                fotosDaUnidade.size() == fotos[unidade].size()

                fotosDaUnidade.each { FotoProduto foto ->
                    if (comentarios[foto.arquivo]) {
                        foto.comentario == comentarios[foto.arquivo]
                    }
                }

            }

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
            termo      | quantidadeDeResultados
            'lim'      | 1
            'limpeza'  | 1
            'p'        | 5
            'pó'       | 2
            'po'       | 2
            'PO'       | 2
            'parecido' | 2
    }

    void "test salvar produto sem fotos e comentarios"() {
        given:
        def nome = 'NOVO Produto'
        def unidades = ['amarelo', 'azul', 'verde']
        def valorAVistaEmCentavos = 1299
        def valorAPrazoEmCentavos = 2199
        when:
        def controller = new ProdutoController()
        controller.request.method = 'POST'
        controller.params.nome = nome
        controller.params.descricao = 'Produto NOVO cadastrado no sistema'
        controller.params.tipoUnitario = 'Cor'
        controller.params.unidades = (unidades as JSON).toString()
        controller.params.precoAVistaEmReais = '12.99'
        controller.params.precoAPrazoEmReais = '21.99'

        controller.save()
        then:
        controller.response.redirectedUrl.matches('.*/show/\\d+$')
        def produto = Produto.findByNome(nome)
        assertNotNull produto

        produto.precoAVistaEmCentavos == valorAVistaEmCentavos
        produto.precoAPrazoEmCentavos == valorAPrazoEmCentavos

        def estoques = Estoque.findAllByProduto(produto)
        estoques.size() == 3

        def estoqueAmarelo = Estoque.findByProdutoAndUnidade(produto, 'amarelo')
        estoqueAmarelo.quantidade == 0

        def estoqueAzul = Estoque.findByProdutoAndUnidade(produto, 'azul')
        estoqueAzul.quantidade == 0

        def estoqueVerde = Estoque.findByProdutoAndUnidade(produto, 'verde')
        estoqueVerde.quantidade == 0
    }


    void "test salvar produto completo"() {
        given:
        def nome = 'NOVA ROUPA'
        def keywords = ['roupa', 'teste', 'vestuario']
        def unidades = ['G', 'M', 'PP', 'P']
        def fotos = ['G' : ['fotoG1', 'fotoG2'],
                     'M' : ['fotoM1', 'fotoM2', 'fotoM3'],
                     'PP': ['fotoPP1', 'fotoPP2', 'fotoPP3', 'fotoPP4'],
                     'P' : ['fotoP1', 'fotoP2', 'fotoP3', 'fotoP4', 'fotoP5', 'fotoP6']]
        def comentarios = ['fotoG1' : 'Tamanho Grande',
                           'fotoM1' : 'Tamanho Medio - Foto 1',
                           'fotoM2' : 'Tamanho Medio - Foto 2',
                           'fotoPP1': 'Tamanho piquininin',
                           'fotoP1' : 'Tamanho pequeno']

        when:
        def controller = new ProdutoController()
        controller.request.method = 'POST'
        controller.params.nome = nome
        controller.params.descricao = 'Produto NOVO cadastrado no sistema'
        controller.params.tipoUnitario = 'Tamanho'
        controller.params.unidades = (unidades as JSON).toString()
        controller.params.fotoComentario = (comentarios as JSON).toString()
        controller.params.fotosUnidades = (fotos as JSON).toString()
        controller.params["palavrasChave[]"] = keywords

        controller.params.precoAVistaEmReais = '0'
        controller.params.precoAPrazoEmReais = '0'

        controller.save()
        then:
        controller.response.redirectedUrl.matches('.*/show/\\d+$')

        def produto = Produto.findByNome(nome)
        assertNotNull produto

        produto.unidades.size() == 4

        produto.keywords.size() == keywords.size()
        produto.keywords.containsAll(keywords)

        def estoques = Estoque.findAllByProduto(produto)
        estoques.size() == 4

        produto.unidades.each { unidade ->
            unidade in unidades

            def fotosDaUnidade = produto.fotos.findAll { FotoProduto fotoProduto -> fotoProduto.unidade.equals(unidade) }
            fotosDaUnidade.size() == fotos[unidade].size()

            fotosDaUnidade.each { FotoProduto foto ->
                if (comentarios[foto.arquivo]) {
                    foto.comentario == comentarios[foto.arquivo]
                }
            }

        }

    }


}
