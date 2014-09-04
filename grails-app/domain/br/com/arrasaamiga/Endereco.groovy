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

    public void setCidade(Cidade cidade) {

        switch (cidade?.id) {
            case Cidade.teresina?.id:
                this.cep = '64000-001'
                break
        }

        this.@cidade = cidade
    }


    public void setCep(String _cep) {

        switch (cidade?.id) {
            case Cidade.teresina?.id:
                this.@cep = '64000-001'
                break
            default:
                this.@cep = _cep
        }

    }


}
