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
            map['vendedor'] = venda.vendedor?.id
            map['freteEmCentavos'] = venda.freteEmCentavos
            map['formaPagamento'] = venda.formaPagamento.name()
            map['status'] = venda.status.name()
            map['dataEntrega'] = venda.dataEntrega?.format('dd/MM/yyyy')
            map['servicoCorreio'] = venda.servicoCorreio?.name()
            map['cliente'] = [:]
            map['cliente']['nome'] = venda.cliente.nome
            map['cliente']['celular'] = "${venda.cliente.dddCelular}-${venda.cliente.dddCelular}"
            map['cliente']['telefone'] = "${venda.cliente.dddTelefone}-${venda.cliente.telefone}"
            map['endereco'] =  [:]
            map['endereco']['cep'] = venda.cliente.endereco.cep
            map['endereco']['complemento'] = venda.cliente.endereco.complemento
            map['endereco']['bairro'] = venda.cliente.endereco.bairro
            map['endereco']['cidade'] = venda.cliente.endereco.cidade.nome
            map['endereco']['uf'] = venda.cliente.endereco.uf.nome
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
