package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured

 
class ProdutoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    
    def grailsApplication
    def springSecurityService



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
        params.unidades = params.unidades.split(',')?.collect{un-> un.trim() }


        def produtoInstance = new Produto(params)
        produtoInstance.fotos = []

        def multipartFileMiniatura = request.getFile('fotoMiniatura')
        produtoInstance.fotoMiniatura =  multipartFileMiniatura.originalFilename
        
        def multipartFiles = []

        params.keySet().each{ param->

            if (param.startsWith('fotos')){

                def multipartFile = request.getFile(param)
                def originalFilename = multipartFile.originalFilename

                if (originalFilename){
                    produtoInstance.fotos << originalFilename
                    multipartFiles << multipartFile
                }

            }
        }

        if (!produtoInstance.save(flush: true)) {
            produtoInstance.fotoMiniatura = ''
            produtoInstance.fotos = []
            render(view: "create", model: [produtoInstance: produtoInstance])
            return
        }

        String uploadDir =  grailsApplication.mainContext.getResource('img/produtos').file.absolutePath 
        
        multipartFiles.each{
            it.transferTo(new File(uploadDir + File.separator + it.originalFilename))
        }

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

    //Apresenta detalhes do produto ao comprador
    def detalhes(Long id) {
        def produtoInstance = Produto.get(id)
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)
        
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(view: "/index")
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

        [produtoInstance: produtoInstance,estoques: produtoInstance.getEstoques(),unidadeComEstoque: unidadeComEstoque, cliente:cliente]
    }

    def quantidadeEmEstoque(Long produtoId,String unidade) {

        def produtoInstance = Produto.get(produtoId)
        
        if (!produtoInstance) {
            render "erro"
            throw new Exception("Erro ao carregar produto com id ${produtoId}")
        }

        try{
            def quantidade = produtoInstance.getQuantidadeEmEstoque(unidade)
            
            render quantidade

        }catch(Exception e){
            render "erro"
            throw e
        }
       
         
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

        


        params.precoAVistaEmReais = params.precoAVistaEmReais.replace('.',',')
        params.precoAPrazoEmReais = params.precoAPrazoEmReais.replace('.',',')
        params.unidades = params.unidades.split(",")
        params.remove('')

        def miniaturaAnterior = produtoInstance.fotoMiniatura

        produtoInstance.properties = params


        def multipartFileMiniatura = request.getFile('fotoMiniatura')
        if (multipartFileMiniatura.originalFilename)
            produtoInstance.fotoMiniatura =  multipartFileMiniatura.originalFilename
        else
             produtoInstance.fotoMiniatura = miniaturaAnterior
             
        
        def multipartFiles = []

        params.keySet().each{ param->

            if (param.startsWith('fotos')){

                def multipartFile = request.getFile(param)
                def originalFilename = multipartFile.originalFilename

                if (originalFilename){
                    produtoInstance.fotos <<  originalFilename
                    multipartFiles << multipartFile
                }

            }
        }

        // cria um novo estoque para novas unidades
        
        produtoInstance.unidades.each{ un->

            if (!Estoque.findByProdutoAndUnidade(produtoInstance,un) ){
                def estoque = new Estoque()

                estoque.produto = produtoInstance
                estoque.unidade = un
                estoque.quantidade = 0 // qtde inicial

                estoque.save()               
            }

        }

        String uploadDir =  grailsApplication.mainContext.getResource('img/produtos').file.absolutePath 

        // atualizando o produto

        if (!produtoInstance.save(flush: true)) {
            render(view: "edit", model: [produtoInstance: produtoInstance])
            produtoInstance.fotoMiniatura = ''
            produtoInstance.fotos = []
            return
        }

        // aqui o produto foi atualizado com sucesso. Assim ele pode salvar as imagens no disco caso necessario

        multipartFiles.each{
            it.transferTo(new File(uploadDir + File.separator  + it.originalFilename))
        }

        if (multipartFileMiniatura.originalFilename)
            multipartFileMiniatura.transferTo(new File(uploadDir + File.separator + produtoInstance.fotoMiniatura))

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
}
