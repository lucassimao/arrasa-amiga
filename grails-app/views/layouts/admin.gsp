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
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
		<g:javascript src="jquery-1.10.1.min.js"/>
		<g:layoutHead/>
		<r:layoutResources />
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
					<a style="${ (controllerName.equals('clienteAvulso'))?'background-color:#666;color:white;':'' }" href="${createLink(controller: 'clienteAvulso')}">
						Contatos de Clientes
					</a>
				</li>

				<li>
					<a style="${ (controllerName.equals('investimento'))?'background-color:#666;color:white;':'' }" href="${createLink(controller: 'investimento')}">
						Investimentos
					</a>
				</li>


			</ul>
		</div>

		<g:layoutBody/>
		<div class="footer" role="contentinfo"></div>
		<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
		<g:javascript library="application"/>

		<r:layoutResources />
	</body>
</html>
