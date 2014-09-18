package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])
class GrupoDeProdutoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond GrupoDeProduto.list(params), model:[grupoDeProdutoInstanceCount: GrupoDeProduto.count()]
    }

    def show(GrupoDeProduto grupoDeProdutoInstance) {
        respond grupoDeProdutoInstance
    }

    def create() {
        respond new GrupoDeProduto(params)
    }

    @Transactional
    def save(GrupoDeProduto grupoDeProdutoInstance) {
        if (grupoDeProdutoInstance == null) {
            notFound()
            return
        }

        if (grupoDeProdutoInstance.hasErrors()) {
            respond grupoDeProdutoInstance.errors, view:'create'
            return
        }

        grupoDeProdutoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'grupoDeProduto.label', default: 'GrupoDeProduto'), ''])
                redirect grupoDeProdutoInstance
            }
            '*' { respond grupoDeProdutoInstance, [status: CREATED] }
        }
    }

    def edit(GrupoDeProduto grupoDeProdutoInstance) {
        respond grupoDeProdutoInstance
    }

    @Transactional
    def update(GrupoDeProduto grupoDeProdutoInstance) {
        if (grupoDeProdutoInstance == null) {
            notFound()
            return
        }

        if (grupoDeProdutoInstance.hasErrors()) {
            respond grupoDeProdutoInstance.errors, view:'edit'
            return
        }

        grupoDeProdutoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'grupoDeProduto.label', default: 'GrupoDeProduto'), ''])
                redirect grupoDeProdutoInstance
            }
            '*'{ respond grupoDeProdutoInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(GrupoDeProduto grupoDeProdutoInstance) {

        if (grupoDeProdutoInstance == null) {
            notFound()
            return
        }

        grupoDeProdutoInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'GrupoDeProduto.label', default: 'GrupoDeProduto'), grupoDeProdutoInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'grupoDeProduto.label', default: 'GrupoDeProduto'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
