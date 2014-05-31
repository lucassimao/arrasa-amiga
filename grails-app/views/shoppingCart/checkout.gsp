<%@ page import="br.com.arrasaamiga.Produto" %>
<%@ page import="br.com.arrasaamiga.FormaPagamento" %>
<%@ page import="br.com.arrasaamiga.Uf" %>
<%@ page import="br.com.arrasaamiga.Cidade" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>

		<style type="text/css">
			table th, table td {
				text-align: center !important;
				vertical-align: middle !important;
			}

			.forma-pagamento-selecionado, .data-entrega-selecionada, .frete-selecionado{
				font-weight: bold;
			}
		</style>


		<g:javascript>


			function recalcularValorVenda(){
				var formaPagamento =  $("input[name='formaPagamento']:checked").val();
				var cidadeId = $("#select-cidade option:selected").val();
				var ufId = $("#select-uf option:selected").val();
				var cep =  $("input[name='cliente.endereco.cep']").val();
				var servicoCorreio = $("input:radio[name='servicoCorreio']:checked").val();


				$('#div-financeiro').css('display','none');

				<g:remoteFunction action="recalcularTotais" onComplete="\$('#div-financeiro').fadeIn(500);"
								  update="div-financeiro" 
								  params="{formaPagamento : formaPagamento, cidadeId:cidadeId, uf: ufId, cep:cep,servicoCorreio: servicoCorreio }" />				
			}
			
					
			$(function(){

				$("#select-cidade").change(function(){

					var cidadeId = Number($("#select-cidade option:selected").val());

					if ( cidadeId === ${Cidade.teresina.id} ){

						$("#div-pagamento-avista").css('display','block');
						$("#div-cep").css('display','none');
						$("#div-escolher-frete").css('display','none');
						$("#div-entrega-teresina").css('display','block');

					}else{

						$("#div-pagamento-avista").css('display','none');
						$("#div-pagamento-pagseguro input").click();
						$("#div-cep").css('display','block');
						$("#div-cep input").focus();
						$("#div-escolher-frete").css('display','block');
						$("#div-entrega-teresina").css('display','none');
						$("#messageDataEntregaFlash").css('display','none');

					}	

					recalcularValorVenda();
				});


				$("#select-uf").change(function(){
					var idUf = $(this).val();

					$.ajax({
						
						url: "${createLink(controller:'shoppingCart',action:'getCidades',absolute:true)}",
						data: {'idUf': idUf},
						settings: {'cache':true}

					}).success(function( data, textStatus, jqXHR ) {

						$("#select-cidade").empty();

						$.each(data,function(index,objCidade){

							var nomeCidade = objCidade.nome;
							var idCidade = objCidade.id;
							
							var option = $("<option/>").text(nomeCidade).attr("value",idCidade);
							
							<g:if test="${venda?.cliente?.endereco?.cidade}">
								if (idCidade === ${venda?.cliente?.endereco?.cidade?.id}){
									$(option).attr('selected',true);
								}
							</g:if>
							
							$("#select-cidade").append(option);


						});

						$("#select-cidade").change();

					
					}).fail(function(){
						window.location = "${createLink(controller:'shoppingCart',action:'checkout',absolute:true)}"

					});
				});



				$("input[name='dataEntrega']").click(function(){

					$("span.data-entrega-selecionada").toggleClass("data-entrega-selecionada");

					var span = $(this).next();
					$(span).toggleClass("data-entrega-selecionada");
					

				});

				$("#div-cep input").change(function(){
					recalcularValorVenda();
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


				$("#select-uf").change();		

				<g:if test="${venda?.cliente?.hasErrors()}">
				    $('html, body').animate({
				        scrollTop: $("#anchor-form-informacoes-entrega").offset().top - 45
				    }, 1500);

				    $("#form-fechar-venda .error input").first().focus();

				</g:if>

				<g:if test="${flash.messageDataEntrega}">

				    $('html, body').animate({
				        scrollTop: $("#anchor-entrega-teresina").offset().top - 45
				    }, 1500);

				</g:if>



			});



		</g:javascript>

	</head>


	<body>


			<hr>

			<a href="#" name="msg"></a>

			<div class="well" style="background-color:#F29BF2;color:white;border:1px solid white;">
				<h2>  Entrega e Pagamento  </h2>
				<h5>  Visualize os detalhes do seu pedido, preencha as informações para entrega e escolha uma forma de pagamento  </h5>
			</div>

			<hr>

			<g:set var="ocultarRodape" value="${true}" scope="request"/>

			

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
						<g:each in="${venda.itensVenda}" var="itemVenda">
							<g:set var="produto" value="${itemVenda.produto}"/>
							
							<tr>
								<td style="text-align:left !important;">
									<g:img dir="img/produtos" style="float:left;" file="${produto.fotoMiniatura}"/>

									<div>
										<label>${produto.nome}</label>

										<g:if test="${produto.isMultiUnidade()}">
											<p> <small> ${produto.tipoUnitario}: ${itemVenda.unidade} </small> </p>
										</g:if>

									</div>
								</td>



								<td style="position:relative;">

									<p> <span class="badge badge-warning">${itemVenda.quantidade}</span>  </p>

								</td>
								
								<td>
									<g:formatNumber number="${itemVenda.precoAPrazoEmReais}" type="currency" currencyCode="BRL" />
								</td>
								
								<td>
									<g:formatNumber number="${itemVenda.subTotalAPrazo}" type="currency" currencyCode="BRL" />
								</td>

							</tr>

						</g:each>
									
					</tbody>
				</table>

			</div>	


			<a href="#" id="anchor-form-informacoes-entrega"></a>

			<g:if test="${flash.message}">
				<div class="alert alert-info">
				   <button type="button" class="close" data-dismiss="alert">&times;</button>
				   <i class=" icon-info-sign"></i>
				   ${flash.message} 
				</div>
			</g:if>

			

			<g:form action="fecharVenda" name="form-fechar-venda">

				<div class="well" style="background-color:white;">

					<legend> <i class="icon-truck"></i> Informações para Entrega </legend>

					<div class="row-fluid">
						<div class="span12">
	
						    <fieldset>
						    	 
						    	  <g:if test="${venda?.cliente?.usuario?.id}">
						    	  		<g:hiddenField value="${venda.cliente.usuario.id}" name="cliente.usuario.id"/> 
						    	  </g:if>

						          <div class="control-group ${hasErrors(bean: venda.cliente, field: 'nome', 'error')}">
						              <label style="font-weight:bold;"> Nome: </label>
						              <input type="text" value="${venda?.cliente?.nome}" name="cliente.nome" 
						              		placeholder="Nome completo …" class="input-xxlarge">

						          </div>

						          <div class="control-group ${hasErrors(bean: venda.cliente, field: 'email', 'error')}">
						              <label style="font-weight:bold;"> E-mail: </label>
						              <input class="input-xlarge" value="${venda?.cliente?.email}" name="cliente.email" type="text" >
						          </div>

						          <div style="float:left;" class="control-group ${hasErrors(bean: venda.cliente, field: 'dddTelefone', 'error')}">
						              <label style="font-weight:bold;"> Telefone: </label>
						              <input type="text" value="${venda?.cliente?.dddTelefone}" placeholder="DDD" name="cliente.dddTelefone" maxlength="2" class="input-mini" >

						          </div>

						          <div style="float:left;margin-right:10px;" class="control-group ${hasErrors(bean: venda.cliente, field: 'telefone', 'error')}">
						          		<input style="margin-top:25px;margin-left:5px;" type="text" value="${venda?.cliente.telefone}" placeholder="numero" maxlength="9" name="cliente.telefone" class="input-small">
						          </div>						          

						          <div style="float:left;"  class="control-group ${hasErrors(bean: venda.cliente, field: 'dddCelular', 'error')}">
						              <label style="font-weight:bold;"> Celular: </label> 
						              <input type="text" value="${venda?.cliente.dddCelular}" placeholder="DDD" maxlength="2" name="cliente.dddCelular" class="input-mini" >
						          </div>

						          <div  class="control-group ${hasErrors(bean: venda.cliente, field: 'celular', 'error')}">
						          		<input style="margin-top:25px;margin-left:5px;" type="text" value="${venda?.cliente.celular}" placeholder="numero" maxlength="9" name="cliente.celular" class="input-small">
						          </div>




						          <div style="clear:both;float:left;margin-right:10px;" class="control-group ${hasErrors(bean: venda.cliente, field: 'endereco.uf', 'error')}">
						              <label style="font-weight:bold;"> Estado: </label>

						              <g:select class="input-medium" value="${ (venda?.cliente?.endereco?.uf?.id)?:Uf.get(17).id }" 
						              		name="cliente.endereco.uf.id" id="select-uf" 
						              		optionValue="nome" optionKey="id" from="${Uf.list()}" />


						          </div>

						          <div style="float:left;margin-right:10px;" class="control-group ${hasErrors(bean: venda.cliente, field: 'endereco.cidade', 'error')}">
						              <label style="font-weight:bold;"> Cidade: </label>

						              <g:select class="input-medium" value="${venda?.cliente?.endereco?.cidade?.id}" 
						              			name="cliente.endereco.cidade.id" id="select-cidade" from="${[]}" />

						          </div>


						          <div style="float:left;margin-right:10px;" class="control-group ${hasErrors(bean: venda.cliente, field: 'endereco.bairro', 'error')}">
						              <label style="font-weight:bold;"> Bairro: </label>
						              <input name="cliente.endereco.bairro"  value="${venda?.cliente?.endereco?.bairro}" type="text" >
						          </div>

						          <div id="div-cep" style="display:none;" class="control-group ${hasErrors(bean: venda.cliente, field: 'endereco.cep', 'error')}">
						              
						              <label style="font-weight:bold;"> CEP: </label>
						              <input class="input-small" name="cliente.endereco.cep" value="${ venda?.cliente?.endereco?.cep }" type="text" >

						          </div>						          

						          <div style="clear:both;" class="control-group ${hasErrors(bean: venda.cliente, field: 'endereco.complemento', 'error')}">
						          	<label style="font-weight:bold;"> Complemento: </label>
						          	<input class="input-xxlarge" value="${venda?.cliente?.endereco?.complemento}" 
						          		placeholder="casa, quadra, apartamento, rua, número, ponto de referência ... "  
						          		name="cliente.endereco.complemento" type="text" >
						          </div>

						    </fieldset>

						</div>

					</div>

				</div>

				<a href="#" id="anchor-entrega-teresina"></a>

				<g:if test="${flash.messageDataEntrega}">
					<div id="messageDataEntregaFlash" class="alert alert-info">
					   <button type="button" class="close" data-dismiss="alert">&times;</button>
					   <i class=" icon-info-sign"></i>
					   ${flash.messageDataEntrega} 
					</div>
				</g:if>


				<div id="div-entrega-teresina" class="well" style="display:none;background-color:white;">

					<legend> <i class="icon-calendar"></i> Entrega </legend>

					<p style="text-align:justify;font-weigth:bold;text-indent:20px;"> 						
						As entregas são feitas em domicílio a partir das 17:30 nas terças e quintas, e nos sábados pela manhã! Selecione uma das seguintes datas para receber seu pedido:
					</p>


					<g:each in="${diasDeEntrega}" var="diaDeEntrega">

						<label class="radio inline">
							<input type="radio" value="${diaDeEntrega.time}" name="dataEntrega" > 
							<span> <g:formatDate format="EEEE, dd/MM/yyyy" date="${diaDeEntrega}"/> </span>
						</label>

					</g:each>
					
				</div>


				<div id="div-escolher-frete" class="well" style="display:none;background-color:white;">

					<legend style="background-image:url(${resource(dir:'img',file:'correios_logo.jpg')});background-repeat:no-repeat;padding-left:35px;"> Frete </legend>

					<p style="text-align:justify;font-weigth:bold;text-indent:20px;"> 						
						Escolha o tipo de frete:
					</p>

					<div style="margin-left:20px;">
						<label class="radio inline">
							<input type="radio" value="PAC" checked name="servicoCorreio" > 
							<span> PAC </span>
						</label>

						<label class="radio inline">
							<input type="radio" value="SEDEX" name="servicoCorreio"> 
							<span> SEDEX ( 2 dias úteis )</span>
						</label>
					</div>
					
				</div>


						

				<div id="div-financeiro" class="well" style="background-color:white;">

				</div>



				<div class="well" style="background-color:white;">

					<legend> <i class="icon-money"></i> Forma de Pagamento </legend>

					
						<div class="row-fluid">


							<div id="div-pagamento-avista" class="span5 well" style="padding-right:0px;">
								<label class="radio">
									<input  name="formaPagamento" type="radio" value="AVista" ${venda.formaPagamento.equals(FormaPagamento.AVista)?'checked':''}>
									<span> Pagamento em Dinheiro </span>
								</label>
								
								<p> 
									Pagamento na entrega da sua encomenda
								</p>
								<p> 
									<em> ** Tem desconto amiga ! </em> 
								</p>
							</div>


							<div id="div-pagamento-pagseguro" class="span7 well pull-right" style="padding-right:0px;">
								<label class="radio">
									<input name="formaPagamento" type="radio" value="PagSeguro" ${venda.formaPagamento.equals(FormaPagamento.PagSeguro)?'checked':''} > 
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
		            	<input type="submit" style="display:none;"/>

		                <a href="${createLink(action:'index')}" class="btn btn-success">
		                    <i class="icon-shopping-cart icon-white"></i>
		                    Voltar para o carrinho de compras
		                </a>	                
		                <button type="submit" style="float:right;" id="btnFecharVenda" class="btn btn-success">

		                   <i class="icon-ok-circle icon-white"></i>  Confirmar Pedido
		                </button>
		            </div>
		        </div>

	    </g:form>

	</body>
</html>



