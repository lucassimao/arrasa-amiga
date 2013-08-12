
<%@ page import="br.com.arrasaamiga.Produto" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'produto.label', default: 'Produto')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-produto" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-produto" class="content scaffold-show" role="main">
			<h1> Detalhes do Produto </h1>

			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list produto">
			

			
				<g:if test="${produtoInstance?.nome}">
				<li class="fieldcontain">
					<span id="nome-label" class="property-label"><g:message code="produto.nome.label" default="Nome" /></span>
					
						<span class="property-value" aria-labelledby="nome-label"><g:fieldValue bean="${produtoInstance}" field="nome"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${produtoInstance?.descricao}">
				<li class="fieldcontain">
					<span id="descricao-label" class="property-label"><g:message code="produto.descricao.label" default="Descricao" /></span>
					
						<span class="property-value" aria-labelledby="descricao-label"><g:fieldValue bean="${produtoInstance}" field="descricao"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${produtoInstance?.tipoUnitario}">
				<li class="fieldcontain">
					<span id="tipoUnitario-label" class="property-label"><g:message code="produto.tipoUnitario.label" default="Tipo Unitario" /></span>
					
						<span class="property-value" aria-labelledby="tipoUnitario-label"><g:fieldValue bean="${produtoInstance}" field="tipoUnitario"/></span>
					
				</li>
				</g:if>

				<g:if test="${produtoInstance?.unidades}">
				<li class="fieldcontain">
					<span id="unidades-label" class="property-label">Unidade(s) </span>
					
						<span class="property-value" aria-labelledby="unidades-label">
							${produtoInstance.unidades.join(',')}
						</span>
					
				</li>
				</g:if>

				<li class="fieldcontain">
					<span id="precoEmCentavos-label" class="property-label"> Valor Unitario </span>
					
						<span class="property-value" aria-labelledby="precoEmCentavos-label">
							R$ ${produtoInstance.precoAVistaEmReais}
						</span>
					
				</li>
			
				<g:if test="${produtoInstance?.fotoMiniatura}">
				<li class="fieldcontain">
					<span id="fotoMiniatura-label" class="property-label"><g:message code="produto.fotoMiniatura.label" default="Foto Miniatura" /></span>
					
					<span class="property-value" aria-labelledby="fotoMiniatura-label">
						<g:img file="${produtoInstance.fotoMiniatura}" dir="img/produtos" />

					</span>
					
				</li>
				</g:if>
			

			
				<g:if test="${produtoInstance?.fotos}">
				<li class="fieldcontain">
					<span id="fotos-label" class="property-label"><g:message code="produto.fotos.label" default="Fotos" /></span>
					
						<span class="property-value" aria-labelledby="fotos-label">
							<g:each in="${produtoInstance.fotos}" var="foto">
								<g:img style="width:10%;" dir="img/produtos" file="${foto}"/>
							</g:each>
						</span>
					
				</li>
				</g:if>
			

			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${produtoInstance?.id}" />
					<g:link style="text-indent: 25px;background-repeat: no-repeat;background-position: 0.7em center;background-image:url(../../images/skin/back.png)" action="list"> Voltar </g:link>
					<g:link class="edit" action="edit" id="${produtoInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
