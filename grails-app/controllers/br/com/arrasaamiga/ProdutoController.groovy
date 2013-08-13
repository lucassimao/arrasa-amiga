package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured

 
class ProdutoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

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


        def produtoInstance = new Produto(params)
        produtoInstance.fotos = []

        def multipartFileMiniatura = request.getFile('fotoMiniatura')
        produtoInstance.fotoMiniatura =  multipartFileMiniatura.originalFilename
        
        def multipartFiles = []

        params.keySet().each{ param->

            if (param.startsWith('fotos')){

                def multipartFile = request.getFile(param)
                def originalFilename = multipartFile.originalFilename

                produtoInstance.fotos << originalFilename
                multipartFiles << multipartFile

            }
        }

        
        if (!produtoInstance.save(flush: true)) {
            produtoInstance.fotoMiniatura = ''
            produtoInstance.fotos = []
            render(view: "create", model: [produtoInstance: produtoInstance])
            return
        }
        
        multipartFiles.each{
            it.transferTo(new File('web-app/img/produtos/' + it.originalFilename))
        }

        multipartFileMiniatura.transferTo(new File('web-app/img/produtos/' + produtoInstance.fotoMiniatura))

        produtoInstance.unidades.each{ un->

            def estoque = new Estoque()

            estoque.produto = produtoInstance
            estoque.unidade = un
            estoque.quantidade = 0 // qtde inicial

            estoque.save()
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'produto.label', default: 'Produto'), produtoInstance.id])
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

    //Apresenta detalhes do produto ao comprados
    def detalhes(Long id) {
        def produtoInstance = Produto.get(id)
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(view: "/index")
            return
        }

        def estoque = Estoque.findByProduto(produtoInstance)

        [produtoInstance: produtoInstance,estoque:estoque]
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

        produtoInstance.properties = params


        params.precoAVistaEmReais = params.precoAVistaEmReais.replace('.',',')
        params.precoAPrazoEmReais = params.precoAPrazoEmReais.replace('.',',')

        produtoInstance.fotos = []

        def multipartFileMiniatura = request.getFile('fotoMiniatura')
        produtoInstance.fotoMiniatura =  multipartFileMiniatura.originalFilename
        
        def multipartFiles = []

        params.keySet().each{ param->

            if (param.startsWith('fotos')){

                def multipartFile = request.getFile(param)
                def originalFilename = multipartFile.originalFilename

                produtoInstance.fotos <<  originalFilename
                multipartFiles << multipartFile

            }
        }


        if (!produtoInstance.save(flush: true)) {
            render(view: "edit", model: [produtoInstance: produtoInstance])
            produtoInstance.fotoMiniatura = ''
            produtoInstance.fotos = []
            return
        }

        multipartFiles.each{
            it.transferTo(new File('web-app/img/produtos/' + it.originalFilename))
        }

        multipartFileMiniatura.transferTo(new File('web-app/img/produtos/' + produtoInstance.fotoMiniatura))

        flash.message = message(code: 'default.updated.message', args: [message(code: 'produto.label', default: 'Produto'), produtoInstance.id])
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
