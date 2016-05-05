package br.com.arrasaamiga

class Estoque {

    Produto produto
    String unidade
    int quantidade
    Date dateCreated, lastUpdated
    List entradas

    static belongsTo = Produto
    static hasMany = [entradas: EntradaEstoque]

    transient def gcmService

    static mapping = {
        autoTimestamp true
        entradas cascade: 'all-delete-orphan'
        unidade sqlType: 'varchar(200) BINARY' //TODO dar um jeito de implementar comparação case sensitive das unidades
    }

    static constraints = {
        produto(nullable: false)
        unidade(nullable: false, blank: false)
        quantidade(min: 0)
    }

    def beforeUpdate(){
        notificarAtualizacaoEstoque()
    }

    protected void notificarAtualizacaoEstoque(){
        if (isDirty('quantidade')){
            int oldQuantidade = getPersistentValue('quantidade')
            gcmService.notificarAtualizacaoEstoque(oldQuantidade,quantidade,unidade,produto)
        }
    }
}
