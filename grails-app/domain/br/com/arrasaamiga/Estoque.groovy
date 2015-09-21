package br.com.arrasaamiga

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
