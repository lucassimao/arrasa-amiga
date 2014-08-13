package br.com.arrasaamiga

class Banner {

    String arquivo
    Date dateCreated
    String link
    String titulo
    String comentario
    boolean visivel=true

    static constraints = {
        arquivo nullable: false, blank: false
        link nullable: true,blank: true
        titulo nullable: false,blank: false
        comentario nullable: false,blank:false

    }
}
