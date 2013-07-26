package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.*
import com.metasieve.shoppingcart.Shoppable


class ShoppingCartController {

	def shoppingCartService

    def index(){
    	def total = 0


    	shoppingCartService.getItems().each{item ->
    		def produto = Shoppable.findByShoppingItem(item)
    		total += produto.precoEmReais * shoppingCartService.getQuantity(produto)
    	}

    	['valorTotal':total]

    }

    def add1(Long id){
    	def produtoInstance = Produto.get(id)
    	produtoInstance.addQuantityToShoppingCart(1)

    	if (shoppingCartService.getQuantity(produtoInstance) == 1){
    		flash.message = "${produtoInstance.nome} adicionado(a) ao seu carrinho de compras"
    	}else{
    		flash.message = "Mais 1 ${produtoInstance.nome} adicionado(a) ao seu carrinho de compras"
    	}
    	
    	redirect(action: "index")
    }

    def add(Long id,Integer quantidade){
    	def produtoInstance = Produto.get(id)
    	produtoInstance.addQuantityToShoppingCart(quantidade)

    	if (shoppingCartService.getQuantity(produtoInstance) == 1){
    		flash.message = "${produtoInstance.nome} adicionado(a) ao seu carrinho de compras"
    	}else{
    		flash.message = "Mais ${quantidade} ${produtoInstance.nome} adicionado(a) ao seu carrinho de compras"
    	}
    	
    	redirect(action: "index")
    }


    def remover1(Long id){
    	def produtoInstance = Produto.get(id)
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
}
