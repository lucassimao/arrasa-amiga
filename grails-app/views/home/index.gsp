<%@ page import="br.com.arrasaamiga.Produto" %>

<!DOCTYPE html>
<html>
	<head>

		<title> Arrasa Amiga: Produtos para maquiagem à pronta entrega </title>
		<parameter name="description" value="Produtos para maquiagem à pronta entrega" />
		<parameter name="keywords" value="Maquiagem, blushes, NYX, batom, sombra, paleta,Teresina, Piauí, maquiadora" />
		<parameter name="og:image" value="${assetPath(src: 'top.jpg')}"/>
		<meta name="layout" content="main"/>

	</head>


	<body>

		<!-- mais vendidos, novidades -->
		<g:set var="count" value="${0}"/>
		<g:set var="hoje" value="${new Date()}"/>
		<g:set var="qtdeProdutosVisiveis" value="${ produtos.size() }"/>

		
		<div class="row">
			<div class="col-md-12">
			  <div class="page-header">
			     <!-- <h2> Mais procurados <small> Most sold product in this month</small></h2> -->
			  </div>
			</div>
		</div>

		<g:each in="${produtos}" var="produto" status="i">

			<g:if test="${ count == 0 }">
				<div class="row product-container">
			</g:if>


				<div class="col-md-3 col-sm-3 col-xs-6">
				  <div class="thumbnail product-item">
				    <a href="${createLink(uri:produto.nomeAsURL, absolute:true)}"> 
				    	<asset:image src="produtos/${produto.fotoMiniatura}" alt="${produto.nome}" title="${produto.nome}"/>
				    </a>


				    <div class="caption">
				      <h5>  ${produto.nome}</h5>
				      <p> <g:formatNumber number="${produto.precoAPrazoEmReais}" type="currency" currencyCode="BRL" /> </p>
				      <!-- <p> * Desconto a vista </p> -->
				    </div>
				    
				    <g:if test="${ produto.dateCreated && ( (produto.dateCreated - hoje) < 30 ) }">
				    	<div class="product-item-badge badge-sale">Novo</div>
				    </g:if>
					
					<div class="buy-item"> 
						<a class="btn btn-primary" href="${createLink(uri:produto.nomeAsURL, absolute:true)}"> 
							<span><i class="fa fa-shopping-cart"></i> Comprar </span> 
						</a> 
					</div>
				  </div>
				</div>


			<g:set var="count" value="${++count}"/>

			<g:if test="${(i+1) == qtdeProdutosVisiveis && count < 4}">

				<g:each in="${1..(4-count)}">
					<g:set var="count" value="${++count}"/>
					<div class="col-md-3 col-sm-3 col-xs-6"> </div>
				</g:each>

			</g:if>

  			<g:if test="${ count == 4 }">
				</div>
				<g:set var="count" value="${0}"/>
			</g:if>

		</g:each>


		<!-- end:best-seller -->

	</body>
</html>



