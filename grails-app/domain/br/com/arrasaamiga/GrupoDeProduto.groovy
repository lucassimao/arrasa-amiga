package br.com.arrasaamiga

class GrupoDeProduto {

    String nome
    GrupoDeProduto pai

    static hasMany = [subGrupos : GrupoDeProduto]


    static constraints = {
    	nome(nullable:false,blank:false)
        pai(nullable:true)
    }

    static mappedBy = [ subGrupos: "pai" ]


    public String toString(){
        return nome
    }


}
