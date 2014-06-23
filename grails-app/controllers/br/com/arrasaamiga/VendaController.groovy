package br.com.arrasaamiga

import grails.plugins.springsecurity.Secured
import grails.rest.RestfulController
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*



class VendaController extends RestfulController {

    static responseFormats = ['html', 'json']


    VendaController() {
        super(Venda)
    }

    
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def show(Long id){

        def venda = Venda.get(id)

        withFormat{
            html { 
                if (venda){
                    [numeroPedido: String.format("%05d", venda.id) , venda : venda,
                        enderecoEntrega: venda.cliente.endereco,cliente: venda.cliente]

                }else{
                    println "Erro ao carregar venda ${id}"
                    render 'Venda não encontrada'
                }
            }
            json { 
                if (venda)
                    respond venda
                else
                    render status: NOT_FOUND
            }
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

    def index() {
        withFormat{
            html { redirect(action: "list", params: params) }
            json { respond Venda.list() }
        }
        
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = 'dateCreated'
        params.order = 'desc'
        respond Venda.list(params), model:[vendaInstanceTotal: Venda.count()]
    }


}