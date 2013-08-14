<%@ page import="br.com.arrasaamiga.Pedido" %>



<div class="fieldcontain ${hasErrors(bean: pedidoInstance, field: 'descricao', 'error')} required">
	<label for="descricao">
		<g:message code="pedido.descricao.label" default="Descricao" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="descricao" size="50" required="" value="${pedidoInstance?.descricao}"/>
</div>



<div class="fieldcontain ${hasErrors(bean: pedidoInstance, field: 'dataPedido', 'error')} required">
	<label for="dataPedido">
		<g:message code="pedido.dataPedido.label" default="Data do Pedido" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="dataPedido" precision="day"  value="${pedidoInstance?.dataPedido}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: pedidoInstance, field: 'quantidade', 'error')} required">
	<label for="quantidade">
		<g:message code="pedido.quantidade.label" default="Quantidade" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="quantidade" type="number" min="1" value="${pedidoInstance.quantidade}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: pedidoInstance, field: 'valorEmCentavosDeReais', 'error')} required">
	<label for="valorEmCentavosDeReais">
		Valor (R$)
		<span class="required-indicator">*</span>
	</label>
	<g:field name="valorEmReais" min="0" value="${pedidoInstance.valorEmReais}" required=""/>
</div>



<div class="fieldcontain ${hasErrors(bean: pedidoInstance, field: 'freteEmCentavosDeReais', 'error')} required">
	<label for="freteEmCentavosDeReais">
		Frete (R$)
		<span class="required-indicator">*</span>
	</label>
	<g:field name="freteEmReais" min="0" value="${pedidoInstance.freteEmReais}" required=""/>
</div>





<div class="fieldcontain ${hasErrors(bean: pedidoInstance, field: 'link', 'error')} ">
	<label for="link">
		<g:message code="pedido.link.label" default="Link" />
		
	</label>
	<g:textField size="60" name="link" value="${pedidoInstance?.link}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pedidoInstance, field: 'codigoRastreio', 'error')} ">
	<label for="codigoRastreio">
		<g:message code="pedido.codigoRastreio.label" default="Codigo Rastreio" />
		
	</label>
	<g:textField name="codigoRastreio" value="${pedidoInstance?.codigoRastreio}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pedidoInstance, field: 'status', 'error')} required">
	<label for="status">
		<g:message code="pedido.status.label" default="Status" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="status" from="${br.com.arrasaamiga.StatusPedido?.values()}" keys="${br.com.arrasaamiga.StatusPedido.values()*.name()}" required="" value="${pedidoInstance?.status?.name()}"/>
</div>

