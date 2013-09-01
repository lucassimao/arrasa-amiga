
<%@ page import="br.com.arrasaamiga.Investimento" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'investimento.label', default: 'Investimento')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-investimento" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="list-investimento" class="content scaffold-list" role="main">
			<h1>Investimentos</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="descricao" title="${message(code: 'investimento.descricao.label', default: 'Descricao')}" />
						<g:sortableColumn property="titular" title="${message(code: 'investimento.titular.label', default: 'Titular')}" />
						<g:sortableColumn property="valorEmCentavos" title="Valor Total" />
						<g:sortableColumn property="valorQuitado" title="Valor Quitado" />
						<g:sortableColumn property="valorDebito" title="Debito" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${investimentoInstanceList}" status="i" var="investimentoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${investimentoInstance.id}">${fieldValue(bean: investimentoInstance, field: "descricao")}</g:link></td>
						<td>${fieldValue(bean: investimentoInstance, field: "titular")}</td>
						<td>R$ ${fieldValue(bean: investimentoInstance, field: "valorEmReais")}</td>
					
						
					
						<td>R$ ${fieldValue(bean: investimentoInstance, field: "valorQuitado")}</td>
						<td> R$		${fieldValue(bean: investimentoInstance, field: "debito")}  </td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${investimentoInstanceTotal}" />
			</div>
		</div>

		<div class="nav" role="navigation">
			<ul>
				<li style="float:right;"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>

	</body>
</html>
