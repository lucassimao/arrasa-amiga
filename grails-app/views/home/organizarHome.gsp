<%@ page import="br.com.arrasaamiga.Produto" %>

<!DOCTYPE html>
<html>
	<head>

		<title> Arrasa Amiga: Produtos para maquiagem à pronta entrega </title>
		<parameter name="description" value="Produtos para maquiagem à pronta entrega" />
		<parameter name="keywords" value="Maquiagem, blushes, NYX, batom, sombra, paleta,Teresina, Piauí, maquiadora" />
		<parameter name="og:image" value="${resource(dir:'img',file:'top.jpg',absolute:true)}"/>


		<meta name="layout" content="main"/>


		<style type="text/css">

			#p-comprar{
				text-align: center;
				position:absolute;
				bottom: 9px;
				left:0px;
				width: 100%;	
			}


			.label-preco{
				text-align: center;
				color:#ad96a5;
				font-size:20px;
				position:absolute;
				bottom: 65px;
				font-weight:bold;
				left:0px;
				width: 100%;
			}


			.item-produto{
				padding:5px;
				text-align:center;
				height:300px;
				border:1px solid pink;
				background-color: white;
				position:relative;
				border-radius: 1em;
				box-shadow: pink 0.5em 0.5em 0.3em;
				width: 200px;
				float:left;
				margin: 7px;
			}

			 #sortable { list-style-type: none; margin: 0; padding: 0; width: 100%; }


		</style>


		<asset:script>
					
			$(function(){

				$("form a").click(function(event){
					event.preventDefault();
					$(this).parent().submit();
										
				});

			    $( "#sortable" ).sortable();
			    $( "#sortable" ).disableSelection();


				$( "#sortable" ).sortable({
					  update: function( event, ui ) {

					  	var ordemArray =   $(this).sortable( "toArray" ) ;
					  	
					  	$.ajax({

					  		data: {'ordem':ordemArray},
					  		settings: {'cache':false},
					  		type:'POST',
					  		url: "${createLink(controller:'home',action:'atualizarOrdemDosItens',absolute:true)}",
					  	
					  	}).fail(function(){


						});;
					  		

					  }
				});


			});


		</asset:script>
	</head>


	<body>

			<hr>

			<g:set var="ocultarRodape" value="${true}" scope="request"/>
			<g:set var="ocultarMenu" value="${true}" scope="request"/>

			
			<a href="${createLink(controller:'produto',action:'list',absolute:true)}" class="btn btn-success">
	            <i class="icon-backward icon-white"></i> Voltar
	        </a>

      		<ul id="sortable">

	      		<g:each in="${produtos}" var="produto" status="i">


      				<li id="${produto.id}" class="ui-state-default item-produto">

							
						<g:img dir="images/produtos" file="${produto.fotoMiniatura}" alt="${produto.nome}" title="${produto.nome}"/>
						<h5> ${produto.nome}</h5>
						
						<p class="label-preco"> 
							<g:formatNumber number="${produto.precoAPrazoEmReais}" type="currency" currencyCode="BRL" />
						</p>					
								

      				</li>
						
				</g:each>

			</ul>
			
	</body>
</html>






