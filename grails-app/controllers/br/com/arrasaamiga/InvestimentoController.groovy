package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class InvestimentoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [investimentoInstanceList: Investimento.list(params), investimentoInstanceTotal: Investimento.count()]
    }

    def create() {
        [investimentoInstance: new Investimento(params)]
    }

    def save() {

        params.valorEmReais = params.valorEmReais.replace('.',',')


        def investimentoInstance = new Investimento(params)
            

        if (!investimentoInstance.save(flush: true)) {
            render(view: "create", model: [investimentoInstance: investimentoInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'investimento.label', default: 'Investimento'), investimentoInstance.id])
        redirect(action: "show", id: investimentoInstance.id)
    }

    def show(Long id) {
        def investimentoInstance = Investimento.get(id)
        if (!investimentoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'investimento.label', default: 'Investimento'), id])
            redirect(action: "list")
            return
        }

        [investimentoInstance: investimentoInstance]
    }

    def edit(Long id) {
        def investimentoInstance = Investimento.get(id)
        if (!investimentoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'investimento.label', default: 'Investimento'), id])
            redirect(action: "list")
            return
        }

        [investimentoInstance: investimentoInstance]
    }

    def update(Long id, Long version) {
        def investimentoInstance = Investimento.get(id)
        if (!investimentoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'investimento.label', default: 'Investimento'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (investimentoInstance.version > version) {
                investimentoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'investimento.label', default: 'Investimento')] as Object[],
                          "Another user has updated this Investimento while you were editing")
                render(view: "edit", model: [investimentoInstance: investimentoInstance])
                return
            }
        }

        params.valorEmReais = params.valorEmReais.replace('.',',')
        investimentoInstance.properties = params

        if (!investimentoInstance.save(flush: true)) {
            render(view: "edit", model: [investimentoInstance: investimentoInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'investimento.label', default: 'Investimento'), investimentoInstance.id])
        redirect(action: "show", id: investimentoInstance.id)
    }

    def delete(Long id) {
        def investimentoInstance = Investimento.get(id)
        if (!investimentoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'investimento.label', default: 'Investimento'), id])
            redirect(action: "list")
            return
        }

        try {
            investimentoInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'investimento.label', default: 'Investimento'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'investimento.label', default: 'Investimento'), id])
            redirect(action: "show", id: id)
        }
    }
}
