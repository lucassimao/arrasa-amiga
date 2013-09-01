<%@ page import="br.com.arrasaamiga.Investimento" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'investimento.label', default: 'Investimento')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
		<style type="text/css">
			.go-back{
				text-decoration:none;
				float:right;
				text-indent: 35px;
				background-repeat: no-repeat;
				background-position: 0.7em center;
				background-image:url( ${ resource(dir:'images/skin',file:'back.png') } );
			}	
		</style>
	</head>
	<body>
		<a href="#edit-investimento" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="edit-investimento" class="content scaffold-edit" role="main">
			<h1>	
				<g:message code="default.edit.label" args="[entityName]" />
				<g:link class="go-back"	action="list"> Voltar </g:link>
			</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${investimentoInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${investimentoInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form method="post" >
				<g:hiddenField name="id" value="${investimentoInstance?.id}" />
				<g:hiddenField name="version" value="${investimentoInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
