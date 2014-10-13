import br.com.arrasaamiga.GrupoDeUsuario
import br.com.arrasaamiga.TurnoEntrega
import br.com.arrasaamiga.Usuario
import br.com.arrasaamiga.UsuarioGrupoDeUsuario
import br.com.arrasaamiga.Venda
import grails.converters.JSON
import grails.util.BuildSettingsHolder
import grails.util.Environment

class BootStrap {

    def grailsApplication

    def init = { servletContext ->

        File uploadDir = grailsApplication.mainContext.getResource('images/produtos').file
        if (!uploadDir.exists()) {

            boolean dirCreated = uploadDir.mkdirs()
            if (!dirCreated)
                throw new Error('Diretorio de imagens de produtos não foi criado')
        }


        File bannersDir = grailsApplication.mainContext.getResource('images/banners').file
        if (!bannersDir.exists()) {

            boolean dirCreated = bannersDir.mkdirs()
            if (!dirCreated)
                throw new Error('Diretorio de banners não foi criado')
        }



        Environment.executeForCurrentEnvironment {

            development {

                def adminRole = new GrupoDeUsuario(authority: 'ROLE_ADMIN').save(flush: true)
                def userRole = new GrupoDeUsuario(authority: 'ROLE_CLIENTE').save(flush: true)

                def testUser = new Usuario(username: 'me', enabled: true, password: '123')
                testUser.save(flush: true)

                UsuarioGrupoDeUsuario.create testUser, adminRole, true
            }
        }

        JSON.registerObjectMarshaller(Venda) { Venda venda ->
            def map = [:]
            map['id'] = venda.id
            map['vendedor'] = venda.vendedor?.username
            map['freteEmCentavos'] = venda.freteEmCentavos
            map['formaPagamento'] = venda.formaPagamento.name()
            map['status'] = venda.status.name()
            map['dataEntrega'] = venda.dataEntrega?.time
            map['turnoEntrega'] = (venda.turnoEntrega) ? venda.turnoEntrega.name() : TurnoEntrega.Manha.name()
            map['servicoCorreio'] = venda.servicoCorreio?.name()
            map['cliente'] = [:]
            map['cliente']['id'] = venda.cliente?.id
            map['cliente']['nome'] = venda.cliente?.nome
            map['cliente']['dddCelular'] = (venda.cliente?.dddCelular) ?: ''
            map['cliente']['celular'] = (venda.cliente?.celular) ?: ''
            map['cliente']['dddTelefone'] = (venda.cliente?.dddTelefone) ?: ''
            map['cliente']['telefone'] = (venda.cliente?.telefone) ?: ''
            map['cliente']['endereco'] = [:]
            map['cliente']['endereco']['cep'] = (venda.cliente?.endereco?.cep) ?: ''
            map['cliente']['endereco']['complemento'] = (venda.cliente?.endereco?.complemento) ?: ''
            map['cliente']['endereco']['bairro'] = (venda.cliente?.endereco?.bairro) ?: ''
            map['cliente']['endereco']['cidade'] = (venda.cliente?.endereco?.cidade?.nome) ?: ''
            map['cliente']['endereco']['uf'] = (venda.cliente?.endereco?.uf?.nome) ?: ''
            map['itens'] = []

            venda.itensVenda.each {
                def item = [:]
                item['produto_id'] = it.produto.id
                item['produto_nome'] = it.produto.nome
                item['quantidade'] = it.quantidade
                item['unidade'] = it.unidade
                item['precoAVistaEmCentavos'] = it.precoAVistaEmCentavos
                item['precoAPrazoEmCentavos'] = it.precoAPrazoEmCentavos

                map['itens'] << item
            }

            return map
        }



        JSON.registerObjectMarshaller(Venda) {Venda venda ->
            def map = [:]
            map['id'] = venda.id
            map['vendedor'] = venda.vendedor?.username
            map['freteEmCentavos'] = venda.freteEmCentavos
            map['formaPagamento'] = venda.formaPagamento.name()
            map['status'] = venda.status.name()
            map['dataEntrega'] = venda.dataEntrega?.time
            map['turnoEntrega'] = (venda.turnoEntrega)?venda.turnoEntrega.name():TurnoEntrega.Manha.name()
            map['servicoCorreio'] = venda.servicoCorreio?.name()
            map['cliente'] = [:]
            map['cliente']['id'] = venda.cliente?.id
            map['cliente']['nome'] = venda.cliente?.nome
            map['cliente']['dddCelular'] = (venda.cliente?.dddCelular) ?: ''
            map['cliente']['celular'] = (venda.cliente?.celular) ?: ''
            map['cliente']['dddTelefone'] = (venda.cliente?.dddTelefone) ?: ''
            map['cliente']['telefone'] = (venda.cliente?.telefone) ?: ''
            map['cliente']['endereco'] = [:]
            map['cliente']['endereco']['cep'] = (venda.cliente?.endereco?.cep) ?: ''
            map['cliente']['endereco']['complemento'] = (venda.cliente?.endereco?.complemento) ?: ''
            map['cliente']['endereco']['bairro'] = (venda.cliente?.endereco?.bairro) ?: ''
            map['cliente']['endereco']['cidade'] = (venda.cliente?.endereco?.cidade?.nome) ?: ''
            map['cliente']['endereco']['uf'] = (venda.cliente?.endereco?.uf?.nome) ?: ''
            map['itens'] = []

            venda.itensVenda.each {
                def item = [:]
                item['produto_id'] = it.produto.id
                item['produto_nome'] = it.produto.nome
                item['quantidade'] = it.quantidade
                item['unidade'] = it.unidade
                item['precoAVistaEmCentavos'] = it.precoAVistaEmCentavos
                item['precoAPrazoEmCentavos'] = it.precoAPrazoEmCentavos

                map['itens'] << item
            }

            return map
        }


    }

    def destroy = {
    }
}
