import br.com.arrasaamiga.*

import grails.util.Environment
import grails.converters.JSON

class BootStrap {

    def init = { servletContext ->


    	if (Environment.current == Environment.DEVELOPMENT){

	        def adminRole = new GrupoDeUsuario(authority: 'ROLE_ADMIN').save(flush: true)
	        def userRole = new GrupoDeUsuario(authority: 'ROLE_CLIENTE').save(flush: true)

	        def testUser = new Usuario(username: 'me', enabled: true, password: '123')
	        testUser.save(flush: true)

	        UsuarioGrupoDeUsuario.create testUser, adminRole, true
    	}

        
        JSON.registerObjectMarshaller(Venda) { venda->
            def map= [:]
            map['id'] = venda.id
            map['vendedor'] = venda.vendedor?.username
            map['freteEmCentavos'] = venda.freteEmCentavos
            map['formaPagamento'] = venda.formaPagamento.name()
            map['status'] = venda.status.name()
            map['dataEntrega'] = venda.dataEntrega?.time
            map['servicoCorreio'] = venda.servicoCorreio?.name()
            map['cliente'] = [:]
            map['cliente']['id'] = venda.cliente?.id
            map['cliente']['nome'] = venda.cliente?.nome
            map['cliente']['dddCelular'] = (venda.cliente?.dddCelular)?:''
            map['cliente']['celular'] = (venda.cliente?.celular)?:''
            map['cliente']['dddTelefone'] = (venda.cliente?.dddTelefone)?:''
            map['cliente']['telefone'] = (venda.cliente?.telefone)?:''
            map['cliente']['endereco'] =  [:]
            map['cliente']['endereco']['cep'] = (venda.cliente?.endereco?.cep)?:''
            map['cliente']['endereco']['complemento'] = (venda.cliente?.endereco?.complemento)?:''
            map['cliente']['endereco']['bairro'] = (venda.cliente?.endereco?.bairro)?:''
            map['cliente']['endereco']['cidade'] = (venda.cliente?.endereco?.cidade?.nome)?:''
            map['cliente']['endereco']['uf'] = (venda.cliente?.endereco?.uf?.nome)?:''
            map['items'] = []

            venda.itensVenda.each{ 
                def item = [:]
                item['produto_id'] = it.produto.id
                item['produto_nome'] = it.produto.nome
                item['quantidade'] = it.quantidade
                item['unidade'] = it.unidade

                map['items'] << item
            }

            return map 
        }


    }

    def destroy = {
    }
}
