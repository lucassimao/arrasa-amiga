<%@ page import="br.com.arrasaamiga.Estoque" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'estoque.label', default: 'Estoque')}" />
		<title> Entrada de Estoque </title>

		<style type="text/css">
			.go-back{
				text-decoration:none;
				float:right;
				text-indent: 35px;
				background-repeat: no-repeat;
				background-position: 0.7em center;
				background-image:url(${resource(dir:'images/skin',file:'back.png')});
			}	
		</style>

	</head>
	<body>
		<a href="#edit-estoque" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="edit-estoque" class="content scaffold-edit" role="main">
			<h1> 

				Entrada de Estoque 
				<g:link class="go-back"	action="list"> Voltar </g:link>
			</h1>
			
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${estoqueInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${estoqueInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>

			<g:form method="post" >
				<g:hiddenField name="id" value="${estoqueInstance?.id}" />
				<g:hiddenField name="version" value="${estoqueInstance?.version}" />
				
				<fieldset class="form">

					<div class="fieldcontain required">
						<label for="produto">
							<g:message code="estoque.produto.label" default="Produto" />
						</label>

						${estoqueInstance.produto.nome.encodeAsHTML()}
					</div>

					<div class="fieldcontain required">
						<label for="unidade">
							<g:message code="estoque.unidade.label" default="Unidade" />
						</label>

						${estoqueInstance.unidade.encodeAsHTML()}
					</div>

					<div class="fieldcontain required">
						<label for="pedido">
							Pedido 
							<span class="required-indicator">*</span>
						</label>
						<g:select optionKey="id" optionValue="descricao" from="${pedidosEmAberto}" required="" name="pedido.id"/>
					</div>

					<!--
					<div class="fieldcontain ${hasErrors(bean: estoqueInstance, field: 'quantidade', 'error')} required">
						<label for="quantidade">
							Quantidade 
							<span class="required-indicator">*</span>
						</label>
						<g:field name="quantidade" type="number" min="0" value="0" required=""/>
					</div>
					-->


				</fieldset>

				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="Confirmar" />
				</fieldset>
			</g:form>


		</div>
	</body>
</html>
