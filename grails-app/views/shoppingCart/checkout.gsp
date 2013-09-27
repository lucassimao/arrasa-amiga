<%@ page import="br.com.arrasaamiga.Produto" %>
<%@ page import="br.com.arrasaamiga.FormaPagamento" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>

		<style type="text/css">
			table th, table td {
				text-align: center !important;
				vertical-align: middle !important;
			}

			.forma-pagamento-selecionado, .data-entrega-selecionada{
				font-weight: bold;
			}
		</style>


		<g:javascript>
					
			$(function(){




				$("input[name='formaPagamento']").click(function(){

					$("span.forma-pagamento-selecionado").toggleClass("forma-pagamento-selecionado");

					var span = $(this).next();
					$(span).toggleClass("forma-pagamento-selecionado");
					

				});


				$("input[name='dataEntrega']").click(function(){

					$("span.data-entrega-selecionada").toggleClass("data-entrega-selecionada");

					var span = $(this).next();
					$(span).toggleClass("data-entrega-selecionada");
					

				});

				$("#btnFecharVenda").click(function(){

					$("form").submit();

				});

				$("input[name='formaPagamento']").click(function(){

					var formaPagamento = $(this).attr("value");

					switch(formaPagamento){
						case "AVista":

							$("#div-valor-total-a-prazo").css("display","none");
							$("#div-desconto").css("display","block");
							$("#div-valor-total-a-vista").css("display","block");

							break;
						
						case "PagSeguro":

							$("#div-desconto").css("display","none");
							$("#div-valor-total-a-vista").css("display","none");
							$("#div-valor-total-a-prazo").css("display","block");

							break;
						default:
							// wtf ??
					}
					
					
				});


				$("input[name='formaPagamento'][checked]").click();

			});


		</g:javascript>

	</head>


	<body>


			<hr>


			<div class="well" style="background-color:#F29BF2;color:white;border:1px solid white;">
				<h2>  Entrega e Pagamento  </h2>
				<h5>  Visualize os detalhes do seu pedido, confirme seu 
				endereço de entrega e escolha uma forma de pagamento  </h5>
			</div>

			<hr>

			<g:set var="ocultarRodape" value="${true}" scope="request"/>

			<g:if test="${flash.message}">
				<div class="alert alert-info">
				   <button type="button" class="close" data-dismiss="alert">&times;</button>
				   <i class=" icon-info-sign"></i>
				   ${flash.message} 
				</div>
			</g:if>

			<div class="row-flow" style="margin:20px 0px;"> 

				<table class="table table-bordered" style="background-color:white;">
					<thead >
						<tr>
							<th> Descrição </th>
							<th> Quantidade </th>
							<th> Preço Unitário </th>
							<th> Valor Total do Item </th>
						</tr>							
					</thead>
					<tbody>
						<g:each in="${itens}" var="itemVenda">
							<g:set var="produto" value="${itemVenda.produto}"/>
							
							<tr>
								<td style="text-align:left !important;">
									<g:img dir="img/produtos" style="float:left;" file="${produto.fotoMiniatura}"/>

									<div>
										<label>${produto.nome }</label>

										<g:if test="${produto.isMultiUnidade()}">
											<p> <small> ${produto.tipoUnitario}: ${itemVenda.unidade} </small> </p>
										</g:if>

									</div>
								</td>



								<td style="position:relative;">

									<p> <span class="badge badge-warning">${itemVenda.quantidade}</span>  </p>

								</td>
								
								<td>
									<g:formatNumber number="${itemVenda.precoAPrazoEmReais}" type="currency" 
										currencyCode="BRL" />
								</td>
								
								<td>
									<g:formatNumber number="${itemVenda.subTotalAPrazo}" type="currency" 
										currencyCode="BRL" />
								</td>

							</tr>

						</g:each>
									
					</tbody>
				</table>

			</div>	

			<g:form action="fecharVenda">

				

				<div class="well" style="background-color:white;">

					<legend> <i class="icon-truck"></i> Entrega </legend>

					<div class="row-fluid">
						<div class="span4">
							<label> CEP: ${enderecoEntrega.cep} </label>

							<label> Estado: ${enderecoEntrega.uf}</label>

							<label> Cidade: ${enderecoEntrega.cidade}</label>

							<label> Bairro: ${enderecoEntrega.bairro} </label>

							<label> Endereço: ${enderecoEntrega.endereco}</label>

							<label> Complemento: ${enderecoEntrega.complemento}</label>

							<label> Ponto de Referência: ${enderecoEntrega.pontoDeReferencia}</label>
						</div>
						
						<g:if test="${enderecoEntrega.fromTeresina}">

							<div class="span8">
								<p style="font-family:Arial;text-align:justify;text-indent:20px;"> 						
								
									Amiga, você é de Teresina - Piauí <g:img dir="img" file="bandeira-piaui.png" /> !!! 
									Aqui nos fazemos entregas em domicílio,
									basta selecionar uma das seguintes datas para receber seu pedido:
								
								</p>

								<label class="radio inline">
									<input type="radio" value="${diasDeEntrega[0].time}" name="dataEntrega" > 
									<span> <g:formatDate format="EEEE, dd/MM/yyyy" date="${diasDeEntrega[0]}"/> </span>
								</label>

								<label class="radio inline">
									<input value="${diasDeEntrega[1].time}" type="radio" name="dataEntrega" > 
									<span> <g:formatDate format="EEEE, dd/MM/yyyy" date="${diasDeEntrega[1]}"/> </span>
								</label>

								<label class="radio inline">
									<input value="${diasDeEntrega[2].time}" type="radio" name="dataEntrega" >
									<span> <g:formatDate format="EEEE, dd/MM/yyyy" date="${diasDeEntrega[2]}"/> </span>
								</label>
								

								<hr>

								<p style="font-family:Arial;text-align:justify;">
									Informaçoes adicionais:
								</p>
								
								<textarea name="informacoesAdicionais" 
									placeholder="Informe o melhor horário ou um endereço alternativo para entregarmos seu pedido (como o seu endereço de trabalho, caso seja melhor)" 
									rows="4" style="min-width:480px;">${flash.informacoesAdicionaisEntrega}</textarea>
								
							</div>

						</g:if>

					</div>

				</div>



				<div class="well" style="background-color:white;">


					<div id="div-subtotal">
						<h4 style="display:inline;color:#666;">Subtotal</h4>

						<div style="float:right;font-weight:bold;font-size:15px;color:#666;"> 
							<g:formatNumber number="${valorAPrazo}" type="currency"	currencyCode="BRL" />
						</div>
					</div>

					<g:if test="${!enderecoEntrega.fromTeresina}">

						<div id="div-frete" style="clear:both;">
							<h5 style="display:inline;color:blue;">Frete</h5>

							<div style="float:right;font-weight:bold;border-bottom:1px solid red;"> 
								<div style="color:blue;font-size:10px;text-align:right;">
									+ <g:formatNumber number="${frete}" type="currency" currencyCode="BRL" />
								</div>
							</div>

						</div>

					</g:if>

					<div id="div-desconto" style="padding:0px 4px;;clear:both;display:none;background-color:yellow;">
						<h5 style="display:inline;color:blue;">Desconto</h5>

						<div style="float:right;font-weight:bold;"> 
							<div style="color:blue;font-size:11px;text-align:right;">
								- <g:formatNumber number="${desconto}" type="currency" currencyCode="BRL" />
							</div>
						</div>

					</div>


					<div id="div-valor-total-a-vista" style="display:none;">
						<hr>

						<h4 style="color:#666;display:inline;">Valor Total</h4>

						<div style="float:right;font-weight:bold;font-size:35px;color:#00adef;"> 
							<g:formatNumber number="${totalAVista}" type="currency"	currencyCode="BRL" />
						</div>
					</div>

					<div id="div-valor-total-a-prazo">
						<hr>
						
						<h4 style="color:#666;display:inline;">Valor Total</h4>

						<div style="float:right;font-weight:bold;font-size:35px;color:#00adef;"> 
							<g:formatNumber number="${totalAPrazo}" type="currency"	currencyCode="BRL" />
						</div>
					</div>

				</div>



				<div class="well" style="background-color:white;">

					<legend> <i class="icon-money"></i> Forma de Pagamento </legend>

					
						<div class="row-fluid">

							<g:if test="${enderecoEntrega.fromTeresina}">

								<div class="span5 well" style="padding-right:0px;">
									<label class="radio">
										<input  name="formaPagamento" type="radio" value="AVista" >
										<span> Pagamento em Dinheiro </span>
									</label>
									
									<p> 
										Pagamento na entrega da sua encomenda
									</p>
									<p> 
										<em> ** Tem desconto amiga ! </em> 
									</p>
								</div>

							</g:if>

							<div class="span7 well" style="padding-right:0px;">
								<label class="radio">
									<input name="formaPagamento" type="radio" value="PagSeguro" checked > 
									<span class="forma-pagamento-selecionado"> Cartão de Crédito / Transferência Bancária / Boleto Bancário </span>								
								</label>
								<p> 
									A compra será finalizada na próxima tela
								</p>
								<p> 
									<em> ** Desconto para transferência bancária e boleto bancário, na próxima tela</em> 
								</p>
							</div>

						</div>


				</div>

			

				<hr>

				<div  class="row-fluid">
		            <div>
		                <a href="${createLink(action:'index')}" class="btn btn-success">
		                    <i class="icon-shopping-cart icon-white"></i>
		                    Voltar para o carrinho de compras
		                </a>	                
		                <a href="#" style="float:right;" id="btnFecharVenda" class="btn btn-success">
		                   <i class="icon-ok-circle icon-white"></i>
		                   Confirmar Pedido
		                </a>
		            </div>
		        </div>

	    </g:form>

	</body>
</html>



