package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.*
import br.com.arrasaamiga.caixa.Bonus

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN','ROLE_VENDEDOR'])
class CaixaController {

    static allowedMethods = [show: "GET"]
    
    def springSecurityService   

    def show() {

        def caixaAtual = Caixa.last()
        if (caixaAtual == null)
            notFound()

        def currentUser = springSecurityService.currentUser
        def vendedor = null
        // se o usuario atual nao for admin, consulta apenas as vendas feitas por ele
        // caso contrario consulta vendas de todos os vendedores
        if (!currentUser.isAdmin()) 
            vendedor = currentUser

        def vendas = caixaAtual.getVendas(vendedor)
        long valorTotal=0, totalAVista=0,totalAPrazo=0
        def mapResumoVendedores = [:]
        def resumoBonus = [:] // username -> [data: valor total vendido a vista]

        vendas.each{v->
            // se a venda for a vista, o desconto tira o acrescimo de 10%
            long valorItensEmCentavos = 0

            def username = 'site'
            if (v.vendedor)
                username = v.vendedor.username

            if (!mapResumoVendedores[username]){
                mapResumoVendedores[username] = ['dinheiro':0L,'cartao':0L,'total':0L,'salario':0L]
                resumoBonus[username] = [:]
            }

           switch(v.formaPagamento) {
                case FormaPagamento.AVista:
                    valorItensEmCentavos = v._getValorItensAVista()
                    mapResumoVendedores[username]['dinheiro'] += valorItensEmCentavos
                    
                    def dataEntrega = new Date(v.dataEntrega.time).clearTime()
                    if ( !resumoBonus[username][dataEntrega] )
                        resumoBonus[username][dataEntrega] = 0

                    resumoBonus[username][dataEntrega] += valorItensEmCentavos
                    break
                case FormaPagamento.PagSeguro:
                    valorItensEmCentavos = v._getValorItensAPrazo()
                    long taxaDosProdutos = ( valorItensEmCentavos * v.taxasPagSeguroEmCentavos)/v._getValorTotal()
                    valorItensEmCentavos -= taxaDosProdutos
                    mapResumoVendedores[username]['cartao'] += valorItensEmCentavos
                    break
            }

            mapResumoVendedores[username]['total'] += valorItensEmCentavos
            valorTotal += valorItensEmCentavos

        }

        mapResumoVendedores.each{ username, map->
            map['salario'] = map['total']*0.15 // porcentagem do vendedor
            
            // removendo os dias que o vendedor vendeu menos de R$ 500
            if (resumoBonus[username]){
                def strikes = caixaAtual.calcularBonus(resumoBonus[username])
                if (strikes){
                    map['strikes'] = strikes
                }
            }
        }


        def map = [:]

        if (currentUser.isAdmin()){
            map['vendedores'] = mapResumoVendedores
            map['total'] = valorTotal
        }
        else if (mapResumoVendedores[currentUser.username])
            map = ["${currentUser.username}": mapResumoVendedores[currentUser.username]]
        else
            map = ["${currentUser.username}": ['dinheiro':0L,'cartao':0L,'total':0L,'salario':0L]]

        render map as JSON
    }



    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'caixa.label', default: 'Caixa'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
