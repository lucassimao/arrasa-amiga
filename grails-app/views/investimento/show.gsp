
<%@ page import="br.com.arrasaamiga.Investimento" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'investimento.label', default: 'Investimento')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
		<style type="text/css">
			.go-back{
				text-decoration:none;
				float:right;
				text-indent: 35px;
				background-repeat: no-repeat;
				background-position: 0.7em center;
				background-image:url( ${ resource(dir:'images/skin',file:'back.png') } );
			}	
		</style>
	</head>
	<body>
		<a href="#show-investimento" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="show-investimento" class="content scaffold-show" role="main">
			<h1> Detalhes
				<g:link class="go-back"	action="list"> Voltar </g:link>
			</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list investimento">
			
				<g:if test="${investimentoInstance?.descricao}">
				<li class="fieldcontain">
					<span id="descricao-label" class="property-label"><g:message code="investimento.descricao.label" default="Descricao" /></span>
					
						<span class="property-value" aria-labelledby="descricao-label"><g:fieldValue bean="${investimentoInstance}" field="descricao"/></span>
					
				</li>
				</g:if>
			

			
				<g:if test="${investimentoInstance?.titular}">
					<li class="fieldcontain">
						<span id="titular-label" class="property-label"><g:message code="investimento.titular.label" default="Titular" /></span>
						
							<span class="property-value" aria-labelledby="titular-label"><g:fieldValue bean="${investimentoInstance}" field="titular"/></span>
						
					</li>
				</g:if>

				<li class="fieldcontain">
					<span id="valorEmCentavos-label" class="property-label">Valor Total (R$)
					</span>
					
						<span class="property-value" aria-labelledby="valorEmCentavos-label">
						<g:fieldValue bean="${investimentoInstance}" field="valorEmReais"/></span>
					
				</li>


				<li class="fieldcontain">
					<span id="valorEmCentavos-label" class="property-label">Valor Quitado (R$)
					</span>
					
						<span class="property-value" aria-labelledby="valorEmCentavos-label">
						<g:fieldValue bean="${investimentoInstance}" field="valorQuitado"/></span>
				
				</li>

				<li class="fieldcontain">
					<span id="valorEmCentavos-label" class="property-label">Debito (R$)
					</span>
					
						<span class="property-value" aria-labelledby="valorEmCentavos-label">
						<g:fieldValue bean="${investimentoInstance}" field="debito"/></span>
					
				</li>
			

			
				<g:if test="${investimentoInstance?.pagamentos}">
				<li class="fieldcontain">
					<span id="pagamentos-label" class="property-label"><g:message code="investimento.pagamentos.label" default="Pagamentos" /></span>
					
						<g:each in="${investimentoInstance.pagamentos}" var="p">
						<span class="property-value" aria-labelledby="pagamentos-label"><g:link controller="pagamentoInvestimento" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${investimentoInstance?.id}" />
					<g:link class="edit" action="edit" id="${investimentoInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
