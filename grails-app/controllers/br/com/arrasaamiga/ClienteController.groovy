package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.Validateable
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.savedrequest.SavedRequest
import org.springframework.validation.FieldError

@Validateable
class EnderecoCommand {
    String cep
    String complemento
    String bairro
    Cidade cidade
    Uf uf

    static constraints = {
        cep(blank: true, nullable: true, validator: { val, obj ->

            if (obj.cidade?.id != Cidade.teresina.id) {
                return val?.trim()?.size() > 0
            }

            return true

        })
        complemento(blank: false, nullable: false)
        bairro(blank: false, nullable: false)
        cidade(blank: false, nullable: false)
        uf(nullable: false)

    }
}
// Command p/ cadastro de cliente pelo site web
@Validateable
class ClienteCommand {
    String nome
    String celular
    String dddCelular
    String dddTelefone
    String telefone
    String email
    Usuario usuario
    String senha
    EnderecoCommand endereco

    static constraints = {
        nome(blank: false, nullable: false)
        email(nullable: true, blank: true, validator: { val, obj ->

            if (obj.usuario?.id) {
                return true
            } else {
                if (val) {
                    int count = Usuario.countByUsername(val)
                    return count == 0
                } else
                    return false
            }

        })
        celular(blank: false, nullable: false, maxSize: 9)
        dddCelular(blank: false, nullable: false, maxSize: 2)
        telefone(blank: false, nullable: false, maxSize: 9)
        dddTelefone(blank: false, nullable: false, maxSize: 2)
        endereco(nullable: false)
        usuario(nullable: true)
        senha(nullable: true, blank: true, validator: { val, obj ->
            if (obj.usuario?.id) {
                return true
            } else {
                return (val) ? true : false
            }
        })

    }

}

@Secured(['isAuthenticated()'])
class ClienteController {


    def springSecurityService
    def log

    def index() {
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)

        [cliente: cliente, pedidos: Venda.findAllByCliente(cliente, [max: 3, sort: 'dateCreated', order: 'desc'])]
    }

    def favoritos() {}

    def edit() {
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)

        [cliente: cliente]
    }

    @Secured(['permitAll'])
    def cadastro() {
        [cliente: new Cliente(usuario: new Usuario(), endereco: new Endereco())]
    }

    @Secured(['permitAll'])
    def save(ClienteCommand command) {

        def cliente = new Cliente(params)
        cliente.usuario.enabled = true

        command.validate()
        command.endereco.validate()

        if (command.hasErrors() || command.endereco.hasErrors()) {

            command.errors.allErrors.each { FieldError error ->
                String field = error.field
                String code = error.code
                cliente.errors.rejectValue(field, code)
            }

            command.endereco.errors.allErrors.each { FieldError error ->
                String field = error.field
                String code = error.code
                cliente.endereco.errors.rejectValue(field, code)
            }

            render(view: "cadastro", model: [cliente: cliente])
            return
        }


        cliente.save(flush: true, failOnError: true)
        log.info "Cliente #${cliente.id} ${cliente.nome} salvo com sucesso"

        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response)
        flash.message = 'Bem vinda amiga! Sua conta foi criada com sucesso.'
        springSecurityService.reauthenticate cliente.email

        if (savedRequest) {
            log.info "redirecionando ${cliente.nome} ${savedRequest.redirectUrl}"
            redirect(url: savedRequest.redirectUrl)
        } else {
            redirect(url: '/')
        }

    }

    def update(ClienteCommand command) {


        def cliente = Cliente.get(params.id)
        cliente.properties = params

        command.validate()
        command.endereco.validate()

        if (command.hasErrors() || command.endereco.hasErrors()) {

            command.errors.allErrors.each { FieldError error ->
                String field = error.field
                String code = error.code
                cliente.errors.rejectValue(field, code)
            }

            command.endereco.errors.allErrors.each { FieldError error ->
                String field = error.field
                String code = error.code
                cliente.endereco.errors.rejectValue(field, code)
            }

            flash.validationMessage = 'Alguns dados precisam ser preenchidos'
            render(view: "edit", model: [cliente: cliente])
            return
        }

        cliente.save(flush: true, failOnError: true)


        flash.message = 'Seus dados foram atualizados com sucesso!'
        springSecurityService.reauthenticate cliente.email

        redirect(action: 'edit', model: [cliente: cliente])

    }


    def pedidos() {
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)

        [pedidos: Venda.findAllByCliente(cliente, [sort: 'dateCreated', order: 'desc'])]
    }

}
