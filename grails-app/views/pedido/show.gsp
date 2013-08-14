
<%@ page import="br.com.arrasaamiga.Pedido" %>
<%@ page import="br.com.arrasaamiga.StatusPedido" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'pedido.label', default: 'Pedido')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
		<style type="text/css">
			.go-back{
				text-decoration:none;
				float:right;
				text-indent: 35px;
				background-repeat: no-repeat;
				background-position: 0.7em center;
				background-image:url(${resource(dir: 'images/skin', file: 'back.png')});
			}	
		</style>

	</head>
	<body>
		<a href="#show-pedido" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-pedido" class="content scaffold-show" role="main">
			<h1> 
				Detalhes do Pedido 
				<g:link class="go-back"	action="list"> Voltar </g:link>
			</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list pedido">
			
				<li class="fieldcontain">
					<span id="descricao-label" class="property-label"><g:message code="pedido.descricao.label" default="Descricao" /></span>
					
						<span class="property-value" aria-labelledby="descricao-label"><g:fieldValue bean="${pedidoInstance}" field="descricao"/></span>
					
				</li>
			
				<li class="fieldcontain">
					<span id="dataPedido-label" class="property-label"><g:message code="pedido.dataPedido.label" default="Data Pedido" /></span>
					
						<span class="property-value" aria-labelledby="dataPedido-label">
							<g:formatDate date="${pedidoInstance?.dataPedido}" format="dd/MM/yyyy" /></span>
					
				</li>


				<li class="fieldcontain">
					<span id="quantidade-label" class="property-label">Quantidade</span>
					
						<span class="property-value" aria-labelledby="quantidade-label"><g:fieldValue bean="${pedidoInstance}" field="quantidade"/></span>
					
				</li>

			
				<li class="fieldcontain">
					<span id="valorEmCentavosDeReais-label" class="property-label">
						Valor
					</span>
					
					<span class="property-value" aria-labelledby="valorEmCentavosDeReais-label">
						R$ <g:fieldValue bean="${pedidoInstance}" field="valorEmReais"/>
					</span>
					
				</li>

				<li class="fieldcontain">
					<span  class="property-label">
						Frete
					</span>
					
					<span class="property-value">
						R$ <g:fieldValue bean="${pedidoInstance}" field="freteEmReais"/>
					</span>
					
				</li>

				<li class="fieldcontain">
					<span  class="property-label">
						IOF
					</span>
					
					<span class="property-value">
						R$ <g:fieldValue bean="${pedidoInstance}" field="iofEmReais"/>
					</span>
					
				</li>

				<li class="fieldcontain">
					<span  class="property-label">
						Custo Total
					</span>
					
					<span class="property-value">
						R$ <g:fieldValue bean="${pedidoInstance}" field="custoTotalEmReais"/>
					</span>
					
				</li>

				<li class="fieldcontain">
					<span  class="property-label">
						Custo Unitário
					</span>
					
					<span class="property-value">
						R$ <g:fieldValue bean="${pedidoInstance}" field="custoUnitarioEmReais"/>
					</span>
					
				</li>

				
			
				<g:if test="${pedidoInstance?.link}">
				<li class="fieldcontain">
					<span id="link-label" class="property-label"><g:message code="pedido.link.label" default="Link" /></span>
					
						<span class="property-value" aria-labelledby="link-label">
						 	<a target="_blank" href="${fieldValue(bean: pedidoInstance, field: 'link')}"> <g:fieldValue bean="${pedidoInstance}" field="link"/> </a>
						</span>
					
				</li>
				</g:if>
			
				<g:if test="${pedidoInstance?.codigoRastreio}">
				<li class="fieldcontain">
					<span id="codigoRastreio-label" class="property-label"><g:message code="pedido.codigoRastreio.label" default="Codigo Rastreio" /></span>
					
						<span class="property-value" aria-labelledby="codigoRastreio-label">
							<a target="_blank" href="${pedidoInstance.urlRastreioCorreios}"> ${pedidoInstance.codigoRastreio} </a>
						</span>
					
				</li>
				</g:if>
			
				<g:if test="${pedidoInstance?.status}">
				<li class="fieldcontain">
					<span id="status-label" class="property-label"><g:message code="pedido.status.label" default="Status" /></span>
					
						<span class="property-value" aria-labelledby="status-label"><g:fieldValue bean="${pedidoInstance}" field="status"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:if test="${pedidoInstance?.status == StatusPedido.Aguardando}">
				<g:form>
					<fieldset class="buttons">
						<g:hiddenField name="id" value="${pedidoInstance?.id}" />
						<g:link class="edit" action="edit" id="${pedidoInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
						<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</fieldset>
				</g:form>
			</g:if>
		</div>
	</body>
</html>
