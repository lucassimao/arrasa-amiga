package br.com.arrasaamiga

class Endereco {

    String cep
    String complemento
    String bairro
    Cidade cidade
    Uf uf

    static constraints = {
        cep(blank: true, nullable: true)
        complemento(blank: true, nullable: true)
        bairro(blank: true, nullable: true)
        cidade(blank: true, nullable: true)
        uf(nullable: true)
    }


}
