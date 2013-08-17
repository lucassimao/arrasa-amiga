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
			
			<g:set var="ocultarRodape" value="${true}" scope="request"/>

			<g:if test="${flash.message}">
				<div class="alert alert-info">
				   <button type="button" class="close" data-dismiss="alert">&times;</button>
				   ${flash.message}
				</div>
			</g:if>

			<div  class="row-fluid">
	            <div>
	                <a href="${createLinkTo(url:'/')}" class="btn btn-success">
	                    <i class="icon-backward icon-white"></i>
	                    Comprar outros produtos
	                </a>	                
	                <a href="${createLink(controller:'shoppingCart',action:'checkout')}" style="float:right;" class="btn btn-success">
	                   <i class="icon-ok-circle icon-white"></i>
	                   Finalizar Compra
	                </a>
	            </div>
	        </div>

			<div class="row-flow" style="margin:20px 0px;"> 

				<table class="table table-bordered" style="background-color:white;">
					<thead >
						<tr>
							<th> Descrição </th>
							<th> Quantidade </th>
							<th> Preço Unitário </th>
							<th> Valor Total do Item </th>
							<th> Excluir </th>
						</tr>							
					</thead>
					<tbody>
						<sc:each>
							<g:set var="produto" value="${com.metasieve.shoppingcart.Shoppable.findByShoppingItem(it['item'])}"/>
							
							<tr>
								<td style="text-align:left !important;">
									<g:img dir="img/produtos" file="${produto.fotoMiniatura}"/>
									<a href="${createLink(controller:'produto',action:'show',id:produto.id)}">${produto.nome }</a>
								</td>

								<td style="position:relative;">

									<p style="position:absolute;top:25%;left:40%;"> <span class="badge badge-warning">${it['qty']}</span>  </p>

					                <a href="${createLink(controller:'shoppingCart',action:'add1',id:produto.id)}" class="btn btn-info">
					                    <i class="icon-plus-sign icon-white"></i>
					                </a>
					           		<a href="${createLink(controller:'shoppingCart',action:'remover1',id:produto.id)}" class="btn btn-info">
					                    <i class="icon-minus-sign icon-white"></i>
					                </a>
								</td>
								
								<td>
									R$ ${produto.precoAVistaEmReais}
								</td>
								
								<td>
									R$ ${it['qty'] * produto.precoAVistaEmReais}
								</td>

								<td>
					                <a href="${createLink(controller:'shoppingCart',action:'removerProduto',id:produto.id)}" class="btn btn-danger">
					                    <i class="icon-trash icon-white"></i>
					                </a>
								</td>
							</tr>

						</sc:each>
									
					</tbody>
				</table>

			</div>	

			<hr>

			

			<div class="well" style="background-color:white;">
			  <h4 style="display:inline;">Valor Total da Compra</h4>
			  <span style="float:right;font-weight:bold;"> R$ ${valorTotal} </span>
			</div>

			<hr>

			<div  class="row-fluid">
	            <div>
	                <a href="${createLinkTo(url:'/')}" class="btn btn-success">
	                    <i class="icon-backward icon-white"></i>
	                    Comprar outros produtos
	                </a>	                
	                <a href="${createLink(controller:'shoppingCart',action:'checkout')}" style="float:right;" class="btn btn-success">
	                   <i class="icon-ok-circle icon-white"></i>
	                   Finalizar Compra
	                </a>
	            </div>
	        </div>

	</body>
</html>



