
<%@ page import="br.com.arrasaamiga.Produto" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'produto.label', default: 'Produto')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>

		<style type="text/css">
			table {
			}

			table td {
			}
		</style>
	</head>
	<body>
		<a href="#list-produto" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		


		<div id="list-produto" class="content scaffold-list" role="main">
			<h1> Produtos </h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
				<thead>
					<tr>
					
					
						<g:sortableColumn property="nome" title="Nome" />
						<th> Valor A Vista </th>
						<th> Valor A Prazo </th>
						<th> Unidades </th>
					
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${produtoInstanceList}" status="i" var="produtoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}" >
					
						<td>
							<g:link action="show" id="${produtoInstance.id}" >
							${fieldValue(bean: produtoInstance, field: "nome")}
							</g:link>
						</td>
					
					
						<td>
							<g:formatNumber number="${produtoInstance.precoAVistaEmReais}" type="currency" 
								currencyCode="BRL" />
						</td>
						<td> 
							<g:formatNumber number="${produtoInstance.precoAPrazoEmReais}" type="currency" 
								currencyCode="BRL" />							
						</td>
					
						<td>${produtoInstance.unidades.join(',')}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${produtoInstanceTotal}" />
			</div>
		</div>


		<div class="nav" role="navigation" style="margin-top:20px;">
			<ul>
				<li style="float:right;"><g:link class="create" action="create"> Cadastrar Novo Produto</g:link></li>
			</ul>
		</div>

	</body>
</html>
