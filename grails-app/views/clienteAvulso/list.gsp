
<%@ page import="br.com.arrasaamiga.ClienteAvulso" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'clienteAvulso.label', default: 'ClienteAvulso')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-clienteAvulso" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		

		
		<div id="list-clienteAvulso" class="content scaffold-list" role="main">
			<h1>Contatos de Clientes </h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="nome" title="${message(code: 'clienteAvulso.nome.label', default: 'Nome')}" />
					
						<g:sortableColumn property="telefones" title="${message(code: 'clienteAvulso.telefones.label', default: 'Telefones')}" />
					
						<g:sortableColumn property="endereco" title="${message(code: 'clienteAvulso.endereco.label', default: 'Endereco')}" />
					
						<g:sortableColumn property="facebook" title="${message(code: 'clienteAvulso.facebook.label', default: 'Facebook')}" />
					
						<g:sortableColumn property="observacao" title="${message(code: 'clienteAvulso.observacao.label', default: 'Observacao')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${clienteAvulsoInstanceList}" status="i" var="clienteAvulsoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${clienteAvulsoInstance.id}">${fieldValue(bean: clienteAvulsoInstance, field: "nome")}</g:link></td>
					
						<td>${clienteAvulsoInstance.telefones.replace('\n',  ' / ')}</td>
					
						<td>${fieldValue(bean: clienteAvulsoInstance, field: "endereco")}</td>

						<g:if test="${clienteAvulsoInstance.facebook}">
							<td><a href="${clienteAvulsoInstance.facebook}"> Link </a></td>
						</g:if>
						<g:else>
							<td> </td>
						</g:else>
						<td>${fieldValue(bean: clienteAvulsoInstance, field: "observacao")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${clienteAvulsoInstanceTotal}" />
			</div>
		</div>

		<div class="nav" role="navigation">
			<ul>
				<li style="float:right;"><g:link class="create" action="create">Novo Contato</g:link></li>
			</ul>
		</div>

	</body>
</html>
