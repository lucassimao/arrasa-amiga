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
    static transients = ['ancestrais','grupoRaiz','descendentes']


    public String toString(){
        return nome
    }

    /**
     * retorna a hierarquia de 
     * grupos, do raiz ao pai do grupo atual
     */
    public List getAncestrais(){
        List lst = []
        if (this.pai)
            lst += [this.pai] + this.pai.ancestrais 
        else 
            return []

        return lst.reverse()
    }

    /**
     *   Retorna o grupo raiz a que este grupo atual pertence.
     *   Caso ele mesmo seja um grupo raiz, retorna a si mesmo
     */
    public GrupoDeProduto getGrupoRaiz(){
        if (this.pai != null)
            return this.pai.getGrupoRaiz()
        else    
            return this
    }

    /**
     * Esse método retorna todos os grupos
     * descentes do atual grupo. O método 
     * getSubGrupos retorna apenas os grupos filhos do grupo atual
     */ 
    public List getDescendentes(){
        List lt = []
       
        if (this.subGrupos){
            this.subGrupos.each{ subGrupo->
                lt += [subGrupo.id] + subGrupo.descendentes
            } 
        }else{
            return []
        }
       
       return lt
    }



}
