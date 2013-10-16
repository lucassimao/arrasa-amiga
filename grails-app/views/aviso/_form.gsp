<%@ page import="br.com.arrasaamiga.Aviso" %>



<div class="fieldcontain ${hasErrors(bean: avisoInstance, field: 'nome', 'error')} required">
	<label for="nome">
		<g:message code="aviso.nome.label" default="Nome" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nome" required="" value="${avisoInstance?.nome}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: avisoInstance, field: 'email', 'error')} required">
	<label for="email">
		<g:message code="aviso.email.label" default="Email" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="email" required="" value="${avisoInstance?.email}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: avisoInstance, field: 'celular', 'error')} required">
	<label for="celular">
		<g:message code="aviso.celular.label" default="Celular" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="celular" required="" value="${avisoInstance?.celular}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: avisoInstance, field: 'produto', 'error')} required">
	<label for="produto">
		<g:message code="aviso.produto.label" default="Produto" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="produto" name="produto.id" from="${br.com.arrasaamiga.Produto.list()}" optionValue="nome" optionKey="id" required="" value="${avisoInstance?.produto?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: avisoInstance, field: 'unidade', 'error')} required">
	<label for="unidade">
		<g:message code="aviso.unidade.label" default="Unidade" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="unidade" required="" value="${avisoInstance?.unidade}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: avisoInstance, field: 'facebookUserId', 'error')} ">
	<label for="facebookUserId">
		<g:message code="aviso.facebookUserId.label" default="Facebook User Id" />
		
	</label>
	<g:textField name="facebookUserId" value="${avisoInstance?.facebookUserId}"/>
</div>

