
<%@ page import="br.com.arrasaamiga.Produto" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>

			<g:javascript>
				
				$(function(){
					

					$("#btn-comprar").click(function(){
						$('form').submit();
					});

					<g:if test="${produtoInstance.isMultiUnidade()}">

							$("#select-unidade").change(function(){
								var unidade = $(this).val();

								$.ajax({
										
										url: "${createLink(controller:'produto',action:'quantidadeEmEstoque',absolute:true)}",
										data: {'produtoId': ${produtoInstance.id} , 'unidade': unidade},
										settings: {'cache':false}

									}).success(function( data, textStatus, jqXHR ) {

										var qtdeEmEstoque = parseInt(data);

										$("#select-quantidade").empty();
										$("#msg-estoque-esgotado").css('display','none');

										if ( qtdeEmEstoque > 0 ){

											$("#select-quantidade").show();
											$("#label-quantidade").show();
											$("#btn-comprar").show();
											

											for(var count=1; count <= qtdeEmEstoque;++count){

												var option = $("<option/>").text(count).attr("value",count);
												$("#select-quantidade").append(option);

											}							
										}else{

											$("#select-quantidade").hide();
											$("#label-quantidade").hide();
											$("#btn-comprar").hide();
											$("#msg-estoque-esgotado #unidade").text(unidade);
											$("#msg-estoque-esgotado").show({duration:250});
										}



									
									}).fail(function(){


									});


							});

							// carregando estoque para o primeiro item da lista de unidades
							$("#select-unidade").change();

					</g:if>


				});
			</g:javascript>
		
		<link type="text/css"  rel="stylesheet" href="${resource(dir: 'css/produto', file: 'detalhes.css')}" > </link>

	</head>
	<body>


			<g:if test='${flash.message}'>
				<div class='alert alert-error' style="margin-top:10px;">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					${flash.message}
				</div>
			</g:if>

			<div class="well well-titulo-produto">	
				<h3> ${produtoInstance.nome} </h3>
			</div>
	

      		<div class="row-fluid" style="clear:both;">
      			
      			<div class="well well-detalhes">

	      			<div id="produto-fotos" class="span5">

						<div id="myCarousel" class="carousel slide">

							<div class="carousel-inner">
								<g:each in="${produtoInstance.fotos}" var="pic" status="i">
									<div class="item ${i==0?'active':''}"> <g:img  dir="img/produtos" file="${pic}"/></div>
								</g:each>
							</div>

							<a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a>
							<a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>

						</div>
	      			</div>


	      			<div id="produto-detalhes" class="span7">

						<small class="well-detalhes-descricao">
							${produtoInstance.descricao}
						</small>

						<h3 id="label-preco">  
							<g:formatNumber number="${produtoInstance.precoAPrazoEmReais}" type="currency" 
								currencyCode="BRL" />
							<i class="icon-tag"></i> 
						</h3>

						<g:if test="${cliente?.endereco?.fromTeresina}">
							<h3 id="label-preco-a-vista">  
								Desconto à vista: <g:formatNumber number="${produtoInstance.descontoAVistaEmReais}" type="currency" 
									currencyCode="BRL" />
							</h3>
						</g:if>

						

						<g:form action="add" controller="shoppingCart" method="post" id="${produtoInstance.id}"> 
						
							<g:if test="${produtoInstance.isMultiUnidade()}">

									<p id="label-tipo-unitario"> ${produtoInstance.tipoUnitario}: </p>


									<g:select value="${ (unidadeComEstoque)?:estoques[0].unidade}" id="select-unidade" name="unidade"
										 from="${produtoInstance.unidades}"/>

									<p id="label-quantidade" style="left:110px;"> Quantidade:</p>
									<g:select style="left:110px;" id="select-quantidade" name="quantidade" from="${[]}" /> 
									
									<p id="msg-estoque-esgotado" style="bottom:5px;left:110px;display:none;"> 
										Ow Amiga, por enquanto estamos sem ${produtoInstance.tipoUnitario} <span style="font-weight:bold;" id="unidade"/> ;-(
									</p>

									<a class="btn btn-primary btn-large" id="btn-comprar"  name="btn-comprar">
										<i class="icon-shopping-cart"> </i> Comprar
									</a>

							</g:if>

							<g:else>
									
									<g:set var="estoque" value="${estoques[0]}"/>

									<g:if test="${estoque.quantidade > 0}">

										<p id="label-quantidade"> Quantidade: </p>
										<g:select id="select-quantidade" name="quantidade" from="${1..estoque.quantidade}"/>
										<g:hiddenField name="unidade" value="${estoque.unidade}" />


										<a class="btn btn-primary btn-large" id="btn-comprar"  name="btn-comprar">
											<i class="icon-shopping-cart"> </i> Comprar
										</a>
									
									</g:if>
									<g:else>
										<span id="msg-estoque-esgotado"> 
											Ow Amiga, o estoque desse item esta esgotado temporariamente ... 
											
										</span>
									</g:else>

							</g:else>


						</g:form>
						
	      			</div>

      			</div>


      		</div>


	</body>
</html>