
<%@ page import="br.com.arrasaamiga.Feriado" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'feriado.label', default: 'Feriado')}" />
		<title>Feriados</title>
	</head>
	<body>
		<a href="#list-feriado" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="list-feriado" class="content scaffold-list" role="main">
			<h1> Feriados </h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
				<tr>

					<g:sortableColumn property="id" title="Nº" />
					<g:sortableColumn property="descricao" title="Descrição" />
					<g:sortableColumn property="inicio" title="Início" />
					<g:sortableColumn property="fim" title="Fim" />

				</tr>
				</thead>
				<tbody>
				<g:each in="${feriadoInstanceList}" status="i" var="feriadoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

						<td>${fieldValue(bean: feriadoInstance, field: "id")}</td>

						<td><g:link action="show" id="${feriadoInstance.id}">${fieldValue(bean: feriadoInstance, field: "descricao")}</g:link></td>

						<td>${formatDate(date: feriadoInstance.inicio, type: 'date')}</td>
						<td>${formatDate(date: feriadoInstance.fim, type: 'date')}</td>

					</tr>
				</g:each>
				</tbody>
			</table>

            <g:if test="${feriadoInstanceTotal > 10}">
                <div class="pagination">
                    <g:paginate total="${feriadoInstanceTotal ?: 0}" />
                </div>
            </g:if>
		</div>

		<div class="nav" role="navigation">
			<ul>
				<li style="float:right;"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
	</body>
</html>
