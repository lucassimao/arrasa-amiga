<%@ page import="br.com.arrasaamiga.Produto" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>

		<style type="text/css">
			.btnComprar{
				left:26%;
				position:absolute;
				bottom: 10px;
			}

			.label-preco{
				left:30%;
				color:#ad96a5;
				font-size:20px;
				position:absolute;
				bottom: 50px;
				font-weight:bold;
			}

			.label-desconto-a-vista{
				left:10%;
				color:#ad96a5;
				font-size:12px;
				position:absolute;
				bottom: 28px;
			}

			.item-produto{
				padding:5px;
				text-align:center;
				height:300px;
				border:1px solid orange;
				position:relative;
			}
		</style>


		<g:javascript>
					
			$(function(){

				$("form a").click(function(){

					$(this).parent().submit();
					
				});
			});


		</g:javascript>
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
					
				<div class="span3 item-produto">
					
					<g:img dir="img/produtos" file="${produto.fotoMiniatura}"/>


					<a href="${createLink(uri:produto.nomeAsURL, absolute:true)}"> 
						<h5> ${produto.nome} </h5>
					</a>
					
					<p class="label-preco"> 
						<g:formatNumber number="${produto.precoAPrazoEmReais}" type="currency" 
						currencyCode="BRL" />
					</p>

					
						
					<p class="label-desconto-a-vista"> 
						Desconto para pagamento à vista
					</p>



					<p>

						<g:if test="${produto.isMultiUnidade()}">

							<a href="${createLink(uri:produto.nomeAsURL, absolute:true)}" class="btnComprar btn btn-primary" > 
								<i class="icon-shopping-cart"> </i> Comprar
							</a>

						</g:if>
						<g:else>
							
							<g:form action="add" controller="shoppingCart">
								<g:hiddenField name="id" value="${produto.id}"/>
								<g:hiddenField name="unidade" value="${produto.unidades[0]}"/>
								<g:hiddenField name="quantidade" value="${1}"/>
								


								<g:link id="${produto.id}" class="btnComprar btn btn-primary" url="#"> 
									<i class="icon-shopping-cart"> </i>  Comprar 
								</g:link>

							</g:form>  									
						
						</g:else>



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



