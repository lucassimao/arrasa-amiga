
<%@ page import="br.com.arrasaamiga.Banner" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'banner.label', default: 'Banner')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-banner" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="list-banner" class="content scaffold-list" role="main">
			<h1>Banners</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="arquivo" title="${message(code: 'banner.arquivo.label', default: 'Banner')}" />
					

						<g:sortableColumn property="titulo" title="${message(code: 'banner.titulo.label', default: 'Titulo')}" />
					
						<g:sortableColumn property="comentario" title="${message(code: 'banner.comentario.label', default: 'Comentário')}" />
					

						<g:sortableColumn property="visivel" title="${message(code: 'banner.visivel.label', default: 'Visivel')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${bannerInstanceList}" status="i" var="bannerInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${bannerInstance.id}"><asset:image style="width: 50px" src="banners/${fieldValue(bean: bannerInstance, field: 'arquivo')}" /> </g:link></td>
					

						<td>${fieldValue(bean: bannerInstance, field: "titulo")}</td>
					
						<td>${fieldValue(bean: bannerInstance, field: "comentario")}</td>
					

						<td><g:formatBoolean boolean="${bannerInstance.visivel}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${bannerInstanceCount ?: 0}" />
			</div>

            <div class="nav" role="navigation">
                <ul>
                    <li style="float:right;"><g:link class="create" action="create">Cadastrar Novo Banner</g:link></li>
                </ul>
            </div>
		</div>
	</body>
</html>
