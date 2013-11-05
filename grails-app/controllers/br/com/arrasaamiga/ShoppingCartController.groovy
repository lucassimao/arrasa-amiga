package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.*
import br.com.uol.pagseguro.domain.*
import br.com.uol.pagseguro.exception.PagSeguroServiceException
import grails.plugins.springsecurity.Secured

class ShoppingCartController {

	def shoppingCartService
    def springSecurityService
    //def mailService

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

    def getCidades(int idUf){
        def uf = Uf.get(idUf)
        def cidades = Cidade.findAllByUf(uf)

        def retorno = []

        cidades.each{c->
            retorno << ['id':c.id,'nome':c.nome]
        }

        render ( retorno as JSON)
    }

    def checkout(){
        
        def itens = shoppingCartService.itens

        if (itens.size() == 0){
            flash.message = 'Seu carrinho está vazio!'
            redirect(action:'index')
            return
        }

        def frete = 25
        def taxaEntrega = 0

        def valorAPrazo = somarValorTotalAPrazoDoCarrinho()
        def valorAVista = somarValorTotalAVistaDoCarrinho()

        def desconto = valorAPrazo - valorAVista

        if (valorAPrazo < 50){
            taxaEntrega = 2.5
        }

        def totalAPrazo = valorAPrazo + frete + taxaEntrega
        def totalAVista = valorAVista + frete + taxaEntrega

        List diasDeEntrega = getProximosDiasDeEntrega()

        def venda = new Venda()
        def user = springSecurityService.currentUser
       
        if (user){
             venda.cliente = Cliente.findByUsuario(user)

        }else{
            venda.cliente = new Cliente()
            venda.cliente.usuario = new Usuario()
            venda.cliente.endereco = new Endereco(uf: Uf.piaui, cidade : Cidade.teresina )
        }
        

        
        

        ['totalAPrazo':totalAPrazo,'totalAVista':totalAVista,valorAPrazo:valorAPrazo,venda: venda,taxaEntrega:taxaEntrega,
            itens: itens, frete: frete, desconto:desconto,valorAVista:valorAVista, diasDeEntrega: diasDeEntrega]
    }

    /*
    private void enviarEmail(Venda venda){
        
        mailService.sendMail {
          to "lsimaocosta@gmail.com"
          subject "Nova venda - #${venda.id} - ${venda.cliente.nome}"
          body( view:"/venda/showFull", model:[numeroPedido: String.format("%05d", venda.id) , venda : venda,
            enderecoEntrega: venda.cliente.endereco,itens: venda.itensVenda?:venda.carrinho.itens,
            cliente: venda.cliente, subTotal: venda.subTotalItensEmReais, 
            detalhesPagamento: venda.detalhesPagamento, frete: venda.freteEmReais,desconto: venda.descontoEmReais,
            valorTotal:venda.valorTotal,dataEntrega: venda.dataEntrega])
        }

    }*/

    private boolean validarDataEntrega(List datasDeEntrega, Date dataEscolhida){
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

    def fecharVenda(){


       if (params.dataEntrega){

            try{
            
                params.dataEntrega = new Date( Long.valueOf(params.dataEntrega) ) 
            
            }catch(Exception e){

                e.printStackTrace()
                flash.messageDataEntrega = "A data da entrega deve ser uma data válida" 
                redirect(action: "checkout") 
                return 

            }
        }

    

        def valorAPrazo = somarValorTotalAPrazoDoCarrinho()
        def valorAVista = somarValorTotalAVistaDoCarrinho()
        List diasDeEntrega = getProximosDiasDeEntrega()
        def itens = shoppingCartService.itens
        def frete = 25   
        def taxaEntrega = 0
        def desconto = valorAPrazo - valorAVista
        def totalAPrazo = valorAPrazo + frete
        def totalAVista = valorAVista + frete 

        def venda = new Venda(params)
        
        def user = (springSecurityService.currentUser)?:Usuario.findByUsername(params.cliente.email)
        
        if (user){
            venda.cliente = Cliente.findByUsuario(user)
            venda.cliente.properties = params.cliente
        }else{
            venda.cliente.usuario.enabled = true
            venda.cliente.senha = '12345'
        }

        if (venda.cliente.isDentroDaAreaDeEntregaRapida()){
            taxaEntrega = 2.5
            totalAPrazo += taxaEntrega
            totalAVista += taxaEntrega
        }
        
        def model = [totalAPrazo:totalAPrazo,
                     totalAVista:totalAVista,
                     valorAPrazo:valorAPrazo,taxaEntrega:taxaEntrega,
                     venda: venda,itens: itens, 
                     frete: frete, desconto:desconto,valorAVista:valorAVista, 
                     diasDeEntrega: diasDeEntrega]


        if (!venda.cliente.validate()){ 
            flash.message = "Amiga, os campos em destaque devem ser preenchidos" 
            render(view: "checkout",model:model) 
            return              
        }

     
        if ( !venda.cliente.isDentroDaAreaDeEntregaRapida() && venda.formaPagamento != FormaPagamento.PagSeguro  ){

            flash.message = "Forma de pagamento inválida!" 
            render(view: "checkout",model:model) 
            return                
        }


        if ( venda.cliente.isDentroDaAreaDeEntregaRapida() ){

            if (venda.dataEntrega){

                if (!validarDataEntrega(diasDeEntrega,venda.dataEntrega)){
                    flash.messageDataEntrega = "Apenas as datas apresentadas são aceitas" 
                    render(view: "checkout",model:model) 
                    return                      
                }

            }else{
                flash.messageDataEntrega = "A data da entrega deve ser selecionada" 
                render(view: "checkout",model:model) 
                return                
            }

        }



        venda.subTotalItensEmCentavos = valorAPrazo*100
        venda.carrinho = shoppingCartService.shoppingCart
        venda.status = StatusVenda.AguardandoPagamento
        venda.itensVenda = shoppingCartService.checkOut()

        if (!venda.cliente.isDentroDaAreaDeEntregaRapida()){
            venda.freteEmCentavos = frete*100    
        }

        if ( venda.formaPagamento == FormaPagamento.AVista ){
            venda.descontoEmCentavos = desconto*100    
        }  

        venda.cliente.save()
        
        springSecurityService.reauthenticate(venda.cliente.email)  

        if ( venda.formaPagamento == FormaPagamento.AVista ){

            venda.carrinho.checkedOut = true
            venda.carrinho.save()
            venda.save(flush:true)
            

            redirect(action:'show',controller:'venda', id:venda.id)
            return

        }else{
            

            venda.save(flush:true) // salva logo, pois precisa do ID da venda para registrar com a transação de pagamento do pagseguro
                // não elimina o carrinho ainda, pq nao sabe se vai dar certo. So vai eliminar no retorno do pag seguro
            
            def paymentURL = null

            try{
                
                paymentURL = venda.getPaymentURL()

                redirect(url:paymentURL)
                return
                
            }catch(PagSeguroServiceException e){

                println "Erro ao tentar ir para o pagseguro : cliente ${venda.cliente.id} "
                Iterator itr = e.getErrorList().iterator();  
      
                while (itr.hasNext()) {  
      
                    Error error = (Error) itr.next();  
      
                    System.out.println("Código do erro: " + error.getCode());  
      
                    System.out.println("Mensagem de erro: " + error.getMessage());  
      
                }  
                
                e.printStackTrace()

                venda.delete() // não deu pra mandar pro pag seguro ... exclui venda 
                
                flash.message = e.toString() 
                render(view: "checkout",model:model)
                return    
            }

        }        
        
    }
}
