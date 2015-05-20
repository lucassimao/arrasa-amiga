package br.com.arrasaamiga

import br.com.arrasaamiga.excecoes.EstoqueException
import org.apache.commons.logging.LogFactory

class Estoque {

    Produto produto
    String unidade
    int quantidade

    List entradas

    static belongsTo = Produto
    static hasMany = [entradas: EntradaEstoque]

    static mapping = {
        entradas cascade: 'all-delete-orphan'
        unidade sqlType: 'varchar(200) BINARY' //TODO dar um jeito de implementar comparação case sensitive das unidades
    }

    static constraints = {
        produto(nullable: false)
        unidade(nullable: false, blank: false)
        quantidade(min: 0)
    }

}
