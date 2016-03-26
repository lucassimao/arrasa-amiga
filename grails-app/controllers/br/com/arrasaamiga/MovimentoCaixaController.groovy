package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional
import static org.springframework.http.HttpStatus.*
import org.codehaus.groovy.grails.web.servlet.HttpHeaders


@Secured(['ROLE_ADMIN'])
class MovimentoCaixaController extends RestfulController {

    static responseFormats = ['json']

    MovimentoCaixaController() {
        super(MovimentoCaixa)
    }
}
