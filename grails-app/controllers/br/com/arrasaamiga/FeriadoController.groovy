package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])
class FeriadoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Feriado.list(params), model: [feriadoInstanceCount: Feriado.count()]
    }

    def show(Feriado feriadoInstance) {
        respond feriadoInstance
    }

    def create() {
        respond new Feriado(params)
    }

    @Transactional
    def save(Feriado feriadoInstance) {
        if (feriadoInstance == null) {
            notFound()
            return
        }

        if (feriadoInstance.hasErrors()) {
            respond feriadoInstance.errors, view: 'create'
            return
        }

        feriadoInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'feriado.label', default: 'Feriado'), feriadoInstance.id])
                redirect feriadoInstance
            }
            '*' { respond feriadoInstance, [status: CREATED] }
        }
    }

    def edit(Feriado feriadoInstance) {
        respond feriadoInstance
    }

    @Transactional
    def update(Feriado feriadoInstance) {
        if (feriadoInstance == null) {
            notFound()
            return
        }

        if (feriadoInstance.hasErrors()) {
            respond feriadoInstance.errors, view: 'edit'
            return
        }

        feriadoInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Feriado.label', default: 'Feriado'), feriadoInstance.descricao])
                redirect feriadoInstance
            }
            '*' { respond feriadoInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Feriado feriadoInstance) {

        if (feriadoInstance == null) {
            notFound()
            return
        }

        feriadoInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Feriado.label', default: 'Feriado'), feriadoInstance.descricao])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'feriado.label', default: 'Feriado'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
