<%@ page import="br.com.arrasaamiga.Produto" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>

		<style type="text/css">
			table th, table td {
				text-align: center !important;
				vertical-align: middle !important;
			}
		</style>

	</head>


	<body>


			<hr>


			<div class="well" style="background-color:#F29BF2;color:white;border:1px solid white;">
				<h2>  Finalização da compra </h2>
				<h5>  Visualize os detalhes do seu pedido, confirme seu 
				endereço de entrega e escolha uma forma de pagamento  </h5>
			</div>

			<hr>

			<g:set var="ocultarRodape" value="${true}" scope="request"/>

			<g:if test="${flash.message}">
				<div class="alert alert-info">
				   <button type="button" class="close" data-dismiss="alert">&times;</button>
				   <i class=" icon-info-sign"></i>
				   ${flash.message} 
				</div>
			</g:if>

			<div class="row-flow" style="margin:20px 0px;"> 

				<table class="table table-bordered" style="background-color:white;">
					<thead >
						<tr>
							<th> Descrição </th>
							<th> Quantidade </th>
							<th> Preço Unitário </th>
							<th> Valor Total do Item </th>
						</tr>							
					</thead>
					<tbody>
						<sc:each>
							<g:set var="produto" value="${com.metasieve.shoppingcart.Shoppable.findByShoppingItem(it['item'])}"/>
							
							<tr>
								<td style="text-align:left !important;">
									<g:img dir="img/produtos" file="${produto.fotoMiniatura}"/>
									<a href="#" style="text-decoration:none;">${produto.nome}</a>
								</td>

								<td style="position:relative;">

									<p> <span class="badge badge-warning">${it['qty']}</span>  </p>

								</td>
								
								<td>
									R$ ${produto.precoAVistaEmReais}
								</td>
								
								<td>
									R$ ${it['qty'] * produto.precoAVistaEmReais}
								</td>

							</tr>

						</sc:each>
									
					</tbody>
				</table>

			</div>	

			<div class="well" style="background-color:white;">

				<g:if test="${!enderecoEntrega.fromTeresina}">

					<h4 style="display:inline;">Frete</h4>
					<span style="float:right;font-weight:bold;"> R$ ${frete} </span>

					<hr>

				</g:if>

				<h4 style="display:inline;">Valor Total da Compra</h4>
				<span style="float:right;font-weight:bold;"> R$ ${valorTotal} </span>

			</div>

			<div class="well" style="background-color:white;">

				<legend> <i class="icon-truck"></i> Entrega </legend>

				<div class="row-fluid">
					<div class="span5">
						<label> CEP: ${enderecoEntrega.cep} </label>

						<label> Estado: ${enderecoEntrega.uf}</label>

						<label> Cidade: ${enderecoEntrega.cidade}</label>

						<label> Bairro: ${enderecoEntrega.bairro} </label>

						<label> Endereço: ${enderecoEntrega.endereco}</label>

						<label> Complemento: ${enderecoEntrega.complemento}</label>

						<label> Ponto de Referência: ${enderecoEntrega.pontoDeReferencia}</label>
					</div>
					
					<g:if test="${enderecoEntrega.fromTeresina}">

						<div class="span6">
							<p style="font-family:Arial;text-align:justify;text-indent:20px;"> 						
							
							Identificamos que você é de Teresina - Piauí 
							<g:img dir="img" file="bandeira-piaui.png" /> ! 
							Informe a melhor, data, horário e lugar
							para entregarmos seu pedido. Caso você deseje, pode indicar um endereço
							alternativo (como o seu endereço de trabalho) :</p>

							<textarea placeholder="Onde e quando podemos entregar seu pedido?" rows="4"
									 style="min-width:400px;"></textarea>
						</div>

					</g:if>

				</div>

			</div>

			<div class="well" style="background-color:white;">

				<legend> <i class="icon-credit-card"></i> Forma de Pagamento </legend>
				
				<div class="row-fluid">

					<g:if test="${enderecoEntrega.fromTeresina}">

						<div class="span6">
							<label class="radio">
								<input  name="radio_forma_pagamento" type="radio" checked>
								Pagamento em dinheiro
							</label>
							
							<em> 
								Você pagará no momento da entrega da sua encomenda 
							</em>
						</div>

					</g:if>

					<div class="span6">
						<label class="radio">
							<input name="radio_forma_pagamento" 
								type="radio" ${(!enderecoEntrega.fromTeresina)?'checked':''} > 
							Pagamento com cartão de crédito
							
						</label>
						<em style="text-indent:20px;"> 
							Você informará na próxima página os dados do seu cartão de crédito
						</em>
					</div>

				</div>

			</div>

			<hr>

			<div  class="row-fluid">
	            <div>
	                <a href="${createLinkTo(url:'/')}" class="btn btn-success">
	                    <i class="icon-shopping-cart icon-white"></i>
	                    Voltar para o carrinho de compras
	                </a>	                
	                <a href="javascript: void(0);" style="float:right;" class="btn btn-success">
	                   <i class="icon-ok-circle icon-white"></i>
	                   Confirmar Pedido
	                </a>
	            </div>
	        </div>

	</body>
</html>



