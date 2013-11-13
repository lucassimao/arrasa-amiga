<%@ page import="br.com.arrasaamiga.FormaPagamento" %>

<div id="div-subtotal">
	<h4 style="display:inline;color:#666;">Subtotal</h4>

	<div style="float:right;font-weight:bold;font-size:16px;color:#666;"> 
		<g:formatNumber number="${venda.valorItensAPrazo}" type="currency"	currencyCode="BRL" />
	</div>
</div>



<g:if test="${!venda.cliente.isDentroDaAreaDeEntregaRapida()}">
	
	<div id="div-frete" style="padding:0 6px;clear:both;background-color:#C3EDBE;-moz-border-radius: 15px;border-radius: 15px;">
		<h5 style="display:inline;color:blue;">Frete</h5>

		<div style="float:right;font-weight:bold;"> 
			<div style="color:blue;font-size:10px;text-align:right;">
				+ <g:formatNumber number="${venda.freteEmReais}" type="currency" currencyCode="BRL" />
			</div>
		</div>

	</div>

</g:if>


<g:if test="${venda.formaPagamento.equals(FormaPagamento.AVista)}">

	<div id="div-desconto" style="padding:0 6px;clear:both;background-color:yellow;-moz-border-radius: 15px;border-radius: 15px;">
		<h5 style="display:inline;color:blue;">Desconto</h5>

		<div style="float:right;font-weight:bold;"> 
			<div style="color:blue;font-size:11px;text-align:right;">
				- <g:formatNumber number="${venda.descontoParaCompraAVista}" type="currency" currencyCode="BRL" />
			</div>
		</div>

	</div>

</g:if>

<g:if test="${venda.taxaEntregaEmReais > 0 }">

	<div id="div-taxa-entrega" style="margin-top:2px;clear:both;padding:0 6px;background-color:#F2AAE0;-moz-border-radius: 15px;border-radius: 15px;">
		<h5 style="display:inline;color:blue;">Taxa de entrega </h5>

		<div style="float:right;font-weight:bold;"> 
			<div style="color:blue;font-size:11px;text-align:right;">
				+ <g:formatNumber number="${venda.taxaEntregaEmReais}" type="currency" currencyCode="BRL" />
			</div>
		</div>

	</div>

</g:if>

<div id="div-valor-total">
	<hr>

	<h4 style="color:#666;display:inline;">Valor Total</h4>

	<div style="float:right;font-weight:bold;font-size:35px;color:#00adef;"> 
		<g:formatNumber number="${venda.valorTotal}" type="currency"	currencyCode="BRL" />
	</div>
</div>