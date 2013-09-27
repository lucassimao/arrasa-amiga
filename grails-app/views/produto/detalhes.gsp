
<%@ page import="br.com.arrasaamiga.Produto" %>
<%@ page import="br.com.arrasaamiga.Aviso" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>

			<g:javascript library="detalhesProdutos"/>

			<g:javascript>
				
				$(function(){
					

					$("#btn-comprar").click(function(){
						$('form').submit();
					});

					$(".fancybox").fancybox();

					<g:each in="${produtoInstance.fotos}" var="fotoProduto" status="i">
						$("#img${i}").data('unidade',"${fotoProduto.unidade}");
					</g:each>

					<g:if test="${produtoInstance.isMultiUnidade()}">

							$("#select-unidade").change(function(){

								var unidade = $(this).val();

								$(".carousel-inner img").each(function(index,img){

									if ( $(img).data('unidade') === unidade ){

										$(".carousel").carousel(index);
										return false;
									}

								});


								$.ajax({
										
										url: "${createLink(controller:'produto',action:'quantidadeEmEstoque',absolute:true)}",
										data: {'produtoId': ${produtoInstance.id} , 'unidade': unidade},
										settings: {'cache':false}

									}).success(function( data, textStatus, jqXHR ) {

										var qtdeEmEstoque = parseInt(data.quantidade);

										$("#select-quantidade").empty();
										$("#msg-estoque-esgotado").css('display','none');
										$("#aviso-ativado").hide();
										$("#ativar-aviso").hide();

										if ( qtdeEmEstoque > 0 ){

											$("#select-quantidade").show();
											$("#label-quantidade").show();
											$("#btn-comprar").show();

											// adicionando as quantidades

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

											var marcadoParaAvisar = Boolean(data.marcadoParaAvisar);
											
											if (marcadoParaAvisar){
												$("#aviso-ativado").show();
												$("#ativar-aviso").hide();

											}else{
												$("#aviso-ativado").hide();
												$("#ativar-aviso").show();

												var url = "${createLink(controller:'produto',action:'avisar',id:produtoInstance.id)}";
												$("#clica-aqui").attr("href", url + "?" + $.param({'un':unidade}));
											}

										}



									
									}).fail(function(){


									});


							});

							// carregando estoque para o primeiro item da lista de unidades
							$("#select-unidade").change();

					</g:if>
					<g:else>
						$(function(){

								var unidade = $("#unidade").val();

								$.ajax({
									
									url: "${createLink(controller:'produto',action:'quantidadeEmEstoque',absolute:true)}",
									data: {'produtoId': ${produtoInstance.id} , 'unidade': unidade},
									settings: {'cache':false}

								}).success(function( data, textStatus, jqXHR ) {

									var qtdeEmEstoque = parseInt(data.quantidade);
									var marcadoParaAvisar = Boolean(data.marcadoParaAvisar);

									
									if ( qtdeEmEstoque == 0 ){

										if (marcadoParaAvisar){

											$("#aviso-ativado").show({duration:250});

										}else{
										
											$("#ativar-aviso").show({duration:250});										
										}
									}



								
								}).fail(function(){


								});


						});						
					</g:else>


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

			<g:if test='${flash.info}'>
				<div class='alert alert-info' style="margin-top:10px;font-weight:bold;">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<i class="icon-ok"> </i> ${flash.info}
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
								<g:each in="${produtoInstance.fotos}" var="fotoProduto" status="i">
									<div class="item ${i==0?'active':''}">	

										<a class="fancybox" rel="group" title="${fotoProduto.comentario}" href="${resource(file:fotoProduto.arquivo, dir:'img/produtos')}"> 
											<g:img id="img${i}" dir="img/produtos" file="${fotoProduto.arquivo}"/> 
										</a>
										
									</div>
								</g:each>
							</div>

							Click na foto para ampliar

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

						<h3 id="label-preco-a-vista">  
							Desconto para pagamento à vista 
						</h3>

						

						<g:form action="add" controller="shoppingCart" method="post" id="${produtoInstance.id}"> 
						
							<g:if test="${produtoInstance.isMultiUnidade()}">

									<p id="label-tipo-unitario"> ${produtoInstance.tipoUnitario}: </p>


									<g:select value="${ (unidadeComEstoque)?:estoques[0].unidade}" id="select-unidade" 
												name="unidade"
										 from="${produtoInstance.unidades}"/>

									<p id="label-quantidade" style="left:110px;"> Quantidade:</p>
									<g:select style="left:110px;" id="select-quantidade" name="quantidade" from="${[]}" /> 
									
									<p id="msg-estoque-esgotado" style="bottom:15px;left:110px;display:none;"> 
										Ow Amiga, por enquanto estamos sem ${produtoInstance.tipoUnitario} 
										<span style="font-weight:bold;" id="unidade"/> ;-(
									</p>
											

									<div style="font-size:14px;color:blue;position:absolute;display:none;bottom:5px;left:110px;" id="aviso-ativado">
										
											Avisaremos você assim que novas unidades chegarem ;-) 

									</div>

									<div style="position:absolute;display:none;bottom:5px;left:110px;" id="ativar-aviso">
										<a id="clica-aqui" href="#"> Quer saber assim que chegar ? </a> 
									</div>

									<a class="btn btn-primary btn-large" id="btn-comprar"  name="btn-comprar">
										<i class="icon-shopping-cart"> </i> Comprar
									</a>

							</g:if>

							<g:else>
									
									<g:set var="estoque" value="${estoques[0]}"/>
									<g:hiddenField id="unidade" name="unidade" value="${estoque.unidade}" />

									<g:if test="${estoque.quantidade > 0}">

										<p id="label-quantidade"> Quantidade: </p>
										<g:select id="select-quantidade" name="quantidade" from="${1..estoque.quantidade}"/>

										<a class="btn btn-primary btn-large" id="btn-comprar"  name="btn-comprar">
											<i class="icon-shopping-cart"> </i> Comprar
										</a>
									
									</g:if>

									<g:else>
										<div id="msg-estoque-esgotado"> 
											<p> 
												Ow Amiga, esse item esgotou, mas estamos aguardando novas unidades !! 
											</p>							
											
											<div style="display:none;font-size:14px;color:blue;" id="aviso-ativado">
													Avisaremos você assim que novas unidades chegarem ;-) 
											</div>
											<div style="display:none;" id="ativar-aviso">
												<a href="${createLink(controller:'produto',action:'avisar',
															id:produtoInstance.id,params:[un:estoque.unidade])}">
															Quer saber assim que chegar ?  
												</a> 
											</div>
										</div>
										
									</g:else>

							</g:else>


						</g:form>
						
	      			</div>

      			</div>


      		</div>


	</body>

</html>