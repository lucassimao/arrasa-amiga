<!DOCTYPE html>
<html>
	<head>

		<title> Arrasa Amiga: Como Comprar  </title>
		<parameter name="description" value="Dicas úteis sobre como comprar na Arrasa Amiga" />
		<parameter name="keywords" value="Maquiagem, blushes, NYX, batom, sombra, paleta,Teresina, Piauí, maquiadora" />
		<parameter name="og:image" value="${resource(dir:'img',file:'top.jpg',absolute:true)}"/>


		<meta name="layout" content="main"/>

		<style type="text/css">

			#round{
				background: url("${assetPath(src:'hr.gif')}") center repeat-x;
				height: 50px;
				margin: 20px auto;
			}
			
			.round-box{
				width: 50px;
				height: 50px;
				background: #DB5D2C;
				color: white;
				font-size: 24px;
				text-align: center;
				line-height: 50px;
				-moz-border-radius: 100%;
				border-radius: 100%;
				-moz-box-shadow: 0 2px 5px #c1c1c1;
				-webkit-box-shadow: 0 2px 5px #C1C1C1;
				box-shadow: 0 2px 5px #C1C1C1;
			}

			.right{
				float:right;
			}

			h3+p{
				text-indent: 2em;
				padding-left: 20px;
				text-align: justify;
			}

			h3{
				padding-left: 20px;
			}			

		</style>

	</head>


	<body>

		
		<div id="round">
			<div>
				<div class="round-box">	1 </div>
			</div>
		</div>

		
		<div class="row">
			<div class="col-md-6 col-sm-6 col-xs-6">
				<h3> Página Principal</h3>
				<p>
					Bem vinda amiga! Assim que você acessa o nosso site, você encontra a disposição vários produtos 
					de maquiagem à pronta entrega
				</p>
			</div>
			<div class="col-md-6 col-sm-6 col-xs-6">
				<asset:image class="img-responsive" src="how-to-buy/pagina-principal.png"/>
			</div>
		</div>

		<div id="round">
			<div class="right">
				<div class="round-box">	2 </div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-6 col-sm-6 col-xs-6">
				<asset:image class="img-responsive" src="how-to-buy/comprando.png"/>
			</div>
			<div class="col-md-6 col-sm-6 col-xs-6">
				
				<h3> Comprando </h3>
				<p>
					Amou nossos produtos? É muito fácil comprar!
					Basta clicar no botão "Comprar", logo abaixo do produto que você deseja  adquirir e 
					ele será adicionado ao seu carrinho de compras ...
				</p>

			</div>
		</div>		

		<div id="round">
			<div>
				<div class="round-box">	3 </div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-6 col-sm-6 col-xs-6">
				<h3> Selecionando </h3>
				<p>
					Alguns produtos você deve escolher a cor, tamanho ou modelo (como bases, batons e 
					corretivos por exemplo). Depois de ter escolhido, basta clicar novamente no botão "Comprar" 
					que o produto será adicionado no seu carrinho de compras.
				</p>
			</div>
			<div class="col-md-6 col-sm-6 col-xs-6">
				<asset:image class="img-responsive" src="how-to-buy/selecionando.png"/>
			</div>			
		</div>		

		<div id="round">
			<div class="right">
				<div class="round-box">	4 </div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-6 col-sm-6 col-xs-6">
				<asset:image class="img-responsive" src="how-to-buy/carrinho.png"/>
			</div>
			<div class="col-md-6 col-sm-6 col-xs-6">
				<h3>Carrinho de compras</h3>
				<p>
					Toda vez que você clicar no botão "Comprar", o produto que você escolheu será adicionado
					ao seu carrinho de compras. 
					Você pode voltar e continuar comprando outros produtos ou finalizar e efetuar o
					pagamento da sua compra.
				</p>

				<p>
					Nessa página você pode até aumentar ou diminuir a quantidade de itens que deseja levar, ou até excluí-lo 
				</p>				
			</div>
		</div>	

		<g:if test="${hasEntregaRapida}">
		
			<div id="round">
				<div>
					<div class="round-box">	5 </div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-6 col-sm-6 col-xs-6">
					<h3> Finalizando a compra</h3>
					<p>
						Selecionou todos os produtos que estava precisando? 
						Agora basta clicar no botão "Pagamento" para informar seus dados de contato
						e endereço para entrega. Nessa mesma página você deve escolher quando deseja receber 
						seus produtos e qual a forma de pagamento. 
					</p>

					<p>
						Para pagamento à vista, sempre tem desconto! Você finaliza a compra e paga quando receber seu pedido.
					</p>
					<p>
						Se você selecionar pagamento com cartão de crédito, você informará qual
						o cartão e em quantas vezes pretende parcelar sua comprinha! 
					</p>
				</div>
				<div class="col-md-6 col-sm-6 col-xs-6">
					<asset:image class="img-responsive" src="how-to-buy/finalizando.png"/>
				</div>			
			</div>		


			<div id="round">
				<div class="right">
					<div class="round-box">	6 </div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-6 col-sm-6 col-xs-6">
					<asset:image class="img-responsive" src="how-to-buy/delivery.png"/>
				</div>
				<div class="col-md-6 col-sm-6 col-xs-6">
					<h3> Entrega </h3>
					<p>
						Finalizou sua compra? Agora é só esperar ansiosamente pela entrega das
						suas comprinhas! Em Teresina as entregas são feitas nas segundas, quartas e sextas-feiras a partir das 16:00hrs.
					</p>
					<p> 
						Qualquer dúvida, basta entrar em contato pela nossa 
						<a target="_blank" href="https://www.facebook.com/arrasaamiga"> Fanpage </a>
						, e-mail <a href="mailto:arrasa.amiga@gmail.com">arrasa.amiga@gmail.com</a>
						ou pelo WhatsApp: (86) 9910-8321
					</p> 				
				</div>
			</div>	
		</g:if>
		<g:else>

			<div id="round">
				<div>
					<div class="round-box">	5 </div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-6 col-sm-6 col-xs-6">
					<h3> Finalizando a compra</h3>
					<p>
						Selecionou todos os produtos que estava precisando? 
						Agora basta clicar no botão "Pagamento" para informar seus dados de contato,
						tipo de frete, endereço para entrega e forma de pagamento.
					</p>
				</div>
				<div class="col-md-6 col-sm-6 col-xs-6">
					<asset:image class="img-responsive" src="how-to-buy/finalizando.png"/>
				</div>			
			</div>	


			<div id="round">
				<div class="right">
					<div class="round-box">	6 </div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-6 col-sm-6 col-xs-6">
					<asset:image class="img-responsive" src="how-to-buy/pagseguro.png"/>
				</div>
				<div class="col-md-6 col-sm-6 col-xs-6">
					<h3> Pagamento </h3>
					<p>
						Essa é a ultima parte da compra!! Basta preencher as informações com seu cartão de crédito
						e selecionar em quantas vezes pretende parcelar sua compra.Todo procedimento é muito seguro e tranquilo
					</p>			
				</div>
			</div>	


			<div id="round">
				<div>
					<div class="round-box">	7 </div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-6 col-sm-6 col-xs-6">
					<h3> Entrega </h3>
					<p>
						Finalizou sua compra? Agora é só esperar ansiosamente pela entrega das
						suas comprinhas! 
					</p>
					<p>
						Nós enviaremos imediatamente o seu pedido via sedex ou PAC, além docódigo de rastreamento por email
						para você saber exatamente quando seus produtos serão entregues
					</p>
					<p> 
						Qualquer dúvida, basta entrar em contato pela nossa 
						<a target="_blank" href="https://www.facebook.com/arrasaamiga"> Fanpage </a>
						ou através do e-mail <a href="mailto:arrasa.amiga@gmail.com">arrasa.amiga@gmail.com</a>
					</p> 
				</div>
				<div class="col-md-6 col-sm-6 col-xs-6">
					<asset:image class="img-responsive" src="how-to-buy/correios31.jpg"/>
				</div>			
			</div>									
		</g:else>
			
	</body>
</html>



