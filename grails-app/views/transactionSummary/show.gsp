
<%@ page import="br.com.arrasaamiga.financeiro.TransactionSummary" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'transactionSummary.label', default: 'TransactionSummary')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-transactionSummary" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="show-transactionSummary" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list transactionSummary">
			
				<g:if test="${transactionSummaryInstance?.code}">
				<li class="fieldcontain">
					<span id="code-label" class="property-label"><g:message code="transactionSummary.code.label" default="Code" /></span>
					
						<span class="property-value" aria-labelledby="code-label"><g:fieldValue bean="${transactionSummaryInstance}" field="code"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${transactionSummaryInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="transactionSummary.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${transactionSummaryInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			

			
				<g:if test="${transactionSummaryInstance?.detalhes}">
				<li class="fieldcontain">
					<span id="detalhes-label" class="property-label"><g:message code="transactionSummary.detalhes.label" default="Detalhes" /></span>
					
						<span class="property-value" aria-labelledby="detalhes-label"><g:fieldValue bean="${transactionSummaryInstance}" field="detalhes"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${transactionSummaryInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="transactionSummary.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${transactionSummaryInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${transactionSummaryInstance?.status}">
				<li class="fieldcontain">
					<span id="status-label" class="property-label"><g:message code="transactionSummary.status.label" default="Status" /></span>
					
						<span class="property-value" aria-labelledby="status-label"><g:fieldValue bean="${transactionSummaryInstance}" field="status"/></span>
					
				</li>
				</g:if>
			

			
				<li class="fieldcontain">
					<span id="valorBrutoEmCentavos-label" class="property-label"><g:message code="transactionSummary.valorBrutoEmCentavos.label" default="Valor Bruto" /></span>
					
						<span class="property-value" aria-labelledby="valorBrutoEmCentavos-label">
							<g:formatNumber type="currency"  currencyCode="BRL"  number="${transactionSummaryInstance.valorBrutoEmReais}" />								
						</span>
					
				</li>

				<li class="fieldcontain">
					<span id="descontoEmCentavos-label" class="property-label"><g:message code="transactionSummary.descontoEmCentavos.label" default="Desconto" /></span>
					
						<span class="property-value" aria-labelledby="descontoEmCentavos-label">
							<g:formatNumber type="currency"  currencyCode="BRL"  number="${transactionSummaryInstance.descontoEmReais}" />
						</span>
					
				</li>				

				<li class="fieldcontain">
					<span id="taxaParcelamentoEmCentavos-label" class="property-label"><g:message code="transactionSummary.taxaParcelamentoEmCentavos.label" default="Taxa Parcelamento" /></span>
					
						<span class="property-value" aria-labelledby="taxaParcelamentoEmCentavos-label">
							<g:formatNumber type="currency"  currencyCode="BRL"  number="${transactionSummaryInstance.taxaParcelamentoEmReais}" />								
							</span>
					
				</li>

			
				<li class="fieldcontain">
					<span id="valorExtraEmCentavos-label" class="property-label"><g:message code="transactionSummary.valorExtraEmCentavos.label" default="Valor Extra" /></span>
					
						<span class="property-value" aria-labelledby="valorExtraEmCentavos-label">
						<g:formatNumber type="currency"  currencyCode="BRL"  number="${transactionSummaryInstance.valorExtraEmReais}" />								
						</span>
					
				</li>
			
				<g:if test="${transactionSummaryInstance?.valorLiquidoEmReais}">
				<li class="fieldcontain">
					<span id="valorLiquidoEmCentavos-label" class="property-label"><g:message code="transactionSummary.valorLiquidoEmCentavos.label" default="Valor Liquido" /></span>
					
						<span class="property-value" aria-labelledby="valorLiquidoEmCentavos-label">
							<g:formatNumber type="currency"  currencyCode="BRL"  number="${transactionSummaryInstance.valorLiquidoEmReais}" />								
						</span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:transactionSummaryInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${transactionSummaryInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
