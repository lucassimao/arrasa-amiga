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
    static transients = ['ancestrais']


    public String toString(){
        return nome
    }

    public List getAncestrais(){
        List lst = []
        if (this.pai)
            lst += [this.pai] + this.pai.ancestrais 
        else 
            return []

        return lst.reverse()
    }



}
