package br.com.arrasaamiga

import grails.util.Holders

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

    def afterUpdate(){
        def config = Holders.config
        if (config.useGcmService){
            notificarAtualizacaoEstoque()
            gcmService.notificarAtualizacao()
        }
    }

    def afterInsert(){
        def config = Holders.config
        if (config.useGcmService)
            gcmService.notificarAtualizacao()
    }

    protected void notificarAtualizacaoEstoque(){
        if (isDirty('quantidade')){
            int oldQuantidade = getPersistentValue('quantidade')
            gcmService.notificarAtualizacaoEstoque(oldQuantidade,quantidade,unidade,produto)
        }
    }
}
