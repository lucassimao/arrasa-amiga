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

			#p-comprar{
				text-align: center;
				position:absolute;
				bottom: 9px;
				left:0px;
				width: 100%;	
			}


			.label-preco{
				text-align: center;
				color:#ad96a5;
				font-size:20px;
				position:absolute;
				bottom: 65px;
				font-weight:bold;
				left:0px;
				width: 100%;
			}

			.label-desconto-a-vista{
				text-align: center;
				color:#ad96a5;
				font-size:12px;
				position:absolute;
				bottom: 38px;
				left:0px;
				width: 100%;
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

			<hr>

			<g:if test='${flash.message}'>
				<div class='alert alert-info' style="margin-top:10px;font-weight:bold;">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<i class="icon-ok"> </i> ${flash.message}
				</div>
			</g:if>

			<div id="toast">
				<img />
				<label/>
			</div>

      		<g:set var="count" value="${0}"/>
      		<g:set var="qtdeProdutosVisiveis" value="${ Produto.countByVisivel(true) }"/>

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
						<g:formatNumber number="${produto.precoAPrazoEmReais}" type="currency" currencyCode="BRL" />
					</p>					
						
					<p class="label-desconto-a-vista"> 
						Desconto para pagamento à vista
					</p>					

					<g:if test="${produto.isMultiUnidade()}">

						<p id="p-comprar">
							<a href="${createLink(uri:produto.nomeAsURL, absolute:true)}" class="btn btn-primary" > 
								<i class="icon-shopping-cart"> </i> Comprar
							</a>
						</p>

					</g:if>
					<g:else>
						
							<g:form action="add" controller="shoppingCart" style="left:0;width:100%;position:absolute;bottom:-1px;">
								<g:hiddenField name="id" value="${produto.id}"/>
								<g:hiddenField name="unidade" value="${produto.unidades[0]}"/>
								<g:hiddenField name="quantidade" value="${1}"/>
								

								<g:link style="" id="${produto.id}" class="btn btn-primary" url="#"> 
									<i class="icon-shopping-cart"> </i>  Comprar 
								</g:link>

							</g:form>  		
					
					</g:else>



				</div>
				
				<g:set var="count" value="${++count}"/>

      			<g:if test="${ count == 4 || (i+1) == qtdeProdutosVisiveis}">
					</div>
					<g:set var="count" value="${0}"/>
				</g:if>

			</g:each>
			
	</body>
</html>



