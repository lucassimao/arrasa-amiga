<%@ page import="br.com.arrasaamiga.Banner" %>



<div class="fieldcontain ${hasErrors(bean: bannerInstance, field: 'arquivo', 'error')} required">
	<label for="arquivo">
		<g:message code="banner.arquivo.label" default="Arquivo" />
		<span class="required-indicator">*</span>
	</label>
    <input type="file" name="bannerFile" required="">
    Preferencialmente 965x450
</div>

<div class="fieldcontain ${hasErrors(bean: bannerInstance, field: 'link', 'error')} ">
	<label for="link">
		<g:message code="banner.link.label" default="Link" />
		
	</label>
	<g:textField name="link" value="${bannerInstance?.link}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: bannerInstance, field: 'titulo', 'error')} required">
	<label for="titulo">
		<g:message code="banner.titulo.label" default="Titulo" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="titulo" required="" value="${bannerInstance?.titulo}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: bannerInstance, field: 'comentario', 'error')} required">
	<label for="comentario">
		<g:message code="banner.comentario.label" default="Comentario" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="comentario" required="" value="${bannerInstance?.comentario}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: bannerInstance, field: 'visivel', 'error')} ">
	<label for="visivel">
		<g:message code="banner.visivel.label" default="Visivel" />
		
	</label>
	<g:checkBox name="visivel" value="${bannerInstance?.visivel}" />

</div>

