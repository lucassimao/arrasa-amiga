<%@ page import="br.com.arrasaamiga.Produto" %>
<%@ page import="br.com.arrasaamiga.FormaPagamento" %>


<html>
<head>
	<meta name='layout' content='main'/>
	<style type='text/css' media='screen'>
		h5{
			border-bottom: 1px solid black;
			font-size: 14px;
			line-height: 22px;
			color: #444;
			font-weight: 600;
		}

		.accordion-heading{
			background-color: #C6E0F7;
			margin-bottom:4px;
			padding: 5px;
			border:1px solid #ccc;
			-webkit-border-radius: 4px;
			-moz-border-radius: 4px;
			-o-border-radius: 4px;
			border-radius: 4px;
		}
	</style>
</head>

	<body>

		<g:set var="ocultarMenu" value="${true}"  scope="request"/>
		<g:set var="ocultarRodape" value="${true}" scope="request"/>

      <div class="row">
        <!-- breadcrumb -->
        <div class="col-md-12">
            <ol class="breadcrumb" style="background-color:white;border:1px solid #ccc;">
              <li><a href="${createLink(absolute:true,uri: '/' )}"> <i class="fa fa-home"></i> Home</a></li>
          	  <li class="active">
          	  	<a href="#"> Minha Conta </a>
          	  </li>
            </ol>
        </div>
        <!-- end breadcumb -->
      </div>	

      <div class="row">
        
        <div class="col-md-3 visible-xs">
        	<h5> Menu </h5>
        	<nav>
				<div class="btn-group">
					<a href="${createLink(controller:'cliente',action:'index',absolute:true)}" class="btn">Minha Conta</a>
					<a href="${createLink(controller:'cliente',action:'favoritos',absolute:true)}" class="btn">Favoritos</a>
					<a href="${createLink(controller:'cliente',action:'pedidos',absolute:true)}" class="btn btn-primary">Meus Pedidos</a>
					<a href="${createLink(controller:'cliente',action:'pedidos',absolute:true)}" class="btn">Editar Dados</a>
				</div>
        	</nav>
        </div>

        <div class="col-md-3 hidden-xs">
        	<h5> Menu </h5>
        	<nav>
	        	<ul class="nav nav-pills nav-stacked">
	        		<li><a href="${createLink(controller:'cliente',action:'index')}">Minha Conta</a></li>
	        		<li><a href="${createLink(controller:'cliente',action:'favoritos')}">Favoritos</a></li>
	        		<li class="active"><a href="${createLink(controller:'cliente',action:'pedidos')}">Meus Pedidos</a></li>
	        		<li><a href="#">Editar Dados</a></li>
	        	</ul>
        	</nav>
        </div>

        <div class="col-md-9">
        	<h5> Meus Pedidos </h5>

        	<div>
				<g:each in="${pedidos}" var="pedido" status="idx">

					<g:set var="cliente" value="${pedido.cliente}"/>

					<div class="accordion-group">

						<div class="accordion-heading">
							<a class="accordion-toggle" style="font-weight:bold;font-size:16px;" 
									data-toggle="collapse" data-parent="#accordion" href="#collapse${idx}">
								Pedido #${ String.format("%05d",pedido.id) }
							</a>
						</div>

						<div id="collapse${idx}" class="accordion-body collapse">
							
							<div class="accordion-inner">

								<!-- well time line -->
								<div class="well hidden-xs" style="text-align:center;background-color:white;color:rgb(102, 102, 102);">

									<g:if test="${pedido.formaPagamento.equals(FormaPagamento.AVista)}">
										<g:img style="margin-top:45px;" dir="img" file="timeline-02.png"/>
									</g:if>
									<g:else>
										<g:img style="margin-top:45px;" dir="img" file="timeline-01.png"/>
									</g:else>
								</div>
								<!-- fim well time line -->

								<div class="well visible-xs"  style="background-color:white;">
									<legend> <i class="fa fa-info"></i> Status </legend>
									<div>
										${pedido.status}
									</div>
								</div>								


								<!-- well dados de entrega -->
								<div class="well" style="background-color:white;">

									<legend> <i class="fa fa-truck"></i> Entrega </legend>

									<div class="row">

										<div class="col-md-12"> 
											<label> Endereço: </label> 
						        			${cliente.endereco.bairro}, ${cliente.endereco.complemento} - ${cliente.endereco.cidade.nome}, ${cliente.endereco.uf.nome}
										</div>
										<g:if test="${cliente.isDentroDaAreaDeEntregaRapida()}">
											<div class="col-md-12"> 
												<label> Data da Entrega: </label> 
												<g:formatDate format="EEEE, dd/MM/yyyy" date="${pedido.dataEntrega}"/>
											</div>
										</g:if>	

										<g:if test="${!cliente.isDentroDaAreaDeEntregaRapida()}">
											<div class="col-md-6">
												<label> CEP: </label> 
												${cliente.endereco.cep} 
											</div>											
											<div class="col-md-6">
												<div> 
													<label> Rastreio: </label> 
													<a href="${pedido.urlRastreioCorreios}" target="_blank">
														${pedido.codigoRastreio}
													</a> 
												</div>
											</div>	
										</g:if>	

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
														<asset:image class="hidden-xs" style="float:left;" src="produtos/${produto.fotoMiniatura}"/>
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

									<g:if test="${cliente.isDentroDaAreaDeEntregaRapida()}">

										<div style="clear:both;">
											<h5 style="display:inline;color:blue;">Taxa de Entrega</h5>

											<div style="float:right;font-weight:bold;"> 
												<div style="color:blue;font-size:10px;text-align:right;">
													+ <g:formatNumber number="${pedido.freteEmReais}" type="currency" currencyCode="BRL" />
												</div>
											</div>
										</div>

									</g:if>
									<g:else>

										<div id="div-frete" style="clear:both;">
											<h5 style="display:inline;color:blue;">Frete</h5>
											<div style="float:right;font-weight:bold;"> 
												<div style="color:blue;font-size:10px;text-align:right;">
													+ <g:formatNumber number="${pedido.freteEmReais}" type="currency" currencyCode="BRL" />
												</div>
											</div>
										</div>

									</g:else>

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

								<div class="well" style="background-color:white;">

									<legend> <i class="fa fa-money"></i> Forma de Pagamento </legend>
									
									<div class="row-fluid">

										<div class="span8">
										  ${pedido.detalhesPagamento}
										</div>

									</div>

								</div>



							</div> <!-- fim accordion inner -->

						</div>
					</div>

				</g:each>
        	</div>


        </div>


      </div>      	


	</body>
</html>