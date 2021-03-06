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
        if (config.useGcmService)
            gcmService.notificarAtualizacao(lastUpdated.time,this)
    }

    def beforeUpdate(){
        def config = Holders.config
        if (config.useGcmService && validate())
            notificarAtualizacaoEstoque()
    }

    def afterInsert(){
        def config = Holders.config
        if (config.useGcmService)
            gcmService.notificarAtualizacao(lastUpdated.time,this)
    }

    protected void notificarAtualizacaoEstoque(){
        if (isDirty('quantidade')){
            int oldQuantidade = getPersistentValue('quantidade')
            gcmService.notificarAtualizacaoEstoque(oldQuantidade,quantidade,unidade,produto)
        }
    }

    def setLastUpdated(Date dt){
        if (dt){
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.set(Calendar.MILLISECOND, 0);
            this.lastUpdated = c.time
        } else
            this.lastUpdated=null
    }
}
