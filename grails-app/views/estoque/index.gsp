
<%@ page import="br.com.arrasaamiga.Estoque" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'estoque.label', default: 'Estoque')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-estoque" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="list-estoque" class="content scaffold-list" role="main">
			<h1> Estoque  </h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>

						<th><g:message code="estoque.produto.label" default="Produto" /></th>

						<g:sortableColumn property="unidade" title="${message(code: 'estoque.unidade.label', default: 'Unidade')}" />

						<g:sortableColumn property="quantidade" title="${message(code: 'estoque.quantidade.label', default: 'Quantidade')}" />

						<th>  </th>
					</tr>
				</thead>
				<tbody>
				<g:each in="${estoqueInstanceList}" status="i" var="estoqueInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

						<td><g:link action="show" id="${estoqueInstance.id}">
							${estoqueInstance.produto.nome}
							</g:link>
						</td>

						<td>${fieldValue(bean: estoqueInstance, field: "unidade")}</td>

						<td>${fieldValue(bean: estoqueInstance, field: "quantidade")}</td>

						<td> <g:link action="entrada" id="${estoqueInstance?.id}"> Dar entrada </g:link> </td>
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate action="index" controller="estoque" total="${estoqueCount?:0}" />
			</div>
		</div>

	</body>
</html>
