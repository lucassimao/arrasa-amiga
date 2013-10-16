
<%@ page import="br.com.arrasaamiga.Venda" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'venda.label', default: 'Venda')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-venda" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="list-venda" class="content scaffold-list" role="main">
			<h1>Vendas</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="cliente" title="Cliente" />
					
					
						<g:sortableColumn property="formaPagamento" title="${message(code: 'venda.formaPagamento.label', default: 'Forma Pagamento')}" />
					
						<g:sortableColumn property="status" title="${message(code: 'venda.status.label', default: 'Status')}" />
						<th> Data </th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${vendaInstanceList}" status="i" var="vendaInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="showFull" id="${vendaInstance.id}">${fieldValue(bean: vendaInstance, field: "cliente.nome")}</g:link></td>
					
					
						<td>${fieldValue(bean: vendaInstance, field: "formaPagamento")}</td>
					
						<td>${fieldValue(bean: vendaInstance, field: "status")}</td>

						<td> <g:formatDate format="dd/MM/yyyy" date="${vendaInstance.dateCreated}" /> </td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${vendaInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
