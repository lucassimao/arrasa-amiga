
<%@ page import="br.com.arrasaamiga.Estoque" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'estoque.label', default: 'Estoque')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
		<style type="text/css">
			.go-back{
				text-decoration:none;
				float:right;
				text-indent: 35px;
				background-repeat: no-repeat;
				background-position: 0.7em center;
				background-image:url(${resource(dir:'images/skin',file:'back.png')});
			}	
		</style>

	</head>
	<body>
		<a href="#show-estoque" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="show-estoque" class="content scaffold-show" role="main">
			<h1> 
				Detalhes do estoque do produto
				<g:link class="go-back"	action="list"> Voltar </g:link>
			</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list estoque">
			
				<li class="fieldcontain">
					<span id="produto-label" class="property-label"><g:message code="estoque.produto.label" default="Produto" /></span>
					
						<span class="property-value" aria-labelledby="produto-label">
							<g:link controller="produto" action="show" id="${estoqueInstance?.produto?.id}">
								${estoqueInstance?.produto?.nome.encodeAsHTML()}</g:link></span>
					
				</li>
			
				<li class="fieldcontain">
					<span id="unidade-label" class="property-label"><g:message code="estoque.unidade.label" default="Unidade" /></span>
					
						<span class="property-value" aria-labelledby="unidade-label"><g:fieldValue bean="${estoqueInstance}" field="unidade"/></span>
					
				</li>
			
				<li class="fieldcontain">
					<span id="quantidade-label" class="property-label">Quantidade Atual:</span>
					
						<span class="property-value" aria-labelledby="quantidade-label"><g:fieldValue bean="${estoqueInstance}" field="quantidade"/></span>
					
				</li>

				<li class="fieldcontain">
					<span id="quantidade-label" class="property-label"> Entradas </span>
					
						<span class="property-value">
							<ul style="list-style-type:none;">
								<g:each in="${estoqueInstance.entradas}" var="entrada">
									<li> 
										${formatDate(format:'dd/MM/yyyy', date:entrada.dataEntrada) } :  
										
										<g:link controller="pedido" action="show" id="${entrada.pedido.id}">  
											${entrada.pedido.descricao} 
										</g:link> 
									</li>
								</g:each>
							</ul>
						</span>
					
				</li>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${estoqueInstance?.id}" />
					<g:link class="edit" action="entrada" id="${estoqueInstance?.id}"> Dar entrada </g:link>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
