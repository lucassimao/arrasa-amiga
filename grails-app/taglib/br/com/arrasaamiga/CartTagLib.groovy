package br.com.arrasaamiga

class CartTagLib {

    static namespace = 'cart'

    def shoppingCartService

    def qtdeTotalItens = { attrs, body ->
        try {

            def cart = shoppingCartService.shoppingCart
            def qtde = 0

            if (cart?.itens) {
                qtde = cart.itens.sum { c -> c.quantidade }
            }

            out << qtde

        } catch (Exception e) {
            e.printStackTrace()
            return 0
        }

    }

}
