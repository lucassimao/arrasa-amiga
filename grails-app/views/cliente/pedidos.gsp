<%@ page import="br.com.arrasaamiga.Produto" %>
<%@ page import="br.com.arrasaamiga.FormaPagamento" %>


<html>
<head>
	<meta name='layout' content='main'/>
	<style type='text/css' media='screen'></style>
</head>

	<body>

		<g:set var="ocultarRodape" value="${true}" scope="request"/>

		<div class="well" style="background-color:white;margin-top:10px;">

			<div class="accordion" id="accordion">

				<g:each in="${pedidos}" var="pedido" status="idx">



					<div class="accordion-group">

						<div class="accordion-heading">
							<a class="accordion-toggle" style="font-weight:bold;font-size:16px;" data-toggle="collapse" data-parent="#accordion" href="#collapse${idx}">
								Pedido #${ String.format("%05d",pedido.id) }
							</a>
						</div>

						<div id="collapse${idx}" class="accordion-body collapse">
							
							<div class="accordion-inner">

								<!-- well time line -->
								<div class="well" style="text-align:center;background-color:white;color:rgb(102, 102, 102);">

									<g:if test="${pedido.formaPagamento.equals(FormaPagamento.AVista)}">
										<g:img style="margin-top:45px;" dir="img" file="timeline-02.png"/>
									</g:if>
									<g:else>
										<g:img style="margin-top:45px;" dir="img" file="timeline-01.png"/>
									</g:else>

								</div>
								<!-- fim well time line -->


								<!-- well dados de entrega -->

								<div class="well" style="background-color:white;">

									<legend> <i class="icon-truck"></i> Entrega </legend>

									<div class="row-fluid">
										<div>

											<g:set var="cliente" value="${pedido.cliente}"/>

											<label> <span class="caption"> Estado: </span> ${cliente.endereco.uf.nome}</label>
											<label> <span class="caption"> Cidade: </span> ${cliente.endereco.cidade.nome}</label>
											<label> <span class="caption"> Bairro: </span> ${cliente.endereco.bairro} </label>
											<label> <span class="caption"> Complemento: </span>  ${cliente.endereco.complemento}</label>

											<g:if test="${!cliente.isDentroDaAreaDeEntregaRapida()}">
												<label> <span class="caption"> CEP: </span> ${cliente.endereco.cep} </label>
											</g:if>

											<g:if test="${cliente.isDentroDaAreaDeEntregaRapida()}">
												<label> <span class="caption"> Dia da Entrega: </span> <g:formatDate format="EEEE, dd/MM/yyyy" date="${pedido.dataEntrega}"/> </label>
											</g:if>


										</div>

									</div>

								</div>
								<!-- fim well dados de entrega -->

								
								<!-- well produtos do pedido -->
								<div class="row-flow" style="margin:20px 0px;"> 

									<table class="table table-bordered" style="background-color:white;">

										<thead>
											<tr>
												<th> Descrição </th>
												<th> Quantidade </th>
												<th> Preço Unitário </th>
												<th> Valor Total do Item </th>
											</tr>             
										</thead>

										<tbody>

											<g:each in="${pedido.itensVenda}" var="itemVenda">
												<g:set var="produto" value="${itemVenda.produto}"/>

												<tr>

													<td style="text-align:left !important;">
														<g:img dir="img/produtos" style="float:left;" file="${produto.fotoMiniatura}"/>
														
														<div>

															<label>${produto.nome }</label>

															<g:if test="${produto.isMultiUnidade()}">
																<p> <small> ${produto.tipoUnitario}: ${itemVenda.unidade} </small> </p>
															</g:if>

														</div>

													</td>


													<td style="position:relative;">
														<p> <span class="badge badge-warning">${itemVenda.quantidade}</span>  </p>
													</td>

													<td>
														<g:formatNumber number="${itemVenda.precoAPrazoEmReais}" type="currency" currencyCode="BRL" />
													</td>

													<td>
														<g:formatNumber number="${itemVenda.subTotalAPrazo}" type="currency" currencyCode="BRL" />
													</td>

												</tr>

											</g:each>

										</tbody>

									</table>

								</div>  
								<!-- fim well produtos do pedido -->

								

						        <div class="well" style="background-color:white;"><!-- well valores -->


									<div id="div-subtotal">
										<h4 style="display:inline;color:#666;">Subtotal</h4>

										<div style="float:right;font-weight:bold;font-size:15px;color:#666;"> 
											<g:formatNumber number="${pedido.valorItensAPrazo}" type="currency" currencyCode="BRL" />
										</div>
									</div>

									<g:if test="${!cliente.isDentroDaAreaDeEntregaRapida()}">

										<div id="div-frete" style="clear:both;">

											<h5 style="display:inline;color:blue;">Frete</h5>

											<div style="float:right;font-weight:bold;"> 
												<div style="color:blue;font-size:10px;text-align:right;">
													+ <g:formatNumber number="${pedido.freteEmReais}" type="currency" currencyCode="BRL" />
												</div>
											</div>

										</div>

									</g:if>

									<g:if test="${pedido.taxaEntregaEmReais > 0}">

										<div style="clear:both;">
											<h5 style="display:inline;color:blue;">Taxa de Entrega</h5>

											<div style="float:right;font-weight:bold;"> 
												<div style="color:blue;font-size:10px;text-align:right;">
													+ <g:formatNumber number="${pedido.taxaEntregaEmReais}" type="currency" currencyCode="BRL" />
												</div>
											</div>
										</div>

									</g:if>

									<g:if test="${pedido.descontoEmReais > 0}">

										<div id="div-desconto" style="clear:both;">
											<h5 style="display:inline;color:blue;">Desconto</h5>

											<div style="float:right;font-weight:bold;"> 
												<div style="color:blue;font-size:10px;text-align:right;">
													- <g:formatNumber number="${pedido.descontoEmReais}" type="currency" currencyCode="BRL" />
												</div>
											</div>

										</div>
									</g:if>


									<div id="div-valor-total">
										<hr>

										<h4 style="color:#666;display:inline;">Valor Total</h4>

										<div style="float:right;font-weight:bold;font-size:35px;color:#00adef;"> 
											<g:formatNumber number="${pedido.valorTotal}" type="currency" currencyCode="BRL" />
										</div>
									</div>

						        </div> <!-- fim well valores -->



							</div> <!-- fim accordion inner -->

						</div>
					</div>

				</g:each>
			</div>

		</div>

	</body>
</html>
