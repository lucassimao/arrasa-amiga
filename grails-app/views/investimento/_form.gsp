<%@ page import="br.com.arrasaamiga.Investimento" %>



<div class="fieldcontain ${hasErrors(bean: investimentoInstance, field: 'descricao', 'error')} required">
	<label for="descricao">
		<g:message code="investimento.descricao.label" default="Descricao" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="descricao" required="" value="${investimentoInstance?.descricao}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: investimentoInstance, field: 'valorEmReais', 'error')} required">
	<label for="valorEmReais">
		Valor (R$)
		<span class="required-indicator">*</span>
	</label>
	<g:field name="valorEmReais" min="0" value="${investimentoInstance.valorEmReais.toString().replace('.',',')}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: investimentoInstance, field: 'titular', 'error')} required">
	<label for="titular">
		<g:message code="investimento.titular.label" default="Titular" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="titular" required="" value="${investimentoInstance?.titular}"/>
</div>

