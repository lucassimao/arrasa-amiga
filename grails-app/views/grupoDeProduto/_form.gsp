<%@ page import="br.com.arrasaamiga.GrupoDeProduto" %>



<div class="fieldcontain ${hasErrors(bean: grupoDeProdutoInstance, field: 'nome', 'error')} required">
	<label for="nome">
		<g:message code="grupoDeProduto.nome.label" default="Nome" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nome" required="" value="${grupoDeProdutoInstance?.nome}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: grupoDeProdutoInstance, field: 'pai', 'error')} ">
	<label for="pai">
		<g:message code="grupoDeProduto.pai.label" default="Pai" />
		
	</label>
	<g:select id="pai" name="pai.id" from="${br.com.arrasaamiga.GrupoDeProduto.list()}" optionKey="id" value="${grupoDeProdutoInstance?.pai?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

