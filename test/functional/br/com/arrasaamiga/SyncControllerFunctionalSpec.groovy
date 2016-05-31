package br.com.arrasaamiga

import grails.converters.JSON
import grails.test.mixin.Mock
import spock.lang.*
import org.codehaus.groovy.grails.web.mime.MimeType
import static javax.servlet.http.HttpServletResponse.*
import geb.spock.GebReportingSpec
import grails.plugin.remotecontrol.RemoteControl
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse

@Stepwise
class SyncControllerFunctionalSpec extends GebReportingSpec    {

    @Shared def remote = new RemoteControl()
    @Shared def authToken = 'token-functional-test'
    @Shared def rest = new RestBuilder()

    def setupSpec(){
        remote.exec{
            UsuarioGrupoDeUsuario.executeUpdate('delete UsuarioGrupoDeUsuario')
            Usuario.executeUpdate('delete AuthenticationToken')
            Usuario.executeUpdate('delete Cliente')
            Usuario.executeUpdate('delete Usuario')

            def user = new Usuario(username:'test',password:'123',enabled:true)
            assert user.save(flush:true,failOnError:true)

            def grupoAdmin = GrupoDeUsuario.findByAuthority('ROLE_ADMIN')
            assert grupoAdmin!=null
            UsuarioGrupoDeUsuario.create user, grupoAdmin, true

            assert new AuthenticationToken(tokenValue:'token-functional-test',
                                    username:'test').save(flush:true,failOnError:true)

            return true
        }
    }

    def cleanupSpec(){
        remote.exec{
            Venda.executeUpdate('delete Venda')
            Venda.executeUpdate('delete ShoppingCart')
            Venda.executeUpdate('delete ItemVenda')
            Venda.executeUpdate('delete Estoque')
            Venda.executeUpdate('delete Produto')
            Usuario.executeUpdate('delete Cliente')
            Usuario.executeUpdate('delete UsuarioGrupoDeUsuario')
            Usuario.executeUpdate('delete Usuario')
            Usuario.executeUpdate('delete AuthenticationToken')
        }
    }
    def setup() {
        remote.exec{
            Venda.executeUpdate('delete Venda')
            Venda.executeUpdate('delete ShoppingCart')
            Venda.executeUpdate('delete ItemVenda')
            Venda.executeUpdate('delete Estoque')
            Venda.executeUpdate('delete Produto')

            def produto1 = new Produto(descricao: 'Produto 1', nome: 'P1', tipoUnitario: 'un', unidades: ['un'] as List)
            produto1.save(flush: true)

            def produto2 = new Produto(descricao: 'Produto 2', nome: 'P2', tipoUnitario: 'un', unidades: ['un'] as List)
            produto2.save(flush: true)

            def e1 = new Estoque(produto: produto1, unidade: 'un', quantidade: 10)
            e1.save(flush: true)

            def e2 = new Estoque(produto: produto2, unidade: 'un', quantidade: 5)
            e2.save(flush: true)

            assert 2 == Estoque.count()

            new Uf(nome: 'Piaui', sigla: 'PI').save(flush: true)
            new Cidade(nome: 'Teresina', uf: Uf.piaui).save(flush: true)

            assert Uf.piaui != null
            assert Cidade.teresina != null
            assert 0 == Venda.count()
            return true
        }
    }

    def "Consultar SyncController sem parametros: Não deve retornar porra nenhuma "() {
        when: ' requisitar do SyncController '
            RestResponse response = rest.get("http://localhost:8080/arrasa-amiga/sync") {
                accept 'application/json'
                header 'Authorization', 'Bearer ' + authToken
            }
        then:
            SC_NO_CONTENT == response.status
    }

    def "Verificando o estado original do SyncController enviando timestamps zerados"() {
        given: 'checando o SynController sem nenhuma Venda feita e 2 estoques cadastrados'
            def queryString = buildQueryString('vendaLastUpdated': 0,'estoqueLastUpdated':0)

        when: ' requisitar do SyncController '
            RestResponse response = rest.get("http://localhost:8080/arrasa-amiga/sync?${queryString}") {
                accept 'application/json'
                header 'Authorization', 'Bearer ' + authToken
            }
        then:
            SC_OK == response.status
            def json = response.json
            json.vendas.size() == 0
            json.estoques.size() == 2
    }

    void "Consultar SyncController informando timestamp do ultimo estoque salvo"() {

        given: 'checando o SynController sem nenhuma Venda feita e com timestamp do ultimo estoque cadastrado'
            long lastUpdated = remote.exec{  Estoque.last().lastUpdated.time }
            def queryString = buildQueryString('vendaLastUpdated': 0,'estoqueLastUpdated':lastUpdated)

        when: 'requisitar do SyncController'
            RestResponse response = rest.get("http://localhost:8080/arrasa-amiga/sync?${queryString}") {
                accept 'application/json'
                header 'Authorization', 'Bearer ' + authToken
            }
        then:
            SC_NO_CONTENT == response.status
    }

    void "Salvar venda já entregue e consultar pelo SyncController"(){
        setup: "cadastrando 2 vendas marcadas como entregue"
            def produto1Id = remote.exec{ Produto.findByNome('P1').id }
            def produto2Id = remote.exec{ Produto.findByNome('P2').id }

            Date ontem = new Date() - 1
            Date amanha = new Date() + 1

            def carrinho = [itens: [[produto: [id: produto1Id], unidade: 'un', quantidade: 1],
                                    [produto: [id: produto2Id], unidade: 'un', quantidade: 1]]]
            String venda1 = [formaPagamento: 'AVista', status: 'Entregue',
                                      carrinho: carrinho,   dataEntrega   : formatarData(ontem),
                                       cliente: [nome: 'Cliente Teste']] as JSON

           RestResponse response = rest.post('http://localhost:8080/arrasa-amiga/api/vendas') {
              accept 'application/json'
              contentType 'application/json'
              header 'Authorization', 'Bearer ' + authToken
              json venda1
           }
           assert SC_CREATED == response.status

           Thread.sleep(1000) // aguardando o hibernate fazer o commit

        and: "cadastrando a 2ª venda"

           String venda2 = [formaPagamento: 'AVista', status: 'Entregue',
                                     carrinho: carrinho,   dataEntrega   : formatarData(amanha),
                                      cliente: [nome: 'Cliente Teste 2']] as JSON
           response = rest.post('http://localhost:8080/arrasa-amiga/api/vendas') {
              accept 'application/json'
              contentType 'application/json'
              header 'Authorization', 'Bearer ' + authToken
              json venda2
           }
           assert SC_CREATED == response.status
           Thread.sleep(1000) // aguardando o hibernate fazer o commit

        and:
            def queryString = buildQueryString('vendaLastUpdated': 0,'estoqueLastUpdated':0)

        when: "sync controller requisitado com timestamp zerado"
            response = rest.get("http://localhost:8080/arrasa-amiga/sync?${queryString}") {
                accept 'application/json'
                header 'Authorization', 'Bearer ' + authToken
            }

        then:"o syncontroller nao vai retornar nenhuma venda, pq so traz vendas ainda não entregues"
            SC_OK == response.status
            def json = response.json
            json.vendas.size() == 0
            json.estoques.size() == 2
    }


    void "Salvar venda e consultar SyncController"(){
        setup:
            def queryString = ''
            def produto1Id = remote.exec{ Produto.findByNome('P1').id }
            def produto2Id = remote.exec{ Produto.findByNome('P2').id }

            long estoque1TimestampBeforeNewOrder = remote.exec{
                def produto1 = Produto.findByNome('P1')
                return Estoque.findByProduto(produto1).lastUpdated.time
            }

            long estoque2TimestampBeforeNewOrder = remote.exec{
                def produto2 = Produto.findByNome('P2')
                return Estoque.findByProduto(produto2).lastUpdated.time
            }
        when:"cadastrando 1ª venda"
            def carrinho = [itens: [[produto: [id: produto1Id], unidade: 'un', quantidade: 2],
                                    [produto: [id: produto2Id], unidade: 'un', quantidade: 4]]]

            Date amanha = new Date() + 1
            String venda1 = [formaPagamento: 'AVista', status: 'AguardandoPagamento',
                                      carrinho: carrinho,   dataEntrega   : formatarData(amanha),
                                       cliente: [nome: 'Cliente Teste']] as JSON

           // problemas do timestamp que o mysql não
           // armazena milisegundos
            Thread.sleep(1000)
            RestResponse response = rest.post('http://localhost:8080/arrasa-amiga/api/vendas') {
               accept 'application/json'
               contentType 'application/json'
               header 'Authorization', 'Bearer ' + authToken
               json venda1
            }

        then: 'Venda criada com sucesso'
            SC_CREATED == response.status
            def jsonVenda1 = response.json
            long lastUpdated = jsonVenda1.last_updated
            long idVenda = jsonVenda1.id
            Thread.sleep(500) // aguardando o hibernate concluir o commit da venda anterior

        when: "SyncController deve retornar venda previamente cadastrada e os estoques com os timestamps atualizados"
            queryString = buildQueryString('vendaLastUpdated':0,'estoqueLastUpdated':0)
            response = rest.get("http://localhost:8080/arrasa-amiga/sync?${queryString}") {
                accept 'application/json'
                header 'Authorization', 'Bearer ' + authToken
            }
        then:
            SC_OK == response.status
            def jsonSyncController = response.json
            def vendas = jsonSyncController.vendas
            def estoques = jsonSyncController.estoques

            vendas.size() == 1
            lastUpdated == vendas[0].last_updated
            idVenda == vendas[0].id

            estoques.size() ==  2
            def estoque1 = estoques.find{it['produto_id'] == produto1Id}
            estoque1.last_updated > estoque1TimestampBeforeNewOrder

            def estoque2 = estoques.find{it['produto_id'] == produto2Id}
            estoque2.last_updated > estoque2TimestampBeforeNewOrder

        when: "cadastrando a 2ª venda"
            def carrinho2 = [itens: [ [produto: [id: produto2Id], unidade: 'un', quantidade: 1]]]
            String venda2 = [formaPagamento: 'AVista', status: 'AguardandoPagamento', carrinho: carrinho2,
                                       dataEntrega   : formatarData(amanha), cliente: [nome: 'Cliente Teste']] as JSON

            // problemas do timestamp que o mysql não
            // armazena milisegundos
            Thread.sleep(1000)
            response = rest.post('http://localhost:8080/arrasa-amiga/api/vendas') {
               accept 'application/json'
               contentType 'application/json'
               header 'Authorization', 'Bearer ' + authToken
               json venda2
           }
        then:
            SC_CREATED == response.status
            2 == remote.exec{ Venda.count() }
            8 == remote.exec{ Estoque.findByProdutoAndUnidade(Produto.findByNome('P1'), 'un').quantidade }
            0 == remote.exec{ Estoque.findByProdutoAndUnidade(Produto.findByNome('P2'), 'un').quantidade }

            def jsonVenda2 = response.json
            long lastUpdatedVenda2 = jsonVenda2.last_updated
            long idVenda2 = jsonVenda2.id

        when: 'Cosultando novamente o synccontroller com o timestamp da 1ª venda'
            queryString = buildQueryString('vendaLastUpdated': lastUpdated,'estoqueLastUpdated':0)
            response = rest.get("http://localhost:8080/arrasa-amiga/sync?${queryString}") {
                accept 'application/json'
                header 'Authorization', 'Bearer ' + authToken
            }

        then:
            SC_OK == response.status

            def json2 = response.json
            def _vendas = json2.vendas
            def _estoques = json2.estoques

            _vendas.size() == 1
            idVenda2 == _vendas[0].id
            lastUpdatedVenda2 == _vendas[0].last_updated

        when: 'Cosultando novamente o synccontroller sem o timestamps'
            queryString = buildQueryString('vendaLastUpdated': 0,'estoqueLastUpdated':0)
            response = rest.get("http://localhost:8080/arrasa-amiga/sync?${queryString}") {
                accept 'application/json'
                header 'Authorization', 'Bearer ' + authToken
            }

        then: 'Deve encontrar as 2 vendas cadastradas'
            SC_OK == response.status
            def json3 = response.json
            json3.vendas.size() == 2
    }

    def buildQueryString(Map params){
        return params.collect { k, v -> "$k=$v" }.join(/&/)
    }

    protected String formatarData(Date data) {
        // utilizando a formatacao padrao definida em Config.groovy
        // na propriedade  grails.databinding.dateFormats = ['MMddyyyy']
        return data.format('MMddyyyy')
    }
}
