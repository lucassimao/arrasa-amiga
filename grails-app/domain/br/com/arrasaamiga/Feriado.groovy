package br.com.arrasaamiga

class Feriado {

    String descricao
    Date inicio
    Date fim

    static constraints = {
        descricao blank: false, nullable: false
        inicio nullable: false
        fim nullable: false
    }
}
