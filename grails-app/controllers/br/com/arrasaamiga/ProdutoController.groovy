package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured
import grails.converters.*
 
class ProdutoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    
    def grailsApplication
    def springSecurityService


    def addNewUnidade(String unidade){
        render(template:'addNewUnidade',model:[unidade:unidade])
    }

    def addNewFoto(String nomeArquivo){
        render(template:'addNewFoto',model:[foto:nomeArquivo])
    }

    def asyncFotoUpload(){

        String uploadDir =  grailsApplication.mainContext.getResource('img/produtos').file.absolutePath 
        def multipartFile = request.getFile('foto')
        def originalFilename = "img${System.currentTimeMillis()}${multipartFile.originalFilename}"

        if (originalFilename){
            multipartFile.transferTo(new File(uploadDir + File.separator + originalFilename))
        }


        render(template:'addNewFoto',model:[foto:originalFilename])
    }


    @Secured(['ROLE_ADMIN'])
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(['ROLE_ADMIN'])
    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        
        [produtoInstanceList: Produto.list(params), produtoInstanceTotal: Produto.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        [produtoInstance: new Produto(params)]
    }

    @Secured(['ROLE_ADMIN'])
    def save() {

        params.precoAVistaEmReais = params.precoAVistaEmReais.replace('.',',')
        params.precoAPrazoEmReais = params.precoAPrazoEmReais.replace('.',',')
        
        def unidades = JSON.parse(params.unidades)
        def fotos = JSON.parse(params.fotosUnidades)
        def comentarios = JSON.parse(params.fotoComentario)

        def produtoInstance = new Produto(params)
        produtoInstance.fotos = []
        produtoInstance.unidades = []

        unidades.each{ unidade->
            produtoInstance.addToUnidades(unidade.trim())

            fotos[unidade].eachWithIndex{ foto, index ->
                def fotoProduto = new FotoProduto();
                
                fotoProduto.comentario = comentarios[foto]?.trim();
                fotoProduto.posicao = index
                fotoProduto.unidade = unidade.trim()
                fotoProduto.arquivo = foto.trim()

                produtoInstance.addToFotos(fotoProduto)
            }
        }
        

        def multipartFileMiniatura = request.getFile('fotoMiniatura')
        produtoInstance.fotoMiniatura = "img${System.currentTimeMillis()}${multipartFileMiniatura.originalFilename}"

        if (!produtoInstance.save(flush: true)) {
            produtoInstance.fotoMiniatura = ''
            produtoInstance.fotos = []
            render(view: "create", model: [produtoInstance: produtoInstance])
            return
        }

        String uploadDir =  grailsApplication.mainContext.getResource('img/produtos').file.absolutePath 
        
        if (multipartFileMiniatura.originalFilename)
            multipartFileMiniatura.transferTo(new File(uploadDir + File.separator + produtoInstance.fotoMiniatura))


       produtoInstance.unidades.each{ un->

            def estoque = new Estoque()

            estoque.produto = produtoInstance
            estoque.unidade = un
            estoque.quantidade = 0 // qtde inicial

            estoque.save()

        }


        flash.message = "Produto cadastrado"
        redirect(action: "show", id: produtoInstance.id)
        
    }

    //Apresenta detalhes do produto ao administrador
    @Secured(['ROLE_ADMIN'])
    def show(Long id) {
        def produtoInstance = Produto.get(id)
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
            return
        }

        [produtoInstance: produtoInstance]
    }



    @Secured(['ROLE_ADMIN'])
    def edit(Long id) {
        def produtoInstance = Produto.get(id)
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
            return
        }

        [produtoInstance: produtoInstance]
    }



    @Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
        def produtoInstance = Produto.get(id)

        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (produtoInstance.version > version) {
                produtoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'produto.label', default: 'Produto')] as Object[],
                          "Another user has updated this Produto while you were editing")
                render(view: "edit", model: [produtoInstance: produtoInstance])
                return
            }
        }


        
        produtoInstance.nome = params.nome
        produtoInstance.descricao = params.descricao
        produtoInstance.tipoUnitario = params.tipoUnitario
        produtoInstance.precoAPrazoEmReais = Double.valueOf( params.precoAPrazoEmReais.replace(',','.') )
        produtoInstance.precoAVistaEmReais = Double.valueOf( params.precoAVistaEmReais.replace(',','.') )

        
        def miniaturaAnterior = produtoInstance.fotoMiniatura
        def multipartFileMiniatura = request.getFile('fotoMiniatura')

        if (multipartFileMiniatura.originalFilename)
            produtoInstance.fotoMiniatura =  multipartFileMiniatura.originalFilename
        else
             produtoInstance.fotoMiniatura = miniaturaAnterior


        produtoInstance.fotos*.delete()
        produtoInstance.fotos.clear()
        produtoInstance.unidades.clear()

        def unidades = JSON.parse(params.unidades)
        def fotos = JSON.parse(params.fotosUnidades)
        def comentarios = JSON.parse(params.fotoComentario)

        unidades.each{ unidade -> 


            if (!Estoque.findByProdutoAndUnidade( produtoInstance, unidade ) ){
                
                def estoque = new Estoque()

                estoque.produto = produtoInstance
                estoque.unidade = unidade
                estoque.quantidade = 0 // qtde inicial

                estoque.save()
            }

            produtoInstance.addToUnidades(unidade)

            fotos[unidade].eachWithIndex{ arquivoFoto, index ->

                def fotoProduto = new FotoProduto()
                
                fotoProduto.comentario = comentarios[arquivoFoto]?.trim()
                fotoProduto.posicao = index
                fotoProduto.unidade = unidade
                fotoProduto.arquivo = arquivoFoto    

                produtoInstance.addToFotos(fotoProduto)

            }

        }

        if (!produtoInstance.save(flush: true)) {
            render(view: "edit", model: [produtoInstance: produtoInstance])
            return
        }

         
        if (multipartFileMiniatura.originalFilename){

            String uploadDir =  grailsApplication.mainContext.getResource('img/produtos').file.absolutePath 
            multipartFileMiniatura.transferTo(new File(uploadDir + File.separator + produtoInstance.fotoMiniatura))
        }

        flash.message = "Produto atualizado"
        redirect(action: "show", id: produtoInstance.id)

    }

    @Secured(['ROLE_ADMIN'])
    def delete(Long id) {
        def produtoInstance = Produto.get(id)
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
            return
        }

        try {
            produtoInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "show", id: id)
        }
    }



    //Apresenta detalhes do produto ao comprador
    def detalhes(Long id) {
        def produtoInstance = Produto.get(id)
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)
        
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(uri: "/", absolute:true)
            return
        }

        // procurando a unidade padrao a ser selecionada na interface: a primeira que tiver estoque
        String unidadeComEstoque = null

        for(String unid : produtoInstance.unidades){
            if (produtoInstance.getQuantidadeEmEstoque(unid) > 0){
                unidadeComEstoque = unid
                break
            }
        } 

        [produtoInstance: produtoInstance,estoques: produtoInstance.getEstoques(),
            unidadeComEstoque: unidadeComEstoque, cliente:cliente]
    }

    def quantidadeEmEstoque(Long produtoId,String unidade) {

        def produtoInstance = Produto.get(produtoId)
        
        if (!produtoInstance) {
            render "erro"
            throw new Exception("Erro ao carregar produto com id ${produtoId} em ProdutoController#quantidadeEmEstoque")
        }

        if (!unidade){
            render "erro"
            throw new Exception("Alguem tentanto carregar o produto ${produtoId} sem unidade ....")
        }

        if (!produtoInstance.unidades.contains(unidade)){
            render "erro"
            throw new Exception("Não existe a unidade ${params.unidade} no produto ${id} ....")
        }

        try{
            def quantidade = produtoInstance.getQuantidadeEmEstoque(unidade)

            boolean marcadoParaAvisar = false

            if (quantidade == 0){
                def user = springSecurityService.currentUser
                def cliente = Cliente.findByUsuario(user)
                
                marcadoParaAvisar = ( Aviso.findByProdutoAndClienteAndUnidade(produtoInstance,cliente,unidade) != null )
            }

            render (['quantidade': quantidade, 'marcadoParaAvisar': marcadoParaAvisar] as JSON)

        }catch(Exception e){
            render "erro"
            throw e
        }
       
         
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def avisar(Long id){
        def produto = Produto.get(id)

        if (!produto){
            println "Alguem tentanto carregar o produto ${id} ...."
            redirect(uri:'/', absolute:true)
            return
        }

        String unidade = params.un

        if (!unidade){
            println "Alguem tentanto carregar o produto ${id} sem unidade ...."
            redirect(uri:'/', absolute:true)
            return            
        }

        if (!produto.unidades.contains(unidade)){
            println "Não existe a unidade ${params.unidade} no produto ${id} ...."
            redirect(uri:'/', absolute:true)
            return            
        }


        def user = springSecurityService.currentUser

        def aviso = new Aviso()
        aviso.cliente = Cliente.findByUsuario(user)
        aviso.produto = produto
        aviso.unidade = unidade

        aviso.save()
    
        flash.info = 'Amiga, avisaremos você assim que novas unidades chegarem !!'
        redirect(action:'detalhes',id:produto.id)

    }
}
