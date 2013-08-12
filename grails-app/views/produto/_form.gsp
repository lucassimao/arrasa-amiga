<%@ page import="br.com.arrasaamiga.Produto" %>

<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'nome', 'error')} required">
	<label for="nome">
		<g:message code="produto.nome.label" default="Nome" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField style="width:400px;" name="nome" required="" value="${produtoInstance?.nome}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'descricao', 'error')} required">
	<label for="descricao">
		<g:message code="produto.descricao.label" default="Descricao" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea name="descricao" style="width:400px;" cols="40" rows="5" maxlength="100000" required="" value="${produtoInstance?.descricao}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'tipoUnitario', 'error')} required">
	<label for="tipoUnitario">
		<g:message code="produto.tipoUnitario.label" default="Tipo Unitario" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="tipoUnitario" required="" value="${produtoInstance?.tipoUnitario}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'tipoUnitario', 'error')} required">
	<label for="tipoUnitario">
		Unidade(s):
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="unidades" required="" value="${produtoInstance?.unidades?.join(',')}"/>
</div>



<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'precoAVistaEmReais', 'error')} required">
	<label for="precoEmReais">
		Valor Unitário A Vista
		<span class="required-indicator">*</span>
	</label>
	<g:field name="precoAVistaEmReais"  min="0" value="${produtoInstance.precoAVistaEmReais.toString().replace('.',',')}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'precoAPrazoEmReais', 'error')} required">
	<label for="precoEmReais">
		Valor Unitário A Prazo
		<span class="required-indicator">*</span>
	</label>
	<g:field name="precoAPrazoEmReais"  min="0" value="${produtoInstance.precoAPrazoEmReais.toString().replace('.',',')}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'fotoMiniatura', 'error')} required">
	<label for="fotoMiniatura">
		Foto Miniatura
		<span class="required-indicator">*</span>
	</label>
	<g:field type="file" name="fotoMiniatura" required=""/>
	
</div>

<div class="fieldcontain" style="overflow:visible;position:relative;" >
	<label for="fotos">
		Fotos		
	</label>


	<div id="div-fotos" style="position:relative;top:-20px;left:220px;">
		<a id="adicionar-input-file" style="margin-top:10px;" href="#"> Adicionar Foto </a>
	</div>
	
</div>

<g:javascript>

	$(function(){

		$("#adicionar-input-file").click(function(){
			var len = $("input[name^='fotos']").length;

			var input = $("<input style='display:block;margin-bottom:5px;' type='file' />");
			input.attr('name',"fotos" + len);

			input.click();

			if (len > 0){
				input.insertBefore("input[name^='fotos']:first");
			}else{
				input.insertBefore("#adicionar-input-file");
			}
		});
	});
</g:javascript>

