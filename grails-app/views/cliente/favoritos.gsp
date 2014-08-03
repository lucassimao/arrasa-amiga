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
              <a href="${createLink(controller:'cliente',action:'index',absolute:true)}" class="btn">Minha Conta</a>
              <a href="${createLink(controller:'cliente',action:'favoritos',absolute:true)}" class="btn btn-primary">Favoritos</a>
              <a href="${createLink(controller:'cliente',action:'pedidos',absolute:true)}" class="btn">Meus Pedidos</a>
              <a href="${createLink(controller:'cliente',action:'edit',absolute:true)}" class="btn">Editar Cadastro</a>
            </div>
          </nav>
        </div>

        <div class="col-md-3 hidden-xs">
          <h5> Menu </h5>
          <nav>
            <ul class="nav nav-pills nav-stacked">
              <li><a href="${createLink(controller:'cliente',action:'index')}">Minha Conta</a></li>
              <li class="active"><a href="${createLink(controller:'cliente',action:'favoritos')}">Favoritos</a></li>
              <li ><a href="${createLink(controller:'cliente',action:'pedidos')}">Meus Pedidos</a></li>
              <li> <a href="${createLink(controller:'cliente',action:'edit',absolute:true)}">Editar Cadastro</a></li>
            </ul>
          </nav>
        </div>

        <div class="col-md-9">
        	<h5> Meus Favoritos </h5>
        	<div>
        		<p> Você ainda não possui produtos favoritos </p>
        	</div>


        </div>


      </div>      	


	</body>
</html>
