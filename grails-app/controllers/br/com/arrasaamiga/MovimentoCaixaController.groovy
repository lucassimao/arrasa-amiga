package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

@Secured(['ROLE_ADMIN'])
class MovimentoCaixaController extends RestfulController {

    static responseFormats = ['json']

    MovimentoCaixaController() {
        super(MovimentoCaixa)
    }

}
