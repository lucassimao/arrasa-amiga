package br.com.arrasaamiga

import grails.plugins.springsecurity.Secured

class VendaController {


        @Secured(['IS_AUTHENTICATED_FULLY'])
        def show(Long id){

                def venda = Venda.get(id)

                if (venda){
                        [	numeroPedido: String.format("%05d", venda.id) , venda : venda,
                        enderecoEntrega: venda.cliente.endereco,itens: venda.itensVenda,
                        cliente: venda.cliente, subTotal: venda.subTotalItensEmReais, 
                        detalhesPagamento: venda.detalhesPagamento, frete: venda.freteEmReais,desconto: venda.descontoEmReais,
                        valorTotal:venda.valorTotal,
                        dataEntrega: venda.dataEntrega]

                }else{
                        println "Erro ao carregar venda ${id}"
                        render 'Venda não encontrada'
                }


        }

        @Secured(['IS_AUTHENTICATED_FULLY'])
        def showFull(Long id){

                def venda = Venda.get(id)

                if (venda){
                        [       numeroPedido: String.format("%05d", venda.id) , venda : venda,
                        enderecoEntrega: venda.cliente.endereco,itens: venda.itensVenda?:venda.carrinho.itens,
                        cliente: venda.cliente, subTotal: venda.subTotalItensEmReais, 
                        detalhesPagamento: venda.detalhesPagamento, frete: venda.freteEmReais,desconto: venda.descontoEmReais,
                        valorTotal:venda.valorTotal,
                        dataEntrega: venda.dataEntrega]

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



        def cancelada(){

        }

        @Secured(['IS_AUTHENTICATED_FULLY'])
        def index() {
                redirect(action: "list", params: params)
        }

        @Secured(['IS_AUTHENTICATED_FULLY'])
        def list(Integer max) {
                params.max = Math.min(max ?: 10, 100)
                [vendaInstanceList: Venda.list(params), vendaInstanceTotal: Venda.count()]
        }


}
