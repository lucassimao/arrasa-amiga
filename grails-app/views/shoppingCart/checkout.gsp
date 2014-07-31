<%@ page import="br.com.arrasaamiga.Produto" %>
<%@ page import="br.com.arrasaamiga.FormaPagamento" %>
<%@ page import="br.com.arrasaamiga.Uf" %>
<%@ page import="br.com.arrasaamiga.Cidade" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>

		<asset:script>


			function recalcularValorVenda(){
				var formaPagamento =  $("input[name='formaPagamento']:checked").val();
				var servicoCorreio = $("input:radio[name='servicoCorreio']:checked").val();

				$('#div-financeiro').css('display','none');

				<g:remoteFunction action="recalcularTotais" onComplete="\$('#div-financeiro').fadeIn(500);ocultarLoading();" before="mostrarLoading()"
					update="div-financeiro" params="{formaPagamento : formaPagamento, servicoCorreio: servicoCorreio }" />				
			}

			function mostrarLoading(){
				$('#div-loading').css('display','block');
			}			

			function ocultarLoading(){
				$('#div-loading').css('display','none');
			}
			
					
			$(function(){


				$("input[name='dataEntrega']").click(function(){

					$("span.data-entrega-selecionada").toggleClass("data-entrega-selecionada");

					var span = $(this).next();
					$(span).toggleClass("data-entrega-selecionada");
					
				});


				$("input[name='formaPagamento']").click(function(){

					$("span.forma-pagamento-selecionado").toggleClass("forma-pagamento-selecionado");
					
					var label = $(this).next();
					$(label).toggleClass("forma-pagamento-selecionado");

					recalcularValorVenda();
					
				});

				$("input[name='servicoCorreio']").click(function(){

					$("span.frete-selecionado").toggleClass("frete-selecionado");
					
					var label = $(this).next();
					$(label).toggleClass("frete-selecionado");

					recalcularValorVenda();
				});


				recalcularValorVenda();	

				<g:if test="${flash.messageDataEntrega}">

				    $('html, body').animate({
				        scrollTop: $("#anchor-entrega-teresina").offset().top - 45
				    }, 1500);

				</g:if>

			});



		</asset:script>

	</head>


	<body>




            <div class="col-md-12">           

              <h3>Entrega e Pagamento</h3>
			  <h5> Visualize os detalhes do seu pedido, escolha a forma de pagamento e a data de entrega </h5>

              <hr />

              <g:if test="${flash.message}">
                <div class="alert alert-info">
                   <button type="button" class="close" data-dismiss="alert">&times;</button>
                   ${flash.message}
                </div>
              </g:if>              
                
              <table class="table table-bordered table-striped table-condensed">
               <thead>
                <tr>
                  <th class="col-sm-1">Produto</th>
                  <th>Descrição</th>
                  <th class="col-sm-1">Preço</th>
                  <th class="col-sm-1">Quantidade</th>
                  <th class="col-sm-1">Total</th>
                  <th class="col-sm-1">Excluir</th>
                </tr>
               </thead>
               <tbody>
                <!-- renderizando items -->
                <g:each in="${venda.itensVenda}" var="item">
                    <g:set var="produto" value="${item.produto}"/>
                    <tr>
                      <td> 
                        <a href="${createLink(uri:produto.nomeAsURL,absolute:true)}">
                          <asset:image src="produtos/${produto.fotoMiniatura}" alt="${produto.nome}" title="${produto.nome}" class="img-cart" />
                        </a>
                      </td>
                      <td>
                        <a href="${createLink(uri:produto.nomeAsURL,absolute:true)}">
                          <strong>${produto.nome} </strong> ${ (produto.marca)? (" - " + produto.marca):''}
                        </a>
                        <g:if test="${produto.unidades.size() > 1}">
                          <p>${produto.tipoUnitario} : ${item.unidade}</p>
                        </g:if>
                      </td>
                      <td><g:formatNumber number="${item.precoAPrazoEmReais}" type="currency" currencyCode="BRL" /></td>
                      <td>
                        <p style="text-align:center;font-size:15px;"> 
                          <span class="badge alert-success">${item.quantidade}</span>  
                        </p>
                      </td>
                      <td><g:formatNumber number="${item.subTotalAPrazo}" type="currency" currencyCode="BRL" /></td>
                      <td>

                            <g:form class="form-inline" action="removerProduto" controller="shoppingCart">
                              <g:hiddenField name="id" value="${produto.id}"/>
                              <g:hiddenField name="unidade" value="${item.unidade}"/>
                              <g:hiddenField name="quantidade" value="${item.quantidade}"/>

                              <button type="submit" class="btn btn-primary">
                                <i class="fa fa-trash-o"></i>
                              </button>                          
                            </g:form> 

                      </td>
                    </tr>
                </g:each>

                <tr>
                  <td colspan="6">&nbsp;</td>
                </tr>              
               </tbody>

              </table>


		  <g:form action="fecharVenda">

			<div class="well" style="background-color:white;">

				<legend> <i class="fa fa-money"></i> Forma de Pagamento </legend>

				<div class="row" style="padding:5px;">

					<g:if test="${venda.cliente.isFromTeresina()}">
						<div id="div-pagamento-avista" class="col-md-6" >
							<label class="radio" style="cursor:pointer;">
								<input  name="formaPagamento" type="radio" value="AVista" ${venda.formaPagamento.equals(FormaPagamento.AVista)?'checked':''}>
								<span> Pagamento em Dinheiro </span>
							</label>
							<p> Pagamento na entrega da sua encomenda </p>
							<p> <em> ** Tem desconto amiga ! </em> </p>
						</div>
					</g:if>

					<div id="div-pagamento-pagseguro" class="col-md-6">
						<label class="radio" style="cursor:pointer;">
							<input name="formaPagamento" type="radio" 
								value="PagSeguro" ${!venda.cliente.isFromTeresina() || venda.formaPagamento.equals(FormaPagamento.PagSeguro)?'checked':''} > 
							<span class="forma-pagamento-selecionado"> Cartão de Crédito / Boleto Bancário </span>						
						</label>
						<p> A compra será finalizada na próxima tela </p>
						<p> <em> ** Desconto para boleto bancário **</em> </p>
					</div>
				</div>
					
			</div>


			<div id="div-financeiro" class="well" style="background-color:white;">
			</div>
			
			<div id="div-loading" class="well" style="background-color:white;text-align:center;display:none;">
				<asset:image src="ajax-loader.gif"/>
			</div>

			<g:if test="${venda.cliente.isFromTeresina()}"> 

				<a href="#" id="anchor-entrega-teresina"></a>

				<g:if test="${flash.messageDataEntrega}">
					<div id="messageDataEntregaFlash" class="alert alert-info">
					   <button type="button" class="close" data-dismiss="alert">&times;</button>
					   <i class=" icon-info-sign"></i>
					   ${flash.messageDataEntrega} 
					</div>
				</g:if>         

				<div id="div-entrega-teresina" class="well" style="background-color:white;">
					<div class="row" style="padding:5px;">
						<legend style="padding-bottom:5px;"> 
							<i class="fa fa-calendar"></i> Entrega 
						</legend>
						<p style="text-align:justify;font-weigth:bold;"> 						
							As entregas são feitas em domicílio a partir das 16:30. Selecione uma das seguintes datas para receber seu pedido:
						</p>
						<g:each in="${diasDeEntrega}" var="diaDeEntrega">
							<div class="col-md-4">
								<label class="radio" style="display: inline-block;cursor:pointer;">
									<input type="radio" value="${diaDeEntrega.time}" name="dataEntrega" > 
									<span> <g:formatDate format="EEEE, dd/MM/yyyy" date="${diaDeEntrega}"/> </span>
								</label>
							</div>
						</g:each>
					</div>
				</div>

			</g:if>
			<g:else>	

				<div id="div-escolher-frete" class="well" style="background-color:white;">

					<legend style="padding-bottom:5px;"> Frete para ${venda.cliente.endereco.cidade.nome}</legend>
					<p style="text-align:justify;font-weigth:bold;text-indent:20px;"> 						
						Escolha o tipo de frete:
					</p>

					<div style="margin-left:20px;">
						<label class="radio" style="display: inline-block;">
							<input type="radio" value="PAC" checked name="servicoCorreio" > 
							<span> PAC </span>
						</label>
						<label class="radio" style="display: inline-block;margin-left:10px;">
							<input type="radio" value="SEDEX" name="servicoCorreio"> 
							<span> SEDEX ( 3 dias úteis )</span>
						</label>
					</div>	
				</div>

			</g:else>


	 
	      	<a href="${createLink(uri:'/',absolute:true)}" class="btn btn-primary">
	      		<i class="fa fa-angle-double-left"></i> Escolher + produtos
	      	</a>
	      	<button type="submit" class="btn btn-success pull-right">Concluir Pedido</button>

	      </g:form>

        </div>



	</body>
</html>



