
<%@ page import="br.com.arrasaamiga.Produto" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
	</head>
	<body>


			<div class="row" style="text-align:left;font-family:museo-slab,'Helvetica Neue',Helvetica,Arial,sans-serif;" >
				<h3 style="color:#01A8DD;"> ${produtoInstance.nome} </h3>
			</div>
	

      		<div class="row" style="border:2px dotted pink;padding-top:10px;clear:both;">
      			<div class="span4" style="height:380px;">
					<div id="myCarousel" class="carousel slide">
					  
					  <div class="carousel-inner">
					  	<g:each in="${produtoInstance.fotos[1..-1]}" var="pic" status="i">
					  		<div class="item ${i==0?'active':''}"> <g:img  dir="img/produtos" file="${pic}"/></div>
					  	</g:each>
					  </div>

					  <a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a>
					  <a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>
					</div>
      			</div>


      			<div class="span4" style="text-align:justify;position:relative;height:380px;">

					<small style="font-size:16;font-family:'Helvetica Neue',Helvetica,Arial,sans-serif;">Sed ut perspiciatis unde omnis iste natus
					 error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, 
					 eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae
					  dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur 
					  voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia 
					  voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia 
					  aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione 
					  voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia 
					  aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione 
					    iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae
					     consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?"

					</small>

					<h3 style="color:#39c61f;position:absolute;bottom:60px;left:0px;">  R$ ${produtoInstance.precoEmReais} <i class="icon-tag"></i> </h3>

					<p style="position:absolute;bottom:40px;font-weight:bold;left:0px;"> Quantidade:</p>

					<g:form action="addToShoppingCart" controller="produto" method="post" id="${produtoInstance.id}"> 
					

						<g:select style="position:absolute;bottom:10px;left:0px;width:60px;" name="quantidade" from="${1..10}"/>

						<p style="position:absolute;bottom:10px;right:0px;">
							<g:submitButton class="btn btn-primary btn-large icon-shopping-cart" name="btnSalvar" value="Comprar" />
						</p>

					</g:form>
					
      			</div>
      		</div>


	</body>
</html>