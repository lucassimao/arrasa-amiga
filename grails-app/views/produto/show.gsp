
<%@ page import="br.com.arrasaamiga.Produto" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'produto.label', default: 'Produto')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>

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
		<a href="#show-produto" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		
		<div id="show-produto" class="content scaffold-show" role="main">
			<h1> 
				Detalhes do Produto 
				
				<g:link class="go-back" action="list"> Voltar </g:link>

			</h1>

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


				<li class="fieldcontain">
					<span class="property-label">Marca</span>
					
					<span class="property-value" aria-labelledby="marca-label">
						${ produtoInstance.marca }
					</span>
					
				</li>


				<li class="fieldcontain">
					<span class="property-label">Tags</span>
					
						<span class="property-value" aria-labelledby="tags-label">
							${ produtoInstance.keywords?.join(',') }
						</span>
					
				</li>

				<li class="fieldcontain">
					<span class="property-label">Grupos</span>
					
						<span class="property-value" aria-labelledby="tags-label">
							${ produtoInstance.grupos?.join(',') }
						</span>
					
				</li>
				
				<li class="fieldcontain">
					<span class="property-label">Visível</span>
					
						<span class="property-value" aria-labelledby="descricao-label">
							${ (produtoInstance.visivel)?'Sim':'Não'}
						</span>
					
				</li>

				<g:if test="${produtoInstance?.tipoUnitario}">
				<li class="fieldcontain">
					<span id="tipoUnitario-label" class="property-label"><g:message code="produto.tipoUnitario.label" default="Tipo Unitario" /></span>
					
						<span class="property-value" aria-labelledby="tipoUnitario-label"><g:fieldValue bean="${produtoInstance}" field="tipoUnitario"/></span>
					
				</li>
				</g:if>

				<li class="fieldcontain">
					<span id="unidades-label" class="property-label">Unidade(s) </span>
					
						<span class="property-value" aria-labelledby="unidades-label">
							${produtoInstance.unidades.join(',')}
						</span>
					
				</li>

				<li class="fieldcontain">
					<span  class="property-label"> Valor Unitario A Vista </span>
					
						<span class="property-value">
							<g:formatNumber number="${produtoInstance.precoAVistaEmReais}" type="currency" 
								currencyCode="BRL" />
						</span>
					
				</li>

				<li class="fieldcontain">
					<span class="property-label"> Valor Unitario A Prazo </span>
					
						<span class="property-value">
							<g:formatNumber number="${produtoInstance.precoAPrazoEmReais}" type="currency" 
								currencyCode="BRL" />	
						</span>
					
				</li>
			
					<g:if test="${produtoInstance?.fotoMiniatura}">
						<li class="fieldcontain">
							<span id="fotoMiniatura-label" class="property-label"><g:message code="produto.fotoMiniatura.label" default="Foto Miniatura" /></span>
							
							<span class="property-value" aria-labelledby="fotoMiniatura-label">
								<img src="${resource(dir:'images/produtos',file: produtoInstance.fotoMiniatura)}"  />

							</span>
							
						</li>
					</g:if>
			

			
				<g:if test="${produtoInstance?.fotos}">
					<li class="fieldcontain">
						<span id="fotos-label" class="property-label">Fotos</span>
						

						<div class="property-value" aria-labelledby="fotos-label">
							<g:each in="${produtoInstance.unidades}" var="unidade">
								<div style="padding:2px;border-bottom:1px solid black;position:relative;"> 
									<h4> ${unidade} </h4>
								</div>


								<g:each in="${produtoInstance.fotos.findAll{f-> f.unidade.equals(unidade) } }" var="fotoProduto" >

									<div style="height:80px;margin-top:10px;padding:5px;" class="produto-unidade-foto">

										<img src="${resource(dir:'images/produtos',file: fotoProduto.arquivo)}" class="produto-foto" style="margin-right:5px;border:1px solid blue;width:70px;height:70px;float:left;"/>

										<div class="comentario" style="position:relative;height:70px;overflow: hidden;">
											<p> ${fotoProduto.comentario} </p>
										</div>

									</div>	

								</g:each>
							</g:each>
						</div>
						
					</li>

				</g:if>
			

			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${produtoInstance?.id}" />
					<g:link class="edit" action="edit" id="${produtoInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<!-- <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" 
							onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /> -->
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
