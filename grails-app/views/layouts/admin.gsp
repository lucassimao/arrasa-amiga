<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
		<asset:link rel="apple-touch-icon" href="apple-touch-icon.png" type="image/x-icon"/>
		<asset:link rel="apple-touch-icon" sizes="114x114" href="apple-touch-icon-retina.png" type="image/x-icon"/>
		<asset:stylesheet src="main.css"/>
   	  	<asset:stylesheet src="mobile.css"/>

		<g:layoutHead/>

	</head>
	<body>
		<div class="nav" role="navigation" style="margin-bottom:40px;">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}">Home</a></li>
				<li>
					<a style="${ (controllerName.equals('produto'))?'background-color:#666;color:white;':'' }" href="${createLink(controller: 'produto')}">
						Produtos
					</a>
				</li>

				<li>
					<a style="${ (controllerName.equals('pedido'))?'background-color:#666;color:white;':'' }" href="${createLink(controller: 'pedido')}">
						Pedidos
					</a>
				</li>

				<li>
					<a style="${ (controllerName.equals('estoque'))?'background-color:#666;color:white;':'' }" href="${createLink(controller: 'estoque')}">
						Estoque
					</a>
				</li>

				<li>
					<a style="${ (controllerName.equals('venda'))?'background-color:#666;color:white;':'' }" href="${createLink(controller: 'venda')}">
						Vendas
					</a>
				</li>

				<li>
					<a style="${ (controllerName.equals('aviso'))?'background-color:#666;color:white;':'' }" href="${createLink(controller: 'aviso')}">
						Avisos
					</a>
				</li>

				<li>
					<a style="${ (controllerName.equals('grupoDeProduto'))?'background-color:#666;color:white;':'' }" href="${createLink(controller: 'grupoDeProduto')}">
						Grupos de Produtos
					</a>
				</li>


			</ul>
		</div>

		<g:layoutBody/>
		<div class="footer" role="contentinfo"></div>
		<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>

		<asset:deferredScripts/>

	</body>
</html>
