<%@ page import="br.com.arrasaamiga.Uf" %>
<%@ page import="br.com.arrasaamiga.Cidade" %>

<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js" xmlns:fb="http://www.facebook.com/2008/fbml"><!--<![endif]-->
	<head>


		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

		<meta property="fb:admins" content="1665191676"/>
		<meta property="fb:admins" content="100002204592399"/>
		<meta property="fb:admins" content="100002253748681"/>

		<meta property="fb:app_id" content="592257150816024"/> 
		<meta property="og:site_name" content="Arrasa Amiga"/>
		<meta property="og:type" content="website" /> 
		<meta property="og:url" content="${ (request.requestURL?.equals('/'))?'http://www.arrasaamiga.com.br': request.requestURL }" /> 
		<meta property="og:title" name="title" content="${ layoutTitle() }" /> 
		<meta property="og:description" content="${pageProperty(name:'page.description')}" />
		<meta property="og:image" content="${ pageProperty(name:'page.og:image') }" /> 



		<title><g:layoutTitle default="Arrasa Amiga"/></title>
		<link rel="shortcut icon" href="${resource(dir: 'img', file: 'favicon.ico')}" type="image/x-icon"> 
		
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'bootstrap.min.css')}"/>
		<link type="text/css" rel="stylesheet" href="${resource(dir:'css',file:'font-awesome.min.css')}">		

		<!--[if IE 7]>
		  <link type="text/css" rel="stylesheet" href="${resource(dir:'css',file:'font-awesome-ie7.min.css')}">
		<![endif]-->

		<meta name="description" content="${ pageProperty(name:'page.description') }" />
		<meta name="keywords" content="${ pageProperty(name:'page.keywords') }">


		<style type="text/css">
			html {
				overflow-y:scroll;
			}

			body {
			    padding-top: 20px;
			    padding-bottom: 60px;
			    background: url(${resource(dir:'img/backs', file:'03.gif')});
			}

			.container {
				margin: 0 auto;
				max-width: 910px;

			}

			.container > hr {
				margin: 20px 0;
			}

		</style>


		<g:javascript src="jquery-1.10.1.min.js" />
		<g:javascript src="bootstrap.min.js" />

		<g:layoutHead/>
		<r:layoutResources />

		<g:javascript>
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
			(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
			m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
			})(window,document,'script','//www.google-analytics.com/analytics.js','ga');

			ga('create', 'UA-43736713-1', 'arrasaamiga.com.br');
			ga('send', 'pageview');

			$(function(){

				$("#como-comprar").click(function(event){
					event.preventDefault();

					$('#myModal').modal();
				});

				
				$("#myModal-select-uf").change(function(){
					var idUf = $(this).val();

					$.ajax({
						
						url: "${createLink(controller:'shoppingCart',action:'getCidades',absolute:true)}",
						data: {'idUf': idUf},
						settings: {'cache':true}

					}).success(function( data, textStatus, jqXHR ) {

						$("#myModal-select-cidade").empty();

						$.each(data,function(index,objCidade){

							var nomeCidade = objCidade.nome;
							var idCidade = objCidade.id;
							
							var option = $("<option/>").text(nomeCidade).attr("value",idCidade);
							if (nomeCidade === 'Teresina'){
								$(option).attr('selected',true);
							}
							$("#myModal-select-cidade").append(option);


						});

						$("#myModal #select-cidade").change();

					
					}).fail(function(){
						alert("Erro ao carregar cidades");

					});
				});

				$("#myModal #btn-ok").click(function(event){
					event.preventDefault();

					var idCidade = $("#myModal-select-cidade").val();
					var url = "${createLink(controller:'home',action:'comocomprar',absolute:true)}?";
					url += $.param({cidade: idCidade});

					window.location =  url  ;

				});

				$("#myModal-select-uf").change();




			});

		</g:javascript>

	</head>
	<body>

		<div id="fb-root" style="display:none;"></div>
		<script>

			$(document).ready(function() {
			  
			  $.ajaxSetup({ cache: true });

			  $.getScript('//connect.facebook.net/pt_BR/all.js#xfbml=1', function(){
				    FB.init({
				      appId: '592257150816024', xfbml: true  
				    });     

					FB.Event.subscribe('auth.login', function(response) {
						alert(response);
					});


			  });
			});

		</script> 


		
		<div id="myModal" class="modal hide fade">
		  <div class="modal-header">
		    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		    <h3> Qual sua cidade? </h3>
		  </div>
		  <div class="modal-body">
		          <div style="clear:both;float:left;margin-right:10px;">
		              <label style="font-weight:bold;"> Estado: </label>

		              <g:select class="input-medium" value="${ Uf.get(17).id }" id="myModal-select-uf" name="uf"
		              		optionValue="nome" optionKey="id" from="${Uf.list()}" />

		          </div>

		          <div style="float:left;margin-right:10px;">
		              <label style="font-weight:bold;"> Cidade: </label>

		              <g:select name="cidade" class="input-large" value="${}" id="myModal-select-cidade" from="${}" />

		          </div>
		  </div>
		  <div class="modal-footer">
		     <button class="btn" data-dismiss="modal" aria-hidden="true">Cancelar</button>
		     <a href="#" id="btn-ok" class="btn btn-primary"> OK </a>
		  </div>
		</div>


		

		<div class="container" >
			
			<div  class="navbar navbar-inverse navbar-fixed-top">
				<div class="navbar-inner">
					<div class="container">


						<div class="nav-collapse collapse">
							<ul class="nav">

								<li class="${(controllerName == 'home' && actionName =='index')?'active':''}">
									<a href="${createLinkTo(uri:'/',absolute:true)}">  
										<i class=" icon-home  icon-large"></i> Home
									</a>
								</li>

								
								<li id="como-comprar" class="${(controllerName == 'home' && actionName =='comocomprar')?'active':''}">
									<g:link controller="home" action="comocomprar">
										<i class="icon-question-sign  icon-large"></i> Como Comprar
									</g:link>
								</li>

								

								<li>
									<a href="https://www.facebook.com/arrasaamiga" target="_blank">
										<i class="icon-facebook  icon-large"></i>	Facebook 
									</a>
								</li>

								

								
								<li class="${(controllerName == 'shoppingCart' && actionName=='index')?'active':''}">
									<g:link controller="shoppingCart">
										<i class="icon-shopping-cart  icon-large"></i>	
										Carrinho  ( <cart:qtdeTotalItens/>  )  
									</g:link>
								</li>

							</ul>
							<ul class="nav pull-right">

								<li class="dropdown">
			                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
			                        	<sec:ifLoggedIn>
			                        		<g:set var="style" value="color:#F29BF2;"/>
			                        	</sec:ifLoggedIn>

			                        	<sec:ifNotLoggedIn>
			                        		<g:set var="style" value=""/>
			                        	</sec:ifNotLoggedIn>

			                        	<i style="${style}" class="icon-female icon-large"></i> 
			                        	<sec:ifNotLoggedIn>
			                        		Entrar
			                        	</sec:ifNotLoggedIn>
			                        	<sec:ifLoggedIn>
			                        		Você
			                        	</sec:ifLoggedIn>
			                        	<b class="caret"></b>
			                       	</a>

			                        <ul class="dropdown-menu">
			                        	<sec:ifLoggedIn>
											<li><a href="#"> Meus Pedidos</a></li>
											<li><a href="#"> Meus Produtos Favoritos </a></li>
											<li class="divider"></li>
											
											<sec:ifAllGranted roles="ROLE_ADMIN">

												<li> 
													<a href="${createLink(controller:'produto')}"> ADMINISTRATIVO </a> 
												</li>

												<li class="divider"></li>

											</sec:ifAllGranted>

											<li><a href="${createLink(controller:'logout')}"> Sair</a></li>
			                        	</sec:ifLoggedIn>
			                        	
			                        	<sec:ifNotLoggedIn>
											<li><a href="${createLink(controller:'login')}"> Entrar </a></li>
											<!--
											<li><a href="${createLink(controller:'cliente',action:'cadastro')}"> Quero me cadastrar ! </a></li>
											<li><a href="#"> Esqueci minha senha! </a></li>
											-->
			                        	</sec:ifNotLoggedIn>

			                        </ul>
		                      </li>

							</ul>

						</div>
					</div>
				</div>
			</div>


			<g:img style="margin-top:30px;" dir="img" file="top.jpg" alt="Arrasa Amiga" title="Espaço com os melhores produtos para maquiagem à pronta entrega"/>

			<g:layoutBody/>

			

			<g:if test="${!ocultarRodape}">

				<hr>
				
				<div class="footer">


					<img style="float:left;" width="268px" src="https://p.simg.uol.com.br/out/pagseguro/i/banners/parcelamento/468x60_pagseguro_5x.gif"> </img>


					<div class="fb-like" style="float:right;" data-href="https://www.facebook.com/arrasaamiga" 
						data-send="true" data-width="450" data-show-faces="true" data-font="tahoma"></div>

					
					<div class="fb-comments"  data-href="${ (request.requestURL?.equals('/'))?'http://www.arrasaamiga.com.br': request.requestURL }" data-width="910" style=""></div>
					
				</div>

			</g:if>

    	</div> 


		<r:layoutResources />
	</body>
</html>
