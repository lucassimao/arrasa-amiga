import br.com.arrasaamiga.GrupoDeUsuario
import br.com.arrasaamiga.Usuario
import br.com.arrasaamiga.UsuarioGrupoDeUsuario
import br.com.arrasaamiga.Venda
import grails.converters.JSON
import grails.util.BuildSettingsHolder
import grails.util.Environment

class BootStrap {

    def grailsApplication

    def init = { servletContext ->


        Environment.executeForCurrentEnvironment {

            production {
                File uploadDir = grailsApplication.mainContext.getResource('images/produtos').file
                if (!uploadDir.exists()) {

                    boolean dirCreated = uploadDir.mkdirs()
                    if (!dirCreated)
                        throw new Error('Diretorio de imagens de produtos não foi criado')
                }
            }

            development {
                def grailsSettings = BuildSettingsHolder.settings
                String separator = File.separator
                String baseDir = grailsSettings.baseDir.absolutePath
                String assetsFolder = "${baseDir}${separator}grails-app${separator}assets${separator}"
                String uploadDir = "${assetsFolder}images${separator}produtos"
                def dir = new File(uploadDir)

                if (!dir.exists()) {

                    boolean dirCreated = dir.mkdirs()
                    if (!dirCreated)
                        throw new Error('Diretorio de imagens de produtos não foi criado')
                }


                def adminRole = new GrupoDeUsuario(authority: 'ROLE_ADMIN').save(flush: true)
                def userRole = new GrupoDeUsuario(authority: 'ROLE_CLIENTE').save(flush: true)

                def testUser = new Usuario(username: 'me', enabled: true, password: '123')
                testUser.save(flush: true)

                UsuarioGrupoDeUsuario.create testUser, adminRole, true


            }
        }



        JSON.registerObjectMarshaller(Venda) { venda ->
            def map = [:]
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
            map['items'] = []

            venda.itensVenda.each {
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
