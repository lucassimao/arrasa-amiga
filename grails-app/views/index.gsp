<%@ page import="br.com.arrasaamiga.Produto" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
	</head>
	<body>

			<hr>
			
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

						<p><a class="btn btn-primary icon-shopping-cart"  style="left:23%;position:absolute;bottom: 10px;" href="#"> Comprar </a></p>
					</div>
					
					<g:set var="count" value="${++count}"/>


      			<g:if test="${ count == 4 || (i+1) == Produto.count()}">
					</div>
					<g:set var="count" value="${0}"/>
				</g:if>
			</g:each>
		
	</body>
</html>
