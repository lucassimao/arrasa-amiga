
<%@ page import="br.com.arrasaamiga.Produto" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>


		<g:if test="${estoques.size() > 1}">

				<g:javascript>

					$(function(){
						

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
										$("#msg-estoque-esgotado").show({duration:250}); //.css('display','block');
									}



								
								}).fail(function(){


								});


						});


						// carregando estoque para o primeiro item da lista de unidades
						$("#select-unidade").change();
					});
				</g:javascript>
		</g:if>

		<style type="text/css">
			.well-titulo-produto{
				background-color:white;
				height:50px;
				margin-top:10px;
				text-align:right;
				color:#01A8DD;
			}

			.well-detalhes{
				background-color:white;
				height:350px;
			}

			.well-detalhes{
				text-align:justify;
				position:relative;
				height:380px;
			}

			.well-detalhes-descricao{
				font-size:14px;
				font-family:'Helvetica Neue',Helvetica,Arial,sans-serif;
				text-align:justify;
			}

			#label-preco{
				color:#39c61f;
				position:absolute;
				bottom:100px;
				left:0px;
			}

			#label-quantidade{
				position:absolute;
				bottom:65px;
				font-weight:bold;
				left:0px;
			}

			#select-quantidade{
				position:absolute;
				bottom:25px;
				left:0px;
				width:60px;
			}
			#btn-comprar{
				position:absolute;
				bottom:30px;
				right:0px
			}

			#msg-estoque-esgotado{
				position:absolute;
				bottom:30px;
				left:0px;
			}

			#label-tipo-unitario{
				position:absolute;
				bottom:65px;
				font-weight:bold;
				left:0px;
			}

			#select-unidade{
				position:absolute;
				bottom:25px;
				left:0px;
				width:100px;
			}


		</style>


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

	      			<div class="span5">
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


	      			<div class="span7 well-detalhes">

						<small class="well-detalhes-descricao">
							${produtoInstance.descricao}
						</small>

						<h3 id="label-preco">  R$ ${produtoInstance.precoAPrazoEmReais} <i class="icon-tag"></i> </h3>

						

						<g:form action="add" controller="shoppingCart" method="post" id="${produtoInstance.id}"> 
						
							<g:if test="${estoques.size() == 1}">

									<g:set var="estoque" value="${estoques[0]}"/>

									<g:if test="${estoque.quantidade > 0}">

										<p id="label-quantidade"> Quantidade: </p>
										<g:select id="select-quantidade" name="quantidade" from="${1..estoque.quantidade}"/>
										<g:hiddenField name="unidade" value="${estoque.unidade}" />


										<g:submitButton name="btnComprar" id="btn-comprar" class="btn btn-primary btn-large" value="Comprar" />
									
									</g:if>
									<g:else>
										<span id="msg-estoque-esgotado"> 
											Ow Amiga, o estoque desse item esta esgotado temporariamente ... 
											
										</span>
									</g:else>

							</g:if>

							<g:else>
									<p id="label-tipo-unitario"> ${produtoInstance.tipoUnitario}: </p>


									<g:select value="${ (unidadeComEstoque)?:estoques[0].unidade}" id="select-unidade" name="unidade"
										 from="${produtoInstance.unidades}"/>

									<p id="label-quantidade" style="left:110px;"> Quantidade:</p>
									<g:select style="left:110px;" id="select-quantidade" name="quantidade" from="${[]}" /> 
									
									<p id="msg-estoque-esgotado" style="bottom:25px;left:110px;display:none;"> 
										Ow Amiga, por enquanto estamos sem <span style="font-weight:bold;" id="unidade"/> ;-(
									</p>

									<g:submitButton id="btn-comprar" class="btn btn-primary btn-large" name="btn-comprar" value="Comprar" />
							</g:else>




						</g:form>
						
	      			</div>

      			</div>


      		</div>


	</body>
</html>