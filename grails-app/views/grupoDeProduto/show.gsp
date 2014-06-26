
<%@ page import="br.com.arrasaamiga.GrupoDeProduto" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'grupoDeProduto.label', default: 'GrupoDeProduto')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-grupoDeProduto" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="show-grupoDeProduto" class="content scaffold-show" role="main">
			<h1>Detalhes do Grupo</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list grupoDeProduto">
			
				<g:if test="${grupoDeProdutoInstance?.nome}">
				<li class="fieldcontain">
					<span id="nome-label" class="property-label"><g:message code="grupoDeProduto.nome.label" default="Nome" /></span>
					
						<span class="property-value" aria-labelledby="nome-label"><g:fieldValue bean="${grupoDeProdutoInstance}" field="nome"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${grupoDeProdutoInstance?.pai}">
				<li class="fieldcontain">
					<span id="pai-label" class="property-label"><g:message code="grupoDeProduto.pai.label" default="Pai" /></span>
					
						<span class="property-value" aria-labelledby="pai-label"><g:link controller="grupoDeProduto" action="show" id="${grupoDeProdutoInstance?.pai?.id}">${grupoDeProdutoInstance?.pai?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:grupoDeProdutoInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${grupoDeProdutoInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					<g:link class="create" action="create">Cadastrar Novo Grupo</g:link>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
