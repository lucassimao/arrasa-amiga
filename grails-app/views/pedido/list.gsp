
<%@ page import="br.com.arrasaamiga.Pedido" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'pedido.label', default: 'Pedido')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-pedido" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="list-pedido" class="content scaffold-list" role="main">
			<h1>Pedidos</h1>

			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
						
						<g:sortableColumn property="id" title="Nº" />

						<g:sortableColumn property="descricao" title="Descrição" />
					
						<g:sortableColumn property="quantidade" title="${message(code: 'pedido.quantidade.label', default: 'Quantidade')}" />
					
						<g:sortableColumn property="dataPedido" title="${message(code: 'pedido.dataPedido.label', default: 'Data Pedido')}" />
					
						<g:sortableColumn property="status" title="Status" />

						<g:sortableColumn property="valorEmCentavosDeReais" title="Custo Total" />
					
						<g:sortableColumn property="link" title="${message(code: 'pedido.link.label', default: 'Link')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${pedidoInstanceList.findAll{it.subGrupo.size()>0 } }" status="i" var="pedidoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						
						<td>${fieldValue(bean: pedidoInstance, field: "id")}</td>

						<td><g:link action="show" id="${pedidoInstance.id}">${fieldValue(bean: pedidoInstance, field: "descricao")}</g:link></td>
					
						<td>${fieldValue(bean: pedidoInstance, field: "quantidade")}</td>
					
						<td><g:formatDate format="dd/MM/yyyy" date="${pedidoInstance.dataPedido}" /></td>

						<td>${fieldValue(bean: pedidoInstance, field: "status")}</td>
					
						<td>R$ ${fieldValue(bean: pedidoInstance, field: "custoTotalEmReais")}</td>
					
						<td> <a href="${fieldValue(bean: pedidoInstance, field: 'link')}"> Go! </a></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${pedidoInstanceTotal}" />
			</div>
		</div>

		<div class="nav" role="navigation" style="margin-top:20px;">
			<ul>
				<li style="float:right;"><g:link class="create" action="create">Cadastrar Novo Pedido</g:link></li>
			</ul>
		</div>

	</body>
</html>
