
<%@ page import="br.com.arrasaamiga.GrupoDeProduto" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'grupoDeProduto.label', default: 'GrupoDeProduto')}" />
		<title>Grupos de Produtos</title>
	</head>
	<body>
		<a href="#list-grupoDeProduto" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="list-grupoDeProduto" class="content scaffold-list" role="main">
			<h1>Grupos de Produtos</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="nome" title="${message(code: 'grupoDeProduto.nome.label', default: 'Nome')}" />
					
						<th>Sub-Grupos</th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${grupoDeProdutoInstanceList}" status="i" var="grupoDeProdutoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${grupoDeProdutoInstance.id}">${fieldValue(bean: grupoDeProdutoInstance, field: "nome")}</g:link></td>
					
						<td>${grupoDeProdutoInstance.subGrupos?.join(',')}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${grupoDeProdutoInstanceCount ?: 0}" />
			</div>

			<div class="nav" role="navigation" style="margin-top:20px;">
				<ul>
					<li style="float:right;"><g:link class="create" action="create">Cadastrar Novo Grupo</g:link></li>
				</ul>
			</div>

		</div>
	</body>
</html>
