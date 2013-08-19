<%@ page import="br.com.arrasaamiga.Produto" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
	</head>


	<body>

			<hr>

			<div id="toast">
				<img />
				<label/>
			</div>

      		<g:set var="count" value="${0}"/>

      		<g:each in="${Produto.list()}" var="produto" status="i">

      			<g:if test="${ count == 0 }">
					<div class="row-fluid" style="margin-bottom:20px;">
				</g:if>

					
					<div class="span3" style="padding:5px;text-align:center;height:270px;border:1px solid orange;position:relative;">
						
						<g:img dir="img/produtos" file="${produto.fotoMiniatura}"/>


						<a href="${createLink(uri:produto.nomeAsURL, absolute:true)}"> 
							<h5> ${produto.nome} </h5>
						</a>
						
						<p style="left:35%;color:#ad96a5;font-size:20px;position:absolute;bottom: 40px;"> R$ ${produto.precoAPrazoEmReais} </p>

						<p>
							<g:link style="left:26%;position:absolute;bottom: 10px;" id="${produto.id}" class="btn btn-primary icon-shopping-cart" 
									controller="shoppingCart" action="add1"> Comprar</g:link>  
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



