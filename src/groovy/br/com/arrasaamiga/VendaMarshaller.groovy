package br.com.arrasaamiga

import grails.converters.JSON

import javax.annotation.PostConstruct

/**
 * Created by lsimaocosta on 26/11/14.
 */

class VendaMarshaller {

    @PostConstruct
    void registerMarshaller() {
        // TODO descomentar na versao 2.4.4
/*        JSON.registerObjectMarshaller(Venda) { Venda venda ->
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
        }*/
    }
}
