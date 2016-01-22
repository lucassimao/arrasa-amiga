
<%@ page import="br.com.arrasaamiga.financeiro.TransactionSummary" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'transactionSummary.label', default: 'TransactionSummary')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-transactionSummary" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="list-transactionSummary" class="content scaffold-list" role="main">
			<h1> Gerenciar Transações PagSeguro</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="code" title="${message(code: 'transactionSummary.code.label', default: 'Code')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'transactionSummary.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="valorBrutoEmCentavos" title="${message(code: 'transactionSummary.valorBrutoEmCentavos.label', default: 'Valor Total')}" />
					
						<g:sortableColumn property="status" title="${message(code: 'transactionSummary.status.label', default: 'Status')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${transactionSummaryInstanceList}" status="i" var="transactionSummaryInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${transactionSummaryInstance.id}">${fieldValue(bean: transactionSummaryInstance, field: "code")}</g:link></td>
					
						<td><g:formatDate date="${transactionSummaryInstance.dateCreated}" /></td>
					
						<td><g:formatNumber type="currency"  currencyCode="BRL"  number="${transactionSummaryInstance.valorBrutoEmReais}" />

						<td>${fieldValue(bean: transactionSummaryInstance, field: "status")}</td>
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${transactionSummaryInstanceCount ?: 0}" />
			</div>
		</div>


	</body>
</html>
