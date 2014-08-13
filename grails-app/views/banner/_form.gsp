<%@ page import="br.com.arrasaamiga.Banner" %>



<div class="fieldcontain ${hasErrors(bean: bannerInstance, field: 'arquivo', 'error')} required">
	<label for="arquivo">
		Banner
		<span class="required-indicator">*</span>
	</label>
    <input type="file" name="bannerFile" required="">  Preferencialmente 965x450
</div>

<div class="fieldcontain ${hasErrors(bean: bannerInstance, field: 'link', 'error')} ">
	<label for="link">
		<g:message code="banner.link.label" default="Link" />
		
	</label>
	<g:textField name="link" value="${bannerInstance?.link}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: bannerInstance, field: 'titulo', 'error')}">
	<label for="titulo">
		<g:message code="banner.titulo.label" default="Titulo" />
	</label>
	<g:textField name="titulo" value="${bannerInstance?.titulo}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: bannerInstance, field: 'comentario', 'error')}">
	<label for="comentario">
		<g:message code="banner.comentario.label" default="Comentario" />
	</label>
	<g:textField name="comentario" value="${bannerInstance?.comentario}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: bannerInstance, field: 'visivel', 'error')} ">
	<label for="visivel">
		<g:message code="banner.visivel.label" default="Visivel" />
		
	</label>
	<g:checkBox name="visivel" value="${bannerInstance?.visivel}" />

</div>

