
<%@ page import="br.com.arrasaamiga.Feriado" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'feriado.label', default: 'Feriado')}" />
		<title>Detalhes do Feriado</title>
	</head>
	<body>
		<a href="#show-feriado" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="show-feriado" class="content scaffold-show" role="main">
			<h1>Detalhes do Feriado</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list feriado">
				<li class="fieldcontain">
					<span id="descricao-label" class="property-label">Descrição</span>
					<span class="property-value" aria-labelledby="descricao-label"><g:fieldValue bean="${feriadoInstance}" field="descricao"/></span>
				</li>
				<li class="fieldcontain">
					<span id="inicio-label" class="property-label">Início</span>
					<span class="property-value" aria-labelledby="fim-label"><g:formatDate type="date" date="${feriadoInstance?.inicio}"/></span>
				</li>
				<li class="fieldcontain">
					<span id="fim-label" class="property-label">Fim</span>
					<span class="property-value" aria-labelledby="fim-label"><g:formatDate type="date" date="${feriadoInstance?.fim}"/></span>
				</li>
			</ol>
			<g:form url="[resource:feriadoInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${feriadoInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
