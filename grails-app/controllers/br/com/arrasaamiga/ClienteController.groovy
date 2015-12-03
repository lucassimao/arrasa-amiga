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

    @Secured(['ROLE_ADMIN'])
    def enderecos(Long lastDownloadedTimestamp){

        def sql = new Sql(dataSource)
        if (lastDownloadedTimestamp == null)
            lastDownloadedTimestamp = 0

     /*    
            -- Gerar datas aleatorias para teste
                insert into cliente(celular,telefone,nome,endereco_complemento) 
                    select celular,telefone,nome,endereco_complemento from cliente;

                update cliente set date_created=( SELECT NOW() - INTERVAL id SECOND);
                
                update cliente set last_updated=date_created;
                
                select last_updated_unix_timestamp,count(*) qtde from cliente group by last_updated_unix_timestamp order by qtde asc;
                
                --as trigger abaixo vao atualizar o campo last_updated_unix_timestamp
            ------------------------------------------------------------------------

            alter table cliente add last_updated_unix_timestamp bigint(20);

            create trigger convert_last_updated_2_unix_timestamp BEFORE UPDATE ON cliente for each row set 
                NEW.last_updated_unix_timestamp=unix_timestamp(new.last_updated);

            create trigger convert_last_updated_2_unix_timestamp_before_insert BEFORE INSERT ON cliente for 
                each row set NEW.last_updated_unix_timestamp=unix_timestamp(new.last_updated);

        -- depois de criar a coluna last_updated, atualizar com o valor de date_created
            update cliente c JOIN cliente c2 on c.id=c2.id set c.last_updated=c2.date_created;

            alter table cliente add index(last_updated_unix_timestamp);
            alter table cliente add index(telefone);
            alter table cliente add index(celular);

            create view celulares_mais_recentes as select celular, max(last_updated_unix_timestamp) last_updated_unix_timestamp
                from cliente group by celular having celular is not null;

            create view telefones_mais_recentes as select telefone, max(last_updated_unix_timestamp) last_updated_unix_timestamp from
                cliente group by telefone having telefone is not null;

          create view clientes_enderecos_recentes as
                select c1.id,c1.nome,c1.celular,c1.ddd_celular,c1.telefone,c1.ddd_telefone,c1.endereco_complemento,
                c1.endereco_uf_id,c1.endereco_cidade_id,c1.endereco_bairro,c1.last_updated_unix_timestamp
            from cliente c1 JOIN celulares_mais_recentes c2 on c1.celular = c2.celular and c1.last_updated_unix_timestamp=c2.last_updated_unix_timestamp    
          UNION
                select c1.id,c1.nome,c1.celular,c1.ddd_celular,c1.telefone,c1.ddd_telefone,c1.endereco_complemento,c1.endereco_uf_id,
                c1.endereco_cidade_id,c1.endereco_bairro,c1.last_updated_unix_timestamp
          from cliente c1 JOIN telefones_mais_recentes c2 on c1.telefone = c2.telefone and c1.last_updated_unix_timestamp=c2.last_updated_unix_timestamp;

         */

        def results = sql.rows('select * from clientes_enderecos_recentes where last_updated_unix_timestamp>?',[lastDownloadedTimestamp])

        def list = results.collect{row->

            def map = [:]

            map['id']=row['id'] 
            map['cliente'] = row['nome']
            map['celular'] = row['celular']?:''
            map['ddd_celular'] = row['ddd_celular']?:''
            map['telefone'] = row['telefone']?:''
            map['ddd_telefone'] = row['ddd_telefone']?:''
            map['endereco'] = row['endereco_complemento']
            

            def uf = (row['endereco_uf_id'])?Uf.get(row['endereco_uf_id']):Uf.piaui

            map['uf'] = uf.sigla
            map['id_uf'] = uf.id

            def cidade = (row['endereco_cidade_id'])?(Cidade.get(row['endereco_cidade_id'])):Cidade.teresina

            map['cidade'] = cidade.nome
            map['id_cidade'] = cidade.id
            map['bairro'] = row['endereco_bairro']

            map['last_updated_unix_timestamp'] = row['last_updated_unix_timestamp']

            return map
        }

        respond list, [formats:['json']]
    }

}
