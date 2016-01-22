<%@ page import="br.com.arrasaamiga.financeiro.TransactionSummary" %>



<div class="fieldcontain ${hasErrors(bean: transactionSummaryInstance, field: 'code', 'error')} required">
	<label for="code">
		<g:message code="transactionSummary.code.label" default="Code" />
	</label>
	${transactionSummaryInstance?.code}

</div>

<div class="fieldcontain ${hasErrors(bean: transactionSummaryInstance, field: 'detalhes', 'error')} required">
	<label for="detalhes">
		<g:message code="transactionSummary.detalhes.label" default="Detalhes" />
	</label>
	${transactionSummaryInstance?.detalhes}

</div>

<div class="fieldcontain ${hasErrors(bean: transactionSummaryInstance, field: 'status', 'error')} required">
	<label for="status">
		<g:message code="transactionSummary.status.label" default="Status" />
	</label>
	${transactionSummaryInstance?.status?.name()}
</div>



<div class="fieldcontain ${hasErrors(bean: transactionSummaryInstance, field: 'valorBrutoEmCentavos', 'error')} required">
	<label for="valorBrutoEmCentavos">
		<g:message code="transactionSummary.valorBrutoEmCentavos.label" default="Valor Bruto" />
	</label>
	<g:formatNumber type="currency"  currencyCode="BRL"  number="${transactionSummaryInstance.valorBrutoEmReais}" />								
</div>


<div class="fieldcontain ${hasErrors(bean: transactionSummaryInstance, field: 'descontoEmCentavos', 'error')} required">
	<label for="descontoEmCentavos">
		<g:message code="transactionSummary.descontoEmCentavos.label" default="Desconto" />
	</label>
	<g:formatNumber type="currency"  currencyCode="BRL"  number="${transactionSummaryInstance.descontoEmReais}" />


</div>

<div class="fieldcontain ${hasErrors(bean: transactionSummaryInstance, field: 'taxaParcelamentoEmCentavos', 'error')} required">
	<label for="taxaParcelamentoEmCentavos">
		<g:message code="transactionSummary.taxaParcelamentoEmCentavos.label" default="Taxa Parcelamento" />
	</label>
	<g:formatNumber type="currency"  currencyCode="BRL"  number="${transactionSummaryInstance.taxaParcelamentoEmReais}" />								
</div>



<div class="fieldcontain ${hasErrors(bean: transactionSummaryInstance, field: 'valorExtraEmCentavos', 'error')} required">
	<label for="valorExtraEmCentavos">
		<g:message code="transactionSummary.valorExtraEmCentavos.label" default="Valor Extra" />
	</label>
	<g:formatNumber type="currency"  currencyCode="BRL"  number="${transactionSummaryInstance.valorExtraEmReais}" />								
</div>

<div class="fieldcontain ${hasErrors(bean: transactionSummaryInstance, field: 'valorLiquidoEmCentavos', 'error')} required">
	<label for="valorLiquidoEmCentavos">
		<g:message code="transactionSummary.valorLiquidoEmCentavos.label" default="Valor Liquido" />
	</label>
	<g:formatNumber type="currency"  currencyCode="BRL"  number="${transactionSummaryInstance.valorLiquidoEmReais}" />								
</div>

<div class="fieldcontain required">
	<label for="venda"> Venda </label>
	<g:select name="venda.id"
				from="${vendasSemTransacoes}" optionKey="id" />
</div>

