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
                    <a href="${createLink(controller:'cliente',action:'index',absolute:true)}" class="btn btn-primary">Minha Conta</a>
                    <a href="${createLink(controller:'cliente',action:'favoritos',absolute:true)}" class="btn">Favoritos</a>
                    <a href="${createLink(controller:'cliente',action:'pedidos',absolute:true)}" class="btn">Meus Pedidos</a>
                    <a href="${createLink(controller:'cliente',action:'atualizarCadastro',absolute:true)}" class="btn">Editar Dados</a>
                </div>
            </nav>
        </div>

        <div class="col-md-3 hidden-xs">
            <h5> Menu </h5>
            <nav>
                <ul class="nav nav-pills nav-stacked">
                    <li class="active"><a href="${createLink(controller:'cliente',action:'index')}">Minha Conta</a></li>
                    <li><a href="${createLink(controller:'cliente',action:'favoritos')}">Favoritos</a></li>
                    <li><a href="${createLink(controller:'cliente',action:'pedidos')}">Meus Pedidos</a></li>
                    <li><a href="${createLink(controller:'cliente',action:'atualizarCadastro')}">Editar Dados</a></li>
                </ul>
            </nav>
        </div>

        <div class="col-md-9">
        	<h5> Minha Conta </h5>
        	<div>
        		<address>
        			<strong> ${cliente.nome}</strong>
        			<br>${cliente.email}
        			<br>${cliente.endereco.bairro}, ${cliente.endereco.complemento}
        			<br>${cliente.endereco.cidade.nome}, ${cliente.endereco.uf.nome}
        			<br> (${cliente.dddCelular}) ${cliente.celular} / (${cliente.dddTelefone}) ${cliente.telefone}
        		</address>

        	</div>


        	<div>
        		<h5> Meus Pedidos Recentes </h5>
        		<table class="table table-striped">
        			<thead>
        				<tr>
        					<th>#</th>
        					<th>Data</th>
        					<th>Forma de Pagamento</th>
        					<th>Status</th>
        					<th>Valor Total</th>
        				</tr>
        			</thead>
        			<tbody>
        				<g:each var="pedido" in="${pedidos}">
	        				<tr>
	        					<td>${pedido.id}</td>
	        					<td>${pedido.dateCreated.format('dd/MM/yyyy')}</td>
	        					<td>${pedido.formaPagamento}</td>
	        					<td>${pedido.status}</td>
	        					<td><g:formatNumber number="${pedido.valorTotal}" type="currency" currencyCode="BRL" /></td>
	        				</tr>
        				</g:each>
        			</tbody>
        		</table>
        	</div>
        </div>


      </div>      	


	</body>
</html>
