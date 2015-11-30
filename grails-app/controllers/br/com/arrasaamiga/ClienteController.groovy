package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.util.Holders
import groovy.sql.Sql
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
                def ctx = Holders.getApplicationContext()
                def correiosService = ctx.getBean("correiosService");
                return ( val?.trim()?.size() > 0 && correiosService.isCepValido(val) )
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
        nome(blank: false, nullable: false,validator: {val,command->
            if (val.trim().split(" ").length == 1)
                return 'incompleto'
        })
        email(nullable: false, blank: false, email: true, validator: { val, obj ->

            if (obj.usuario?.id) {
                return true
            } else {
                int count = Usuario.countByUsername(val)
                (count == 0)?true:'emailJaExiste'
            }

        })
        celular(blank: false, nullable: false, maxSize: 9,minSize: 8)
        dddCelular(blank: false, nullable: false, maxSize: 2)
        telefone(blank: false, nullable: false, maxSize: 9,minSize: 8)
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
    def dataSource

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
        cliente.usuario?.enabled = true

        command.validate()
        command.endereco?.validate()

        if (command.hasErrors() || command.endereco?.hasErrors()) {

            command.errors.allErrors.each { FieldError error ->
                String field = error.field
                String code = error.code
                cliente.errors.rejectValue(field, code)
            }

            command.endereco?.errors?.allErrors.each { FieldError error ->
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

    def enderecos(Long lastDownloadedTimestamp){

        Date date = null
        def sql = new Sql(dataSource)
        if (lastDownloadedTimestamp == null)
            lastDownloadedTimestamp = 0

     /*    mysql>
     select c1.id,c1.nome,c1.celular,c1.telefone,c1.endereco_complemento,c1.endereco_uf_id,c1.endereco_cidade_id,
          c1.date_created from cliente c1 JOIN (SELECT celular,MAX(id)
         id from cliente group by celular having celular is not null) c2 on c1.celular = c2.celular and c1.id=c2.id
         UNION
         select c1.id,c1.nome,c1.celular, c1.telefone,c1.endereco_complemento,c1.endereco_uf_id,c1.endereco_cidade_id,
         c1.date_created from cliente c1 JOIN (SELECT telefone,MAX(id)
         id from cliente group by telefone having telefone is not null) c2 on c1.telefone = c2.telefone and c1.id=c2.id


          create view celulares_mais_recentes as select celular, max(id) id
          from cliente group by celular having celular is not null;

          create view telefones_mais_recentes as select telefone, max(id) id from
          cliente group by telefone having telefone is not null;

          create view clientes_enderecos_recentes as
                select c1.nome,c1.celular,c1.telefone,c1.endereco_complemento,
                c1.endereco_uf_id,c1.endereco_cidade_id,c1.endereco_bairro,c1.date_created
          from cliente c1 JOIN celulares_mais_recentes c2 on c1.celular = c2.celular and
          c1.id=c2.id          UNION
                select c1.nome,c1.celular,c1.telefone,c1.endereco_complemento,c1.endereco_uf_id,
                c1.endereco_cidade_id,c1.endereco_bairro,c1.date_created
          from cliente c1 JOIN telefones_mais_recentes c2 on c1.telefone = c2.telefone and c1.id=c2.id;

         */

        def results = sql.rows('select * from clientes_enderecos_recentes where date_created > ?',[lastDownloadedTimestamp])

        def list = results.collect{row->

            def map = [:]

            map['nome'] = row[0]
            map['celular'] = row[1]
            map['telefone'] = row[2]
            map['complemento'] = row[3]
            map['uf'] = row[4]
            map['cidade'] = row[5]
            map['bairro'] = row[6]

            return map
        }

        respond results, [formats:['json']]
    }

}
