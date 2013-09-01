package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.*
import br.com.uol.pagseguro.domain.*
import br.com.uol.pagseguro.exception.PagSeguroServiceException
import grails.plugins.springsecurity.Secured

class ShoppingCartController {

	def shoppingCartService
    def springSecurityService

    static allowedMethods = [add: "POST", removerProduto: "POST"]

    private List getProximosDiasDeEntrega(){
        def hoje = new Date()
        def diasDeEntraga = []

        Date terca, quinta, sabado

        switch(hoje.toCalendar().get(Calendar.DAY_OF_WEEK)){
            case Calendar.SUNDAY:
                terca = (hoje + 2)
                quinta = (hoje + 4)
                sabado = (hoje + 6)
                break
            case Calendar.MONDAY:
                terca = (hoje + 1)
                quinta = (hoje + 3)
                sabado = (hoje + 5)

                break
            case Calendar.TUESDAY:
                terca =  (hoje + 7)
                quinta = (hoje + 2)
                sabado = (hoje + 4)
                break

            case Calendar.WEDNESDAY:
                terca =  (hoje + 6)
                quinta = (hoje + 1)
                sabado = (hoje + 3)
                break

            case Calendar.THURSDAY:
                terca =  (hoje + 5)
                quinta = (hoje+7)
                sabado = (hoje + 2)
                break

            case Calendar.FRIDAY:
                terca =  (hoje + 4)
                quinta = (hoje + 6)
                sabado = (hoje + 1)
                break

            case Calendar.SATURDAY:
                terca =  (hoje + 3)
                quinta = (hoje + 5)
                sabado = (hoje + 7)
                break
            default:
                throw new Exception("Data não identificada! ${hoje}")

        }

        diasDeEntraga.add(terca)
        diasDeEntraga.add(quinta)
        diasDeEntraga.add(sabado)

        return diasDeEntraga.sort()
    }

    private Double somarValorTotalAPrazoDoCarrinho(){
        double total = 0
        
        shoppingCartService.getItens().each{ itemVenda ->

            total += itemVenda.precoAPrazoEmReais * itemVenda.quantidade
        }

        return total

    }


    private Double somarValorTotalAVistaDoCarrinho(){
        double total = 0
        
        shoppingCartService.getItens().each{ itemVenda ->

            total += itemVenda.precoAVistaEmReais * itemVenda.quantidade
        }

        return total

    }

    def index(){
    	def total = somarValorTotalAPrazoDoCarrinho()

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

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def checkout(){
        
        def itens = shoppingCartService.itens

        if (itens.size() == 0){
            flash.message = 'Seu carrinho está vazio!'
            redirect(action:'index')
            return
        }

        def cliente = Cliente.findByUsuario(springSecurityService.currentUser)
        def frete = (cliente.endereco.fromTeresina)?0:25
        
        def valorAPrazo = somarValorTotalAPrazoDoCarrinho()
        def valorAVista = somarValorTotalAVistaDoCarrinho()

        def desconto = valorAPrazo - valorAVista

        def totalAPrazo = valorAPrazo + frete
        def totalAVista = valorAVista + frete

        List diasDeEntrega = getProximosDiasDeEntrega()

        

        ['totalAPrazo':totalAPrazo,'totalAVista':totalAVista,valorAPrazo:valorAPrazo,
            'enderecoEntrega': cliente.endereco,itens: itens, frete: frete, desconto:desconto,valorAVista:valorAVista, diasDeEntrega: diasDeEntrega]
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def fecharVenda(){

        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)

        flash.informacoesAdicionaisEntrega = params.informacoesAdicionais

        try{
        
            def fp = FormaPagamento.valueOf( params.formaPagamento )

            if ( !cliente.endereco.isFromTeresina() && fp != FormaPagamento.PagSeguro  ){
                flash.message = "Forma de pagamento inválida!" 
                redirect(action: "checkout") 
                return                
            }
        
        }catch(Exception e){
            flash.message = "A forma de pagamento deve ser informada" 
            redirect(action: "checkout") 
            return 
        }
        

        if (cliente.endereco.isFromTeresina()){

            if ( params.dataEntrega )  {

                def dataDeEntregaEscolhida = null

                try{
                
                    dataDeEntregaEscolhida =  new Date( Long.valueOf(params.dataEntrega) ) 
                
                }catch(Exception e){

                    e.printStackTrace()
                    flash.message = "A data da entrega deve ser selecionada" 
                    redirect(action: "checkout") 
                    return 

                }

                List diasDeEntrega = getProximosDiasDeEntrega()

                def calDiaEntrega = Calendar.getInstance()
                calDiaEntrega.time = dataDeEntregaEscolhida

                boolean dataSelecionadaCorretamente = diasDeEntrega.any {dia-> 
                   
                    def cal2 = Calendar.getInstance()
                    cal2.time = dia

                    boolean sameDay = calDiaEntrega.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                                      calDiaEntrega.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)

                    return sameDay
                }

                if (!dataSelecionadaCorretamente){
                    flash.message = "A data da entrega deve ser selecionada" 
                    redirect(action: "checkout") 
                    return                      
                }

    
            }else{
                flash.message = "A data da entrega deve ser selecionada" 
                redirect(action: "checkout") 
                return  
            }

        }


        def valorAPrazo = somarValorTotalAPrazoDoCarrinho()
        def valorAVista = somarValorTotalAVistaDoCarrinho()

        def venda = new Venda()
        venda.carrinho = shoppingCartService.shoppingCart
        venda.cliente = cliente
        venda.enderecoEntrega = cliente.endereco

        venda.freteEmCentavos = (venda.enderecoEntrega.fromTeresina)?0:2500
        venda.subTotalItensEmCentavos = valorAPrazo * 100

        if (cliente.endereco.isFromTeresina()){
            
            venda.informacoesAdicionaisEntrega = params.informacoesAdicionais
            venda.dataEntrega = new Date( Long.valueOf(params.dataEntrega) )

        }

        venda.formaPagamento = FormaPagamento.valueOf( params.formaPagamento )
        venda.status = StatusVenda.AguardandoPagamento  
        venda.itensVenda = shoppingCartService.checkOut()

        if ( venda.formaPagamento == FormaPagamento.AVista ){

            venda.descontoEmCentavos = (valorAPrazo - valorAVista) * 100

        }else{

            venda.descontoEmCentavos = 0 //será atribuido em Pagseguro#retorno
        
        }                    

        



        if ( venda.formaPagamento == FormaPagamento.AVista ){

            venda.carrinho.checkedOut = true
            venda.carrinho.save()
            venda.save()

            redirect(action:'show',controller:'venda', id:venda.id)
            return

        }else{
            
            venda.save() // salva logo, pois precisa do ID da venda para registrar com a transação de pagamento do pagseguro
           
            def paymentURL = null

            try{
                
                paymentURL = venda.getPaymentURL()
                redirect(url:paymentURL)
                return
                
            }catch(PagSeguroServiceException e){

                println "Erro ao tentar ir para o pagseguro : cliente ${cliente.id} "
                Iterator itr = e.getErrorList().iterator();  
      
                while (itr.hasNext()) {  
      
                    Error error = (Error) itr.next();  
      
                    System.out.println("Código do erro: " + error.getCode());  
      
                    System.out.println("Mensagem de erro: " + error.getMessage());  
      
                }  
                
                e.printStackTrace()

                venda.delete() // não deu pra mandar pro pag seguro ... exclui venda 
                
                flash.message = e.toString() 
                redirect(action: "checkout") 
                return    
            }

        }        
        

    }
}
