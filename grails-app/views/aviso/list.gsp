
<%@ page import="br.com.arrasaamiga.Aviso" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'aviso.label', default: 'Aviso')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-aviso" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="list-aviso" class="content scaffold-list" role="main">
			<h1> Avisos </h1>

			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="nome" title="${message(code: 'aviso.nome.label', default: 'Nome')}" />
					
						<g:sortableColumn property="email" title="${message(code: 'aviso.email.label', default: 'Email')}" />
					
						<g:sortableColumn property="celular" title="${message(code: 'aviso.celular.label', default: 'Celular')}" />
					
						<th><g:message code="aviso.produto.label" default="Produto" /></th>
					
						<g:sortableColumn property="unidade" title="${message(code: 'aviso.unidade.label', default: 'Unidade')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'aviso.dateCreated.label', default: 'Data')}" />

						<th> Excluir </th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${avisoInstanceList}" status="i" var="avisoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						
						<g:if test="${avisoInstance.facebookUserId}">
							<td><a href="http://www.facebook.com/${avisoInstance.facebookUserId}" target="_blank">${fieldValue(bean: avisoInstance, field: "nome")}</a></td>
						</g:if>
						<g:else>
							<td>${fieldValue(bean: avisoInstance, field: "nome")}</td>
						</g:else>
						
					
						<td>${fieldValue(bean: avisoInstance, field: "email")}</td>
					
						<td>${fieldValue(bean: avisoInstance, field: "celular")}</td>
					
						<td>${avisoInstance.produto.nome}</td>
					
						<td>${fieldValue(bean: avisoInstance, field: "unidade")}</td>
					
						<td><g:formatDate format="dd/MM/yyyy" date="${avisoInstance.dateCreated}" /></td>
						<td>
							<g:form>
									<g:hiddenField name="id" value="${avisoInstance?.id}" />
									<g:actionSubmit class="delete" action="delete" value="Excluir" 
										onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
							</g:form>
						</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${avisoInstanceTotal}" />
			</div>
		</div>

		<div class="nav" role="navigation" style="margin-top:20px;">
			<ul>
				<li style="float:right;"><g:link class="create"  action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>

	</body>
</html>
