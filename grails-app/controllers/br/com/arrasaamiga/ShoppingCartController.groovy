package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.*
import br.com.uol.pagseguro.domain.*
import br.com.uol.pagseguro.exception.PagSeguroServiceException
import grails.plugin.springsecurity.annotation.Secured


@Secured(['permitAll'])
class ShoppingCartController {

	def shoppingCartService
    def springSecurityService
    def emailService


    static allowedMethods = [add: "POST", removerProduto: "POST"]

    def index(){
    	def total = shoppingCartService.shoppingCart.valorTotalAPrazo

    	['valorTotal':total, itens: shoppingCartService.itens]

    }

    def add(Long id,Integer quantidade,String unidade){

    	def produtoInstance = Produto.get(id)

        if (!produtoInstance){
            flash.message = "Produto ${id} desconhecido"
            redirect(uri:'/', absolute:true)
            return
        }

        if (!produtoInstance.unidades.contains(unidade)){
            flash.message = "${produtoInstance.nome} não contem a unidade ${unidade}"
            redirect(uri: produtoInstance.nomeAsURL, absolute:true)
            return
        }

        if (quantidade <= 0 ){
            flash.message = "Quantidade inválida: ${quantidade}"
            redirect(uri: produtoInstance.nomeAsURL, absolute:true)
            return
        }

        int qtdeEmEstoque = produtoInstance.getQuantidadeEmEstoque(unidade)

        if (qtdeEmEstoque == 0){
            flash.message = "Este produto esta em falta temporariamente ;-("
            redirect(uri: produtoInstance.nomeAsURL, absolute:true)
            return            
        }

        def shoppingCart = shoppingCartService.getShoppingCart()        
        def qtdeAnterior = shoppingCart.getQuantidade(produtoInstance,unidade) // quantidade desse item que o cliente ja adicionou no carrinho

        if ( (qtdeAnterior + quantidade) > qtdeEmEstoque){

            flash.message = "Há apenas ${qtdeEmEstoque}  ${produtoInstance.nome} em estoque para esse item. Você ja incluiu ${qtdeAnterior}"
            
            if (params.origem){
                redirect(url: params.origem, absolute:true)
            }else{
                redirect(uri: produtoInstance.nomeAsURL, absolute:true)
            }
            return
        }

        shoppingCartService.addToShoppingCart(produtoInstance,unidade,quantidade)

    	if (qtdeAnterior ==  0 ){
    		flash.message = " ${quantidade} ${produtoInstance.nome} adicionados(as) ao seu carrinho de compras"
    	}else{
    		flash.message = "Mais ${quantidade} ${produtoInstance.nome} adicionados(as) ao seu carrinho de compras"
    	}
    	
    	redirect(action: "index")
    }


    def removerProduto(Long id,String unidade,Integer quantidade){
        def produto = Produto.get(id)

        if (quantidade){
            
            shoppingCartService.removeFromShoppingCart(produto,unidade,quantidade)

            flash.message = "${quantidade} ${produto.nome} removido(a) do seu carrinho de compras"   

        }else{
            
            quantidade = shoppingCartService.shoppingCart.getQuantidade(produto,unidade)
            
            shoppingCartService.removeFromShoppingCart(produto,unidade, quantidade)

            flash.message = "${produto.nome} removido(a) do seu carrinho de compras"               
        }

    	redirect(action: "index")
    }

    def getCidades(int idUf){
        def uf = Uf.get(idUf)
        def cidades = Cidade.findAllByUf(uf)

        def retorno = []

        cidades.each{c->
            retorno << ['id':c.id,'nome':c.nome]
        }

        render ( retorno as JSON)
    }

    @Secured(['isAuthenticated()'])
    def recalcularTotais(){

        def venda = new Venda()
        venda.carrinho = shoppingCartService.shoppingCart
        venda.cliente = Cliente.findByUsuario(springSecurityService.currentUser)
        venda.formaPagamento = FormaPagamento.valueOf(params.formaPagamento)
        
        if (!venda.cliente.isDentroDaAreaDeEntregaRapida()){
            venda.servicoCorreio = ServicoCorreio.valueOf(params.servicoCorreio)
        }

        render(template:'totalVendaDetalhes',model:[ venda: venda ] )
    }

    @Secured(['isAuthenticated()'])
    def confirmAddress(){
        def user = springSecurityService.currentUser
        ['cliente': Cliente.findByUsuario(user)]
    }

    @Secured(['isAuthenticated()'])
    def checkout(){
        
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)
        cliente.properties = params

        // verifica se foi enviada alguma atualização nos dados do cliente
        // pois ele pode ter vindo de /shoppingCart/confirmAddress
        if (cliente.isDirty() && !cliente.save(flush:true)){
            render view :'confirmAddress', model:[cliente:cliente]
            return
        }

        def shoppingCart = shoppingCartService.shoppingCart
        def itens = shoppingCart.itens

        if (!itens){
            flash.message = 'Seu carrinho está vazio!'
            redirect(action:'index')
            return
        }

        def venda = new Venda()
        venda.carrinho = shoppingCart
        venda.formaPagamento = FormaPagamento.AVista
        venda.cliente = cliente
        venda.carrinho = shoppingCart 

        [ venda: venda, diasDeEntrega: getProximosDiasDeEntrega() ]
    }

    @Secured(['isAuthenticated()'])
    def fecharVenda(){


        def shoppingCart = shoppingCartService.shoppingCart

        if ( !shoppingCart.itens ){
            flash.message = 'Seu carrinho está vazio!'
            redirect(action:'index')
            return
        }

        def venda = new Venda(params)
        venda.carrinho = shoppingCart
        venda.status = StatusVenda.AguardandoPagamento
        venda.cliente = Cliente.findByUsuario(springSecurityService.currentUser)

        def model = [ venda: venda, diasDeEntrega: getProximosDiasDeEntrega() ]

        try{       
            venda.dataEntrega = new Date( Long.valueOf(params.dataEntrega) )
            venda.clearErrors()

        }catch(Exception e){

            flash.messageDataEntrega = "A data da entrega deve ser uma data válida" 
            render(view: "checkout",model:model) 
            return 
        }

        
        // para clientes que nao são de Teresina, o pagamento deve ser necessariamente via PagSeguro
        if ( !venda.cliente.isDentroDaAreaDeEntregaRapida() && venda.formaPagamento != FormaPagamento.PagSeguro  ){

            flash.message = "Forma de pagamento inválida!" 
            render(view: "checkout",model:model) 
            return                
        }


        if ( venda.cliente.isDentroDaAreaDeEntregaRapida() ){

            if (!validarDataEntrega(venda.dataEntrega)){
                flash.messageDataEntrega = "Apenas as datas apresentadas são aceitas" 
                render(view: "checkout",model: model ) 
                return                      
            }

        }      

        Estoque.removerItens(venda.itensVenda)


        if ( venda.formaPagamento == FormaPagamento.AVista ){

            venda.save(flush:true,failOnError:true)
            shoppingCartService.checkout()
            emailService.notificarAdministradores(venda)
            emailService.notificarCliente(venda)         

            redirect(action:'show',controller:'venda', id:venda.id)
            return

        }else{
            
            venda.save(flush:true) // salva logo, pois precisa do ID da venda para registrar com a transação de pagamento do pagseguro
                // não elimina o carrinho ainda, pq nao sabe se vai dar certo. So vai eliminar no retorno do pag seguro
            
            try{
                
                def paymentURL = venda.getPaymentURL()     
                redirect(url:paymentURL)

                return
                
            }catch(PagSeguroServiceException e){

                println "Erro ao tentar ir para o pagseguro : cliente ${venda.cliente.id} "
                Iterator itr = e.getErrorList().iterator();  
      
                while (itr.hasNext()) {  
      
                    Error error = (Error) itr.next();  
      
                    println "Código do erro: ${error.getCode()}"; 
                    println "Código do erro: ${error.getMessage()}"; 
      
                }  
                
                e.printStackTrace()

                venda.delete(flush:true) // não deu pra mandar pro pag seguro ... exclui venda
                Estoque.reporItens(venda.itensVenda)
                
                flash.message = e.toString() 
                render(view: "checkout",model:model)
                return    
            
            }catch(Exception e){

                e.printStackTrace()

                venda.delete(flush:true) // não deu pra mandar pro pag seguro ... exclui venda
                Estoque.reporItens(venda.itensVenda)
                
                flash.message = e.toString() 
                render(view: "checkout",model:model)
                return 

            }

        }        
        
    }


    private boolean validarDataEntrega(Date dataEscolhida){
        List datasDeEntrega = getProximosDiasDeEntrega()

        def calDiaEntrega = Calendar.getInstance()
        calDiaEntrega.time = dataEscolhida
        

        boolean dataSelecionadaCorretamente = datasDeEntrega.any {dia-> 
           
            def cal2 = Calendar.getInstance()
            cal2.time = dia

            boolean sameDay = calDiaEntrega.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                              calDiaEntrega.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)

            return sameDay
        }        
    }



    private List getProximosDiasDeEntrega(){

        def hoje = new Date()
        def diasDeEntraga = []

        Date segunda, quarta, sexta

        switch(hoje.toCalendar().get(Calendar.DAY_OF_WEEK)){
            case Calendar.SUNDAY:
                segunda = (hoje + 1)
                quarta = (hoje + 3)
                sexta = (hoje + 5)
                break
            case Calendar.MONDAY:
                segunda = (hoje + 7)
                quarta = (hoje + 2)
                sexta = (hoje + 4)

                break
            case Calendar.TUESDAY:
                segunda = (hoje + 6)
                quarta = (hoje + 1)
                sexta = (hoje + 3)
                break

            case Calendar.WEDNESDAY:
                segunda = (hoje + 5)
                quarta = (hoje + 7)
                sexta = (hoje + 2)
                break

            case Calendar.THURSDAY:
                segunda = (hoje + 4)
                quarta = (hoje + 6)
                sexta = (hoje + 1)
                break

            case Calendar.FRIDAY:
                segunda = (hoje + 3)
                quarta = (hoje + 5)
                sexta = (hoje + 7)
                break

            case Calendar.SATURDAY:
                segunda = (hoje + 2)
                quarta = (hoje + 4)
                sexta = (hoje + 5)
                break
            default:
                throw new Exception("Data não identificada! ${hoje}")

        }

        diasDeEntraga.add(segunda)
        diasDeEntraga.add(quarta)
        diasDeEntraga.add(sexta)

        return diasDeEntraga.sort()
    }

}
