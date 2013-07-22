<%@ page import="br.com.arrasaamiga.Produto" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>

		<g:javascript>
			$(function(){
				$(".alert.alert-success").fadeOut(4000);
			});
			
		</g:javascript>

	</head>


	<body>

			<hr>

			<g:if test="${flash.message}">
				<div class="alert alert-success">
					${flash.message} 

				</div>
			</g:if>
			
      		<g:set var="count" value="${0}"/>

      		<g:each in="${Produto.list()}" var="produto" status="i">

      			<g:if test="${ count == 0 }">
					<div class="row-fluid" style="margin-bottom:50px;">
				</g:if>

					
					<div class="span3" style="padding:5px;text-align:center;height:270px;border:1px solid orange;position:relative;">
						
						<g:img dir="img/produtos" file="${produto.fotos[0]}"/>

						<g:link action='index' controller='produto' id="${produto.id}"> 
							<h5> ${produto.nome} </h5>
						</g:link>
						
						<p style="left:35%;color:#ad96a5;font-size:20px;position:absolute;bottom: 40px;"> R$ ${produto.precoEmReais} </p>

						<p>
							<g:remoteLink style="left:26%;position:absolute;bottom: 10px;" params="['quantidade':1]" id="${produto.id}" onSuccess="document.location.reload();"
							class="btn btn-primary icon-shopping-cart" controller="produto" action="addToShoppingCart"> Comprar</g:remoteLink>  
						</p>

					</div>
					
					<g:set var="count" value="${++count}"/>


      			<g:if test="${ count == 4 || (i+1) == Produto.count()}">
					</div>
					<g:set var="count" value="${0}"/>
				</g:if>
			</g:each>
		
	</body>
</html>



