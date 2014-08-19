package br.com.arrasaamiga

class CartTagLib {

    static namespace = 'cart'

    def qtdeTotalItens = { attrs, body ->

        def cart = session.shoppingCart
        def qtde = 0

        if (cart?.itens) {
            qtde = cart.itens.sum { c -> c.quantidade }
        }
        out << qtde

    }

}
