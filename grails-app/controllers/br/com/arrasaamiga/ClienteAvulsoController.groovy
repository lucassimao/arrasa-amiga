package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class ClienteAvulsoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [clienteAvulsoInstanceList: ClienteAvulso.list(params), clienteAvulsoInstanceTotal: ClienteAvulso.count()]
    }

    def create() {
        [clienteAvulsoInstance: new ClienteAvulso(params)]
    }

    def save() {
        def clienteAvulsoInstance = new ClienteAvulso(params)
        if (!clienteAvulsoInstance.save(flush: true)) {
            render(view: "create", model: [clienteAvulsoInstance: clienteAvulsoInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'clienteAvulso.label', default: 'ClienteAvulso'), clienteAvulsoInstance.id])
        redirect(action: "show", id: clienteAvulsoInstance.id)
    }

    def show(Long id) {
        def clienteAvulsoInstance = ClienteAvulso.get(id)
        if (!clienteAvulsoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clienteAvulso.label', default: 'ClienteAvulso'), id])
            redirect(action: "list")
            return
        }

        [clienteAvulsoInstance: clienteAvulsoInstance]
    }

    def edit(Long id) {
        def clienteAvulsoInstance = ClienteAvulso.get(id)
        if (!clienteAvulsoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clienteAvulso.label', default: 'ClienteAvulso'), id])
            redirect(action: "list")
            return
        }

        [clienteAvulsoInstance: clienteAvulsoInstance]
    }

    def update(Long id, Long version) {
        def clienteAvulsoInstance = ClienteAvulso.get(id)
        if (!clienteAvulsoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clienteAvulso.label', default: 'ClienteAvulso'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (clienteAvulsoInstance.version > version) {
                clienteAvulsoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'clienteAvulso.label', default: 'ClienteAvulso')] as Object[],
                          "Another user has updated this ClienteAvulso while you were editing")
                render(view: "edit", model: [clienteAvulsoInstance: clienteAvulsoInstance])
                return
            }
        }

        clienteAvulsoInstance.properties = params

        if (!clienteAvulsoInstance.save(flush: true)) {
            render(view: "edit", model: [clienteAvulsoInstance: clienteAvulsoInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'clienteAvulso.label', default: 'ClienteAvulso'), clienteAvulsoInstance.id])
        redirect(action: "show", id: clienteAvulsoInstance.id)
    }

    def delete(Long id) {
        def clienteAvulsoInstance = ClienteAvulso.get(id)
        if (!clienteAvulsoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clienteAvulso.label', default: 'ClienteAvulso'), id])
            redirect(action: "list")
            return
        }

        try {
            clienteAvulsoInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'clienteAvulso.label', default: 'ClienteAvulso'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'clienteAvulso.label', default: 'ClienteAvulso'), id])
            redirect(action: "show", id: id)
        }
    }
}
