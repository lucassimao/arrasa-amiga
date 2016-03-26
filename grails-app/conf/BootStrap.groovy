import br.com.arrasaamiga.Endereco
import br.com.arrasaamiga.MovimentoCaixa
import br.com.arrasaamiga.Estoque
import br.com.arrasaamiga.caixa.Bonus
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

        File anexosDir = grailsApplication.mainContext.getResource('images/anexos').file
        if (!anexosDir.exists()) {

            boolean dirCreated = anexosDir.mkdirs()
            if (!dirCreated)
                throw new Error('Diretorio de anexos não foi criado')
        }


        if (!GrupoDeUsuario.findByAuthority('ROLE_ADMIN'))
            new GrupoDeUsuario(authority: 'ROLE_ADMIN').save(flush: true)


        if (!GrupoDeUsuario.findByAuthority('ROLE_VENDEDOR'))
            new GrupoDeUsuario(authority: 'ROLE_VENDEDOR').save(flush: true)

        if (!GrupoDeUsuario.findByAuthority('ROLE_CLIENTE'))
            new GrupoDeUsuario(authority: 'ROLE_CLIENTE').save(flush: true)


        JSON.registerObjectMarshaller(MovimentoCaixa){MovimentoCaixa mc->

            def map = [:]
            map['descricao'] = mc.descricao
            map['tipoMovimentoCaixa'] = mc.tipoMovimentoCaixa.name()
            map['valorEmCentavos'] = mc.valorEmCentavos
            map['formaPagamento'] = mc.formaPagamento.name()
            map['lastUpdated'] = mc.lastUpdated.time
            map['data'] = mc.data.format('MMddyyyy')

            return map
        }

        JSON.registerObjectMarshaller(Bonus){Bonus bonus->
            String pattern = 'dd/MM/yyyy'

            def map = [:]
            map['week_start'] = bonus.weekStart.format(pattern)
            map['week_end'] = bonus.weekEnd.format(pattern)
            map['strike_dates'] = bonus.strikeDates.collect{date-> date.format(pattern)}

            return map
        }

        JSON.registerObjectMarshaller(Estoque){Estoque estoque->
            def map = [:]
            map['last_updated'] = estoque.lastUpdated.time
            map['produto_id'] = estoque.produto.id
            map['estoque_id'] = estoque.id
            map['unidade'] = estoque.unidade
            map['quantidade'] = estoque.quantidade
            map['produto_nome'] = estoque.produto.nome
            map['produto_precoAVistaEmCentavos'] = estoque.produto.precoAVistaEmCentavos
            map['produto_precoAPrazoEmCentavos'] = estoque.produto.precoAPrazoEmCentavos
            def fotosDaUnidadeDoProduto = estoque.produto.fotos.findAll{fotoProduto-> fotoProduto.unidade?.equals(estoque.unidade) }
            map['produto_fotos'] = fotosDaUnidadeDoProduto.collect{fotoProduto-> fotoProduto.arquivo}

            return map
        }

        JSON.registerObjectMarshaller(Venda) { Venda venda ->
            def map = [:]
            map['id'] = venda.id
            map['last_updated'] = venda.lastUpdated.time
            map['vendedor'] = venda.vendedor?.username
            map['abatimentoEmCentavos'] = venda.abatimentoEmCentavos
            map['freteEmCentavos'] = venda.freteEmCentavos
            map['flagClienteVaiBuscar'] = venda.flagClienteVaiBuscar
            map['flagClienteJaBuscou'] = venda.flagClienteJaBuscou
            map['formaPagamento'] = venda.formaPagamento.name()
            map['status'] = venda.status.name()
            map['dataEntrega'] = venda.dataEntrega?.time
            map['turnoEntrega'] = (venda.turnoEntrega) ? venda.turnoEntrega.name() : TurnoEntrega.Manha.name()
            map['servicoCorreio'] = (venda.servicoCorreio)?venda.servicoCorreio.name():''
            map['codigoRastreio'] = venda.codigoRastreio?:''
            map['anexos_json_array'] = (venda.anexos)?:[]
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
            map['cliente']['endereco']['cidade'] = [:]
            map['cliente']['endereco']['cidade']['id'] = (venda.cliente?.endereco?.cidade?.id) ?: ''
            map['cliente']['endereco']['uf'] = (venda.cliente?.endereco?.uf?.sigla) ?: ''
            map['itens'] = []

            venda.itensVenda.each {
                def item = [:]
                item['id'] = it.id
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
