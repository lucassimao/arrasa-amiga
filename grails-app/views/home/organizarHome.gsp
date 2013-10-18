<%@ page import="br.com.arrasaamiga.Produto" %>

<!DOCTYPE html>
<html>
	<head>

		<title> Arrasa Amiga: Produtos para maquiagem à pronta entrega </title>
		<parameter name="description" value="Produtos para maquiagem à pronta entrega" />
		<parameter name="keywords" value="Maquiagem, blushes, NYX, batom, sombra, paleta,Teresina, Piauí, maquiadora" />
		<parameter name="og:image" value="${resource(dir:'img',file:'top.jpg',absolute:true)}"/>


		<meta name="layout" content="main"/>

		<style type="text/css">

			.label-preco{
				left:30%;
				color:#ad96a5;
				font-size:20px;
				position:absolute;
				bottom: 65px;
				font-weight:bold;
			}

			.label-desconto-a-vista{
				left:10%;
				color:#ad96a5;
				font-size:12px;
				position:absolute;
				bottom: 38px;
			}

			.item-produto{
				padding:5px;
				text-align:center;
				height:300px;
				border:1px solid pink;
				background-color: white;
				position:relative;
				border-radius: 1em;
				box-shadow: pink 0.5em 0.5em 0.3em;
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


			<g:link style="margin-top:20px;" controller="produto" class=" btn btn-primary"> 
				<i class="icon-backward"> </i> Voltar
			</g:link>	

			<hr>
			
			<g:set var="ocultarRodape" value="${true}" scope="request"/>
			<g:set var="ocultarMenu" value="${true}" scope="request"/>

      		<g:set var="count" value="${0}"/>
      		<g:set var="qtdeProduto" value="${Produto.count()}"/>

      		<g:each in="${produtos}" var="produto" status="i">

      			<g:if test="${ count == 0 }">
					<div class="row-fluid" style="margin-bottom:20px;">
				</g:if>
					
				<div class="span3 item-produto">
					
					
					<a href="${createLink(uri:produto.nomeAsURL, absolute:true)}"> 
						<g:img dir="img/produtos" file="${produto.fotoMiniatura}" alt="${produto.nome}" title="${produto.nome}"/>
						<h5> ${produto.nome} </h5>
					</a>
					
					<p class="label-preco"> 
						<g:formatNumber number="${produto.precoAPrazoEmReais}" type="currency" 
						currencyCode="BRL" />
					</p>
					
						
					<p>

							

						<div style="position:absolute;bottom: 10px;margin-left:15px;">

							<g:if test="${i > 0}">
								<g:link action="left" controller="home" id="${produto.id}" class=" btn btn-primary"> 
									<i class="icon-arrow-left"> </i>  
								</g:link>
							</g:if>

							<g:if test="${ (i+1) > 4 }">
								<g:link action="up" controller="home" id="${produto.id}"  class="btn btn-primary"> 
									<i class="icon-arrow-up"> </i>  
								</g:link>
							</g:if>

							<g:if  test="${ i < (qtdeProduto - 4) }">
								<g:link action="down" controller="home" id="${produto.id}" class=" btn btn-primary"> 
									<i class="icon-arrow-down"> </i>  
								</g:link>
							</g:if>

							<g:if test="${ (i+1) < qtdeProduto}">
								<g:link action="right" controller="home" id="${produto.id}" class=" btn btn-primary"> 
									<i class="icon-arrow-right"> </i> 
								</g:link>
							</g:if>

						</div>
																								
						

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



