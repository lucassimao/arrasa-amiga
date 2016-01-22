<%@ page import="br.com.arrasaamiga.financeiro.TransactionSummary" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'transactionSummary.label', default: 'TransactionSummary')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#edit-transactionSummary" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="edit-transactionSummary" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.error}">
			<div class="message" role="status" style="color: red;box-shadow: 0 0 0.25em red;">${flash.error}</div>
			</g:if>
			<g:hasErrors bean="${transactionSummaryInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${transactionSummaryInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form url="[resource:transactionSummaryInstance, action:'update']" method="PUT" >
				<g:hiddenField name="version" value="${transactionSummaryInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:link class="list" action="index">Voltar</g:link>
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
