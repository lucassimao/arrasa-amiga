<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>Arrasa Amiga</title>
		<!-- <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"> -->
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}" type="text/css">

		
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'bootstrap.min.css')}"/>
		<link type="text/css" rel="stylesheet" href="${resource(dir:'css',file:'font-awesome.min.css')}">

		<script type="text/javascript" src="${resource(dir:'js',file:'jquery-1.10.1.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir:'js',file:'bootstrap.min.js')}"></script>
		

		<!--[if IE 7]>
		  <link type="text/css" rel="stylesheet" href="${resource(dir:'css',file:'font-awesome-ie7.min.css')}">
		<![endif]-->

		<style type="text/css">
		  body {
		        padding-top: 20px;
		        padding-bottom: 60px;
		        background: url(${resource(dir:'img/backs', file:'03.gif')});
		  }

		  .container {
		    margin: 0 auto;
		    max-width: 800px;
		    text-align: center;
		  }

		  .container > hr {
		    margin: 50px 0;
		  }

		</style>

		<g:layoutHead/>
		<r:layoutResources />
	</head>
	<body>
		
		<div id="fb-root"></div>
		<script>(function(d, s, id) {
		  var js, fjs = d.getElementsByTagName(s)[0];
		  if (d.getElementById(id)) return;
		  js = d.createElement(s); js.id = id;
		  js.src = "//connect.facebook.net/pt_BR/all.js#xfbml=1";
		  fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));</script> 


		<div class="container">

			
			<div class="row">
				<div class="navbar">
					<div class="navbar-inner" >
						<ul class="nav">
							<li>
								<a href="${createLinkTo(url:'/')}">  
									<i class=" icon-home  icon-large"></i> Home 
								</a>
							</li>
							<li class="divider-vertical"></li>
							<li>
								<a href="#">
									<i class=" icon-eye-open  icon-large"></i>	Precisa de uma maquiadora ? 
								</a>
							</li>
							<li class="divider-vertical"></li>
							<li>
								<a href="#"> 
									<i class="icon-shopping-cart  icon-large"></i>	Carrinho de compras (0)  
								</a>
							</li>
							<li class="divider-vertical"></li>
							<li>
								<a href="#" >
									<i class="icon-credit-card  icon-large"></i> Finalizar compra
								</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
			

			<div class="row clear-fix">
				<g:img dir="img" file="top.jpg"/>
			</div>


      		


			<g:layoutBody/>



			<hr>

			<div class="footer">
				<div class="fb-like" style="float:left;" data-href="https://www.facebook.com/arrasaamiga" 
					data-send="true" data-width="450" data-show-faces="true" data-font="tahoma"></div>

				<img style="float:right;" width="268px" src="https://p.simg.uol.com.br/out/pagseguro/i/banners/parcelamento/468x60_pagseguro_6x.gif"> 


				<div id="disqus_thread" style="clear:both;"></div>

			    <script type="text/javascript">
			        
			        var disqus_shortname = 'arrasaamiga';

			        (function() {
			            var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
			            dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
			            (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
			        })();
			    </script>
				<noscript>Por favor, habilite o JavaScript para ver os <a href="http://disqus.com/?ref_noscript">comentários </a></noscript>
				<a href="http://disqus.com" class="dsq-brlink">comments powered by <span class="logo-disqus">Disqus</span></a>
			</div>

    	</div> 


		<g:javascript library="application"/>
		<r:layoutResources />
	</body>
</html>
