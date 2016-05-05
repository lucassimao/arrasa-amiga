package br.com.arrasaamiga

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.JSON
import groovy.xml.*
import java.text.NumberFormat

class GcmService {

    boolean transactional = false

    def springSecurityService
    final String url = 'https://gcm-http.googleapis.com/gcm/send'
    final String authHeader = 'key=AIzaSyBfpFMqawpVu1Uj3NqXSc3a3-S3acTHMCY'

    def notificarNovoAnexo(Venda venda,String anexo) {
        def http = new HTTPBuilder(url)
        def login = springSecurityService.authentication.name
        String message = "Anexo ${anexo} adicionado na Venda #${venda.id} por ${login}"

        http.request(POST, JSON) { req ->
            uri.path = '/gcm/send'
            headers.'Authorization' = authHeader
            body =  [data: [resumo:'Novo anexo adicionado', message:message],
                    to :'/topics/admin']

            response.success = { resp, json ->
                assert resp.status == 200
                println json
            }
        }
    }

    def notificarNovoProduto(Produto produto){
        def http = new HTTPBuilder(url)
        String message = "${produto.nome} acaba de ser cadastrado."
        message +=" Valores: R\$ ${produto.precoAVistaEmReais} a vista e"
        message +=" R\$ ${produto.precoAPrazoEmReais} no cartão"

        http.request(POST, JSON) { req ->
            uri.path = '/gcm/send'
            headers.'Authorization' = authHeader
            body =  [data: [resumo:'Novo produto cadastrado', message:message],
                    to :'/topics/all']

            response.success = { resp, json ->
                assert resp.status == 200
                println json
            }
        }
    }

    def notificarAtualizacaoEstoque(int qtdeAnterior,int novaQtde,String unidade,Produto produto){
        def http = new HTTPBuilder(url)
        String message
        if (novaQtde > 0 && qtdeAnterior==0){
            message = "${produto.nome}"
            if (produto.isMultiUnidade())
                message += "(${unidade})"
            message += " acaba de receber ${novaQtde} unidades no estoque"
        } else if (novaQtde == 0){
            message = "${produto.nome}"
            if (produto.isMultiUnidade())
                message += "(${unidade})"
            message += " acabou de esgotar no estoque!"
        }

        if (message)
            http.request(POST, JSON) { req ->
                uri.path = '/gcm/send'
                headers.'Authorization' = authHeader
                body =  [data: [resumo:'Atualização de estoque', message:message],
                        to :'/topics/all']

                response.success = { resp, json ->
                    assert resp.status == 200
                    println json
                }
            }
    }


    def relembrarVendedorDeClienteIrBuscarProduto(Collection vendasParaBuscar){
        def http = new HTTPBuilder(url)

        Map<String,Set<Venda>> vendasPorVendedores = [:]
        vendasParaBuscar.each{Venda v->
            String loginVendedor = v.vendedor.username.replace('@','%')
            if (!vendasPorVendedores[loginVendedor])
                vendasPorVendedores[loginVendedor] = []

            vendasPorVendedores[loginVendedor] << v
        }

        log.debug("vendas por vendedores: ${vendasPorVendedores}")

        vendasPorVendedores.each{ loginVendedor,vendas ->
            int qtde = vendas.size()
            String resumo = ''
            if (qtde > 1)
                resumo = "${qtde} clientes sua ainda não foram buscar!"
            else
                resumo = "${qtde} cliente sua ainda nao foi buscar!"

            String details = "As seguintes clientes ainda não foram buscar: "
            details += vendas.collect{ v-> v.cliente.nome }.join(',')

            String topico = "/topics/${loginVendedor}"

            http.request(POST, JSON) { req ->
                uri.path = '/gcm/send'
                headers.'Authorization' = authHeader
                body =  [data: [resumo:resumo, message:details],
                        to :topico]

                response.success = { resp, json ->
                    assert resp.status == 200
                    println json
                }
            }
        }

    }
}
