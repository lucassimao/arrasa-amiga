
<%@ page import="br.com.arrasaamiga.ClienteAvulso" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'clienteAvulso.label', default: 'ClienteAvulso')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>

		<style type="text/css">
			.go-back{
				text-decoration:none;
				float:right;
				text-indent: 35px;
				background-repeat: no-repeat;
				background-position: 0.7em center;
				background-image:url(${resource(dir:'images/skin', file:'back.png')});
			}	
		</style>

	</head>
	<body>
		<a href="#show-clienteAvulso" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>


		<div id="show-clienteAvulso" class="content scaffold-show" role="main">
			<h1>
				Detalhes do Contato
				<g:link class="go-back" action="list"> Voltar </g:link>
			</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list clienteAvulso">
			
				<g:if test="${clienteAvulsoInstance?.nome}">
				<li class="fieldcontain">
					<span id="nome-label" class="property-label"><g:message code="clienteAvulso.nome.label" default="Nome" /></span>
					
						<span class="property-value" aria-labelledby="nome-label"><g:fieldValue bean="${clienteAvulsoInstance}" field="nome"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${clienteAvulsoInstance?.telefones}">
				<li class="fieldcontain">
					<span id="telefones-label" class="property-label">
						<g:message code="clienteAvulso.telefones.label" default="Telefones" />
					</span>
					
						<span class="property-value" aria-labelledby="telefones-label">
							${clienteAvulsoInstance.telefones.replace('\n',' / ')}
						</span>
					
				</li>
				</g:if>
			
				<g:if test="${clienteAvulsoInstance?.endereco}">
				<li class="fieldcontain">
					<span id="endereco-label" class="property-label"><g:message code="clienteAvulso.endereco.label" default="Endereco" /></span>
					
						<span class="property-value" aria-labelledby="endereco-label"><g:fieldValue bean="${clienteAvulsoInstance}" field="endereco"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${clienteAvulsoInstance?.facebook}">
				<li class="fieldcontain">
					<span id="facebook-label" class="property-label"><g:message code="clienteAvulso.facebook.label" default="Facebook" /></span>
						
						<g:if test="${clienteAvulsoInstance.facebook}">
							<span class="property-value" aria-labelledby="facebook-label">
								<a target="_blank" href="${clienteAvulsoInstance.facebook}"> 
								Link
								</a>
							</span>
						</g:if>
					
				</li>
				</g:if>
			
				<g:if test="${clienteAvulsoInstance?.observacao}">
				<li class="fieldcontain">
					<span id="observacao-label" class="property-label"><g:message code="clienteAvulso.observacao.label" default="Observacao" /></span>
					
						<span class="property-value" aria-labelledby="observacao-label"><g:fieldValue bean="${clienteAvulsoInstance}" field="observacao"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${clienteAvulsoInstance?.id}" />
					<g:link class="edit" action="edit" id="${clienteAvulsoInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
