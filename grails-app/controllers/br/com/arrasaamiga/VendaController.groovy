package br.com.arrasaamiga

import grails.plugins.springsecurity.Secured

import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

class VendaController {


        @Secured(['IS_AUTHENTICATED_FULLY'])
        def show(Long id){

                def venda = Venda.get(id)

                if (venda){
                        [numeroPedido: String.format("%05d", venda.id) , venda : venda,
                        enderecoEntrega: venda.cliente.endereco,cliente: venda.cliente]

                }else{
                        println "Erro ao carregar venda ${id}"
                        render 'Venda não encontrada'
                }


        }


        @Secured(['IS_AUTHENTICATED_FULLY'])
        def setTrackingCode(Long id,String trackingCode){
                
                if (trackingCode){
                        def v = Venda.load(id)
                        v.codigoRastreio = trackingCode
                        v.save(flush:true)
                        flash.message = 'Código de rastreio atualizado com sucesso'
                }       

                redirect(action:'list', params:[offset:params.offset, max: params.max] )

        }

        def save(){
                println request.JSON

                /*
                def http = new HTTPBuilder( 'https://android.googleapis.com/gcm/send' )
                http.post( POST, JSON ) {
                        //uri.path = '/ajax/services/search/web'
                        //uri.query = [ v:'1.0', q: 'Calvin and Hobbes' ]

                        headers.'Authorization' = 'AIzaSyB-UkU5kwTKcsSYoWONSBXSEXvPFEP9c-M'
                        headers.'Content-Type' = 'application/json'
                        body = [
                                "registration_ids":['1']
                        ]

                        // response handler for a success response code:
                        response.success = { resp, json ->
                                println resp.statusLine

                        // parse the JSON response object:
                                json.responseData.results.each {
                                        println "  ${it.titleNoFormatting} : ${it.visibleUrl}"
                                }
                        }

                        // handler for any failure status code:
                        response.failure = { resp ->
                                println "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
                        }
                }*/


        }

        @Secured(['IS_AUTHENTICATED_FULLY'])
        def showFull(Long id){

                def venda = Venda.get(id)

                if (venda){
                        [ numeroPedido: String.format("%05d", venda.id) , venda : venda,cliente: venda.cliente]

                }else{
                        println "Erro ao carregar venda ${id}"
                        render 'Venda não encontrada'
                }


        }

        @Secured(['IS_AUTHENTICATED_FULLY'])
        def marcarComoEntregue(Long id){
                def v = Venda.get(id)
                v.status = StatusVenda.Entregue

                v.save(flush:true)

                flash.message = 'A venda foi marcada como entregue'
                
                if (params.offset && params.max){
                        redirect(action:'list', params:[offset:params.offset, max: params.max] )
                }else{
                        redirect(action:'list')
                }
                

        }

        @Secured(['IS_AUTHENTICATED_FULLY'])
        def excluir(Long id){
                def v = Venda.load(id)

                // repõe os itens no estoque somente se a venda tiver sido a vista
                if (v.formaPagamento?.equals(FormaPagamento.AVista)) 
                        Estoque.reporItens(v.itensVenda)
                
                v.delete(flush:true)

                flash.message = 'A venda foi excluida'
                
                if (params.offset && params.max)
                        redirect(action:'list', params:[offset:params.offset, max: params.max] )
                else
                        redirect(action:'list')
                
                
        }



        def cancelada(){

        }

        @Secured(['IS_AUTHENTICATED_FULLY'])
        def index() {
                redirect(action: "list", params: params)
        }

        @Secured(['IS_AUTHENTICATED_FULLY'])
        def list(Integer max) {
                params.max = Math.min(max ?: 10, 100)
                params.sort = 'dateCreated'
                params.order = 'desc'
                [vendaInstanceList: Venda.list(params), vendaInstanceTotal: Venda.count()]
        }


}
