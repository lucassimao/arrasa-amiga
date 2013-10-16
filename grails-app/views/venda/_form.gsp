<%@ page import="br.com.arrasaamiga.Venda" %>



<div class="fieldcontain ${hasErrors(bean: vendaInstance, field: 'freteEmCentavos', 'error')} required">
	<label for="freteEmCentavos">
		<g:message code="venda.freteEmCentavos.label" default="Frete Em Centavos" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="freteEmCentavos" type="number" min="0" value="${vendaInstance.freteEmCentavos}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: vendaInstance, field: 'subTotalItensEmCentavos', 'error')} required">
	<label for="subTotalItensEmCentavos">
		<g:message code="venda.subTotalItensEmCentavos.label" default="Sub Total Itens Em Centavos" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="subTotalItensEmCentavos" type="number" min="0" value="${vendaInstance.subTotalItensEmCentavos}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: vendaInstance, field: 'descontoEmCentavos', 'error')} required">
	<label for="descontoEmCentavos">
		<g:message code="venda.descontoEmCentavos.label" default="Desconto Em Centavos" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="descontoEmCentavos" type="number" min="0" value="${vendaInstance.descontoEmCentavos}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: vendaInstance, field: 'taxasPagSeguroEmCentavos', 'error')} required">
	<label for="taxasPagSeguroEmCentavos">
		<g:message code="venda.taxasPagSeguroEmCentavos.label" default="Taxas Pag Seguro Em Centavos" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="taxasPagSeguroEmCentavos" type="number" min="0" value="${vendaInstance.taxasPagSeguroEmCentavos}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: vendaInstance, field: 'itensVenda', 'error')} required">
	<label for="itensVenda">
		<g:message code="venda.itensVenda.label" default="Itens Venda" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="itensVenda" from="${br.com.arrasaamiga.ItemVenda.list()}" multiple="multiple" optionKey="id" size="5" required="" value="${vendaInstance?.itensVenda*.id}" class="many-to-many"/>
</div>

<div class="fieldcontain ${hasErrors(bean: vendaInstance, field: 'formaPagamento', 'error')} required">
	<label for="formaPagamento">
		<g:message code="venda.formaPagamento.label" default="Forma Pagamento" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="formaPagamento" from="${br.com.arrasaamiga.FormaPagamento?.values()}" keys="${br.com.arrasaamiga.FormaPagamento.values()*.name()}" required="" value="${vendaInstance?.formaPagamento?.name()}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: vendaInstance, field: 'status', 'error')} required">
	<label for="status">
		<g:message code="venda.status.label" default="Status" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="status" from="${br.com.arrasaamiga.StatusVenda?.values()}" keys="${br.com.arrasaamiga.StatusVenda.values()*.name()}" required="" value="${vendaInstance?.status?.name()}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: vendaInstance, field: 'cliente', 'error')} required">
	<label for="cliente">
		<g:message code="venda.cliente.label" default="Cliente" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="cliente" name="cliente.id" from="${br.com.arrasaamiga.Cliente.list()}" optionKey="id" required="" value="${vendaInstance?.cliente?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: vendaInstance, field: 'dataEntrega', 'error')} ">
	<label for="dataEntrega">
		<g:message code="venda.dataEntrega.label" default="Data Entrega" />
		
	</label>
	<g:datePicker name="dataEntrega" precision="day"  value="${vendaInstance?.dataEntrega}" default="none" noSelection="['': '']" />
</div>

<div class="fieldcontain ${hasErrors(bean: vendaInstance, field: 'transacaoPagSeguro', 'error')} ">
	<label for="transacaoPagSeguro">
		<g:message code="venda.transacaoPagSeguro.label" default="Transacao Pag Seguro" />
		
	</label>
	<g:textField name="transacaoPagSeguro" value="${vendaInstance?.transacaoPagSeguro}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: vendaInstance, field: 'carrinho', 'error')} ">
	<label for="carrinho">
		<g:message code="venda.carrinho.label" default="Carrinho" />
		
	</label>
	<g:select id="carrinho" name="carrinho.id" from="${br.com.arrasaamiga.ShoppingCart.list()}" optionKey="id" value="${vendaInstance?.carrinho?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

