<%@ page import="br.com.arrasaamiga.Produto" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>

		<style type="text/css">
			table th, table td {
				text-align: center !important;
				vertical-align: middle !important;
			}
		</style>

		<g:javascript>
					
			$(function(){

				$("form a").click(function(event){

					event.preventDefault();
					$(this).parent().submit();
					
				});
			});


		</g:javascript>

	</head>


	<body>

			<hr>
			
			<g:set var="ocultarRodape" value="${true}" scope="request"/>

			<g:if test="${flash.message}">
				<div class="alert alert-info">
				   <button type="button" class="close" data-dismiss="alert">&times;</button>
				   ${flash.message}
				</div>
			</g:if>

			<div  class="row-fluid">
	            <div>
	                <a href="${createLinkTo(uri:'/',absolute:true)}" class="btn btn-success">
	                    <i class="icon-backward icon-white"></i>
	                    Escolher mais produtos
	                </a>	                
	                <a href="${createLink(controller:'shoppingCart',action:'checkout')}" style="float:right;" class="btn btn-primary">
	                  <!-- <i class="icon-credit-card icon-white"></i> -->
	                   Efetuar Pagamento
	                   <i class="icon-forward icon-white"></i>
	                </a>
	            </div>
	        </div>

			<div class="row-flow" style="margin:20px 0px;"> 

				<table class="table table-bordered" style="background-color:white;">
					<thead >
						<tr>
							<th> Descrição </th>
							<th> Quantidade </th>
							<th> Preço Unitário </th>
							<th> Valor Total do Item </th>
							<th> Excluir </th>
						</tr>							
					</thead>
					<tbody>
						<g:each in="${itens}" var="item">

							<g:set var="produto" value="${item.produto}"/>
							
							<tr>
								<td style="text-align:left !important;">
									<g:img dir="img/produtos" style="float:left;" file="${produto.fotoMiniatura}"/>

									<div>
										<a href="${createLink(controller:'produto',action:'detalhes',id:produto.id)}">${produto.nome }</a>

										<g:if test="${produto.isMultiUnidade()}">
											<p> <small> ${produto.tipoUnitario}: ${item.unidade} </small> </p>
										</g:if>

									</div>
								</td>

								<td style="position:relative;">

									<p style="display:block;margin-left:auto;margin-right:auto;"> 
										<span class="badge badge-warning">${item.quantidade}</span>  
									</p>

									<div style="width:80px;display:block;margin-left:auto;margin-right:auto;">
										<g:form action="add" controller="shoppingCart" style="float:left;">
											<g:hiddenField name="id" value="${produto.id}"/>
											<g:hiddenField name="unidade" value="${item.unidade}"/>
											<g:hiddenField name="quantidade" value="${1}"/>
											<g:hiddenField name="origem" value="/shoppingCart/index"/>

							                <a href="#" class="btn btn-info">
							                    <i class="icon-plus-sign icon-white"></i>
							                </a>
						                </g:form>

						                <g:form action="removerProduto" controller="shoppingCart">
											<g:hiddenField name="id" value="${produto.id}"/>
											<g:hiddenField name="unidade" value="${item.unidade}"/>
											<g:hiddenField name="quantidade" value="${1}"/>
											<g:hiddenField name="origem" value="/shoppingCart/index"/>

							           		<a href="#" class="btn btn-info">
							                    <i class="icon-minus-sign icon-white"></i>
							                </a>
						                </g:form>
					                </div>


								</td>
								
								<td>
									<g:formatNumber number="${item.precoAPrazoEmReais}" type="currency" currencyCode="BRL" />
								</td>
								
								<td>
									<g:formatNumber number="${item.getSubTotalAPrazo()}" type="currency" currencyCode="BRL" />
								</td>

								<td>

					                <g:form action="removerProduto" controller="shoppingCart">
										<g:hiddenField name="id" value="${produto.id}"/>
										<g:hiddenField name="unidade" value="${item.unidade}"/>

						                <a href="#" class="btn btn-danger">
						                    <i class="icon-trash icon-white"></i>
						                </a>
					                </g:form>



								</td>
							</tr>

						</g:each>
									
					</tbody>
				</table>

			</div>	

			<hr>

			

			<div class="well" style="background-color:white;">
			  <h4 style="display:inline;">Valor Total </h4>
			 
			  <span style="float:right;font-weight:bold;"> 
					<g:formatNumber number="${valorTotal}" type="currency" currencyCode="BRL" />	
			  </span>
			</div>

			<hr>

			<div  class="row-fluid">
	            <div>
	                <a href="${createLinkTo(uri:'/',absolute:true)}" class="btn btn-success">
	                    <i class="icon-backward icon-white"></i>
	                    Escolher mais produtos
	                </a>	                
	                <a href="${createLink(controller:'shoppingCart',action:'checkout')}" 
	                	style="float:right;" class="btn btn-primary">
	                   <!-- <i class="icon-credit-card icon-white"></i> -->
	                   Efetuar Pagamento
	                   <i class="icon-forward icon-white"></i>
	                </a>
	            </div>
	        </div>

	</body>
</html>



