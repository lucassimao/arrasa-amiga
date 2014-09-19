
<%@ page import="br.com.arrasaamiga.Banner" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'banner.label', default: 'Banner')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-banner" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="show-banner" class="content scaffold-show" role="main">
			<h1>Detalhes do Banner</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list banner">

				<g:if test="${bannerInstance?.arquivo}">
				<li class="fieldcontain">
					<span id="arquivo-label" class="property-label">Banner</span>

						<span class="property-value" aria-labelledby="arquivo-label">
                            <a target="_blank" href="${resource(dir: 'images/banners',file: bannerInstance.arquivo)}">
                                <img src="${resource(dir: 'images/banners',file: bannerInstance.arquivo)}" style="width:25%;"/>
                            </a>
                            <p> <small> Click para ampliar </small></p>
                        </span>

				</li>
				</g:if>

				<g:if test="${bannerInstance?.link}">
				<li class="fieldcontain">
					<span id="link-label" class="property-label"><g:message code="banner.link.label" default="Link" /></span>

						<span class="property-value" aria-labelledby="link-label"><a target="_blank" href="${fieldValue(bean:bannerInstance,field: 'link')}"> <g:fieldValue bean="${bannerInstance}" field="link"/></a> </span>

				</li>
				</g:if>

				<g:if test="${bannerInstance?.titulo}">
				<li class="fieldcontain">
					<span id="titulo-label" class="property-label"><g:message code="banner.titulo.label" default="Titulo" /></span>

						<span class="property-value" aria-labelledby="titulo-label"><g:fieldValue bean="${bannerInstance}" field="titulo"/></span>

				</li>
				</g:if>

				<g:if test="${bannerInstance?.comentario}">
				<li class="fieldcontain">
					<span id="comentario-label" class="property-label"><g:message code="banner.comentario.label" default="Comentario" /></span>

						<span class="property-value" aria-labelledby="comentario-label"><g:fieldValue bean="${bannerInstance}" field="comentario"/></span>

				</li>
				</g:if>

				<g:if test="${bannerInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="banner.dateCreated.label" default="Data de Cadastro" /></span>

						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${bannerInstance?.dateCreated}" /></span>

				</li>
				</g:if>

				<g:if test="${bannerInstance?.visivel}">
				<li class="fieldcontain">
					<span id="visivel-label" class="property-label"><g:message code="banner.visivel.label" default="Visivel" /></span>

						<span class="property-value" aria-labelledby="visivel-label"><g:formatBoolean boolean="${bannerInstance?.visivel}" /></span>

				</li>
				</g:if>

			</ol>
			<g:form url="[resource:bannerInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${bannerInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
