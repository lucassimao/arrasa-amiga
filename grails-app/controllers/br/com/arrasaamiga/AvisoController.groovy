package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.dao.DataIntegrityViolationException

@Secured(['ROLE_ADMIN'])
class AvisoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [avisoInstanceList: Aviso.list(params), avisoInstanceTotal: Aviso.count()]
    }

    def create() {
        [avisoInstance: new Aviso(params)]
    }

    def save() {
        def avisoInstance = new Aviso(params)
        if (!avisoInstance.save(flush: true)) {
            render(view: "create", model: [avisoInstance: avisoInstance])
            return
        }

        flash.message = 'Aviso salvo!'
        redirect(action: "list")
    }



    def delete(Long id) {
        def avisoInstance = Aviso.get(id)
        if (!avisoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'aviso.label', default: 'Aviso'), id])
            redirect(action: "list")
            return
        }

        try {
            avisoInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'aviso.label', default: 'Aviso'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'aviso.label', default: 'Aviso'), id])
            redirect(action: "list")
        }
    }
}
