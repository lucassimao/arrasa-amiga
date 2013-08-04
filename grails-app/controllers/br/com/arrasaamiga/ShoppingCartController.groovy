package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.*
import com.metasieve.shoppingcart.Shoppable
import grails.plugins.springsecurity.Secured

class ShoppingCartController {

	def shoppingCartService
    def springSecurityService

    private Double somarValorTotalDoCarrinho(){
        double total = 0
        
        shoppingCartService.getItems().each{item ->
            def produto = Shoppable.findByShoppingItem(item)
            total += produto.precoEmReais * shoppingCartService.getQuantity(produto)
        }

        return total

    }

    def index(){
    	def total = somarValorTotalDoCarrinho()

    	['valorTotal':total]

    }

    def add1(Long id){
    	def produtoInstance = Produto.get(id)
        def qtdeAnterior = shoppingCartService.getQuantity(produtoInstance)

    	produtoInstance.addQuantityToShoppingCart(1)

    	if (qtdeAnterior == null || qtdeAnterior == 0 ){
    		flash.message = "${produtoInstance.nome} adicionado(a) ao seu carrinho de compras"
    	}else{
    		flash.message = "Mais 1 ${produtoInstance.nome} adicionado(a) ao seu carrinho de compras"
    	}
    	
    	redirect(action: "index")
    }

    def add(Long id,Integer quantidade){
    	def produtoInstance = Produto.get(id)
        def qtdeAnterior = shoppingCartService.getQuantity(produtoInstance)


    	produtoInstance.addQuantityToShoppingCart(quantidade)

    	if (qtdeAnterior == null || qtdeAnterior == 0 ){
    		flash.message = " ${quantidade} ${produtoInstance.nome} adicionados(as) ao seu carrinho de compras"
    	}else{
    		flash.message = "Mais ${quantidade} ${produtoInstance.nome} adicionados(as) ao seu carrinho de compras"
    	}
    	
    	redirect(action: "index")
    }


    def remover1(Long id){
    	def produtoInstance = Produto.get(id)
        def qtdeAnterior = shoppingCartService.getQuantity(produtoInstance)

    	produtoInstance.removeQuantityFromShoppingCart(1)
    	
    	if (shoppingCartService.getQuantity(produtoInstance) == 0){
    		flash.message = "${produtoInstance.nome} removido(a) do seu carrinho de compras"
    	}else{
    		flash.message = "1 ${produtoInstance.nome} removido(a) do seu carrinho de compras"
    	}
    	
    	redirect(action: "index")
    }

    def removerProduto(Long id){
    	def produtoInstance = Produto.get(id)
    	produtoInstance.removeQuantityFromShoppingCart(shoppingCartService.getQuantity(produtoInstance))
    	flash.message = "${produtoInstance.nome} removido(a) do seu carrinho de compras"
    	redirect(action: "index")
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def checkout(){
        def frete = 25
        def total = somarValorTotalDoCarrinho() + frete
        def cliente = Cliente.findByUsuario(springSecurityService.currentUser)

        ['valorTotal':total,'enderecoEntrega': cliente.endereco, frete: frete]
    }
}
