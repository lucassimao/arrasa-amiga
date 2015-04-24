package br.com.arrasaamiga.excecoes

import br.com.arrasaamiga.Produto

/**
 * Created by lucas.simao on 29/04/2015.
 */
class EstoqueException extends RuntimeException {

    Produto produto
    String unidade

    EstoqueException(String msg, Produto produto, String unidade) {
        super(msg)
        this.produto = produto
        this.unidade = unidade
    }
}
