<%@ page import="br.com.arrasaamiga.ClienteAvulso" %>



<div class="fieldcontain ${hasErrors(bean: clienteAvulsoInstance, field: 'nome', 'error')} required">
	<label for="nome">
		<g:message code="clienteAvulso.nome.label" default="Nome" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nome" size="60" required="" value="${clienteAvulsoInstance?.nome}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: clienteAvulsoInstance, field: 'telefones', 'error')} ">
	<label for="telefones">
		<g:message code="clienteAvulso.telefones.label" default="Telefones" />
		
	</label>
	<g:textArea name="telefones" cols="40" rows="5" maxlength="1000" value="${clienteAvulsoInstance?.telefones}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: clienteAvulsoInstance, field: 'endereco', 'error')} ">
	<label for="endereco">
		<g:message code="clienteAvulso.endereco.label" default="Endereco" />
		
	</label>
	<g:textArea name="endereco" cols="40" rows="5" maxlength="5000" value="${clienteAvulsoInstance?.endereco}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: clienteAvulsoInstance, field: 'facebook', 'error')} ">
	<label for="facebook">
		<g:message code="clienteAvulso.facebook.label" default="Facebook" />
		
	</label>
	<g:textField name="facebook" size="60" value="${clienteAvulsoInstance?.facebook}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: clienteAvulsoInstance, field: 'observacao', 'error')} ">
	<label for="observacao">
		<g:message code="clienteAvulso.observacao.label" default="Observacao" />
		
	</label>
	<g:textArea name="observacao" cols="40" rows="5" maxlength="5000" value="${clienteAvulsoInstance?.observacao}"/>
</div>

