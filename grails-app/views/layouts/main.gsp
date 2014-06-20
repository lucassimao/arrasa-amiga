<%@ page import="br.com.arrasaamiga.Cidade" %>


<!DOCTYPE html>
<html lang="en" xmlns:fb="http://www.facebook.com/2008/fbml">
  <head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="${ pageProperty(name:'page.description') }" />
    <meta name="keywords" content="${ pageProperty(name:'page.keywords') }">

    <!-- metatags facebook -->
    
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

    <!-- fim metatags facebook -->

    <link rel="shortcut icon" href="${resource(dir: 'img', file: 'favicon.ico')}">


    <!-- Bootstrap core CSS -->
    <link href="${resource(dir:'css',file:'bootstrap.css') }" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="${resource(dir:'css',file:'style.css') }" rel="stylesheet">
    <link href="${resource(dir:'css',file:'responsive.css') }" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="${resource(dir:'js',file:'html5shiv.js') }"></script>
      <script src="${resource(dir:'js',file:'respond.min.js') }"></script>
    <![endif]-->


    <g:layoutHead/>
    <r:layoutResources />
    
    <title><g:layoutTitle default="Arrasa Amiga"/></title>

  </head>

  <body>

    <div id="fb-root" style="display:none;"></div>

    <!-- se tiver perfil administrativo renderiza a barra -->
    <sec:ifAllGranted roles="ROLE_ADMIN">
        <g:render template="/layouts/navbar"/>
    </sec:ifAllGranted>


    <!-- begin:content -->
    <div class="container">

      <!-- begin:logo -->
      <div class="row">
        <div class="col-md-6 col-sm-6 col-xs-6">
          <div class="logo">
            <h1><a href="${createLink(uri:'/',absolute:true)}">Arrasa<span> amiga</span> </a></h1>
            <p>Produtos para maquiagem a pronta entrega</p>
          </div>
        </div>
        <div class="col-md-6 col-sm-6 col-xs-6">
          <div class="account">
            <ul>

              <sec:ifLoggedIn>

                  <li id="your-account">
                    <div class="hidden-xs">
                      <h4> Bem vinda </h4>
                      <p><a href="${createLink(controller:'cliente',action:'pedidos',absolute:true)}"> Minha conta </a></p>
                    </div>
                    <div class="visible-xs">
                      <a href="login.html" class="btn btn-primary"><i class="fa fa-user"></i></a>
                    </div>
                  </li>

              </sec:ifLoggedIn>

              <sec:ifNotLoggedIn>
                      
                  <li id="your-account">
                    <div class="hidden-xs">
                      <h4><a href="${createLink(controller:'login')}"> Login </a></h4>
                      <p> <a href="${createLink(controller:'cliente',action:'cadastro')}"> Cadastre-se aqui </a></p>
                    </div>
                    <div class="visible-xs">
                      <a href="login.html" class="btn btn-primary"><i class="fa fa-user"></i></a>
                    </div>
                  </li>

              </sec:ifNotLoggedIn>


              <li>
                <div class="hidden-xs">
                  <h4><a href="cart.html"> Carrinho </a></h4>
                  <p><strong> <cart:qtdeTotalItens/> Produto(s)</strong></p>
                </div>
                <div class="visible-xs">
                  <a href="cart.html" class="btn btn-primary"><span class="cart-item">3</span> <i class="fa fa-shopping-cart"></i></a>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div> 
      <!-- end:logo -->

      <!-- begin:nav-menus -->
      <div class="row">
        <div class="col-md-12">
          <div class="nav-menus">
            <ul class="nav nav-pills">
              <li class="${(controllerName == 'home' && actionName =='index')?'active':''}"><a href="${createLink(uri:'/',absolute:true)}">Home</a></li>
              <li><a href="#">Acessories</a></li>
              <li class="dropdown">
                <a href="#" data-toggle="dropdown" class="dropdown-toggle">Boy <b class="caret"></b></a>
                <ul class="dropdown-menu" id="menu1">
                  <li>
                    <a href="#">Shirts <b class="caret"></b></a>
                    <ul class="dropdown-menu sub-menu">
                      <li><a href="#">Shirts</a></li>
                      <li><a href="#">T-shirts</a></li>
                      <li><a href="#">Polo Shirts</a></li>
                      <li><a href="#">Tanktop</a></li>
                    </ul>
                  </li>
                  <li><a href="#">Jacket</a></li>
                  <li><a href="categories.html">Pants</a></li>
                  <li><a href="#">Boxer</a></li>
                  <li class="divider"></li>
                  <li><a href="#">SweatShirts</a></li>
                </ul>
              </li>
              <li class="dropdown">
                <a href="#" data-toggle="dropdown" class="dropdown-toggle">Girl <b class="caret"></b></a>
                <ul class="dropdown-menu" id="menu1">
                  <li><a href="#">Shirts</a></li>
                  <li><a href="#">Pants</a></li>
                  <li><a href="#">Skirts</a></li>
                </ul>
              </li>
              <li><a href="#">Edition </a></li>
              <li><a href="#">Authorized Dealer</a></li>
              <li class="${(controllerName == 'home' && actionName =='comocomprar')?'active':''}" ><a href="${createLink(action:'comocomprar',controller:'home',absolute:true,params: [cidade:Cidade.teresina.id])}">Como Comprar </a></li>
              <li class="${ (request.forwardURI?.endsWith('contato') )?'active':''}"><a href="${createLink(uri:'/contato',absolute:true)}">Contato</a></li>
            </ul>
          </div>
        </div>
      </div>
      <!-- end:nav-menus -->

      <!-- begin:home-slider -->
      <div id="home-slider" class="carousel slide" data-ride="carousel">
        <ol class="carousel-indicators">
          <li data-target="#home-slider" data-slide-to="0" class="active"></li>
          <li data-target="#home-slider" data-slide-to="1" class=""></li>
          <li data-target="#home-slider" data-slide-to="2" class=""></li>
        </ol>
        <div class="carousel-inner">
          <div class="item active">
            <img src="${resource(dir:'img',file:'layout-WB0BMF1K5/slider1.png')}" alt="clotheshop">
            <div class="carousel-caption hidden-xs">
              <h3>First slide label</h3>
              <p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
            </div>
          </div>
          <div class="item">
            <img src="${resource(dir:'img',file:'layout-WB0BMF1K5/slider2.png')}" alt="clotheshop">
            <div class="carousel-caption hidden-xs">
              <h3>Second slide label</h3>
              <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
            </div>
          </div>
          <div class="item">
            <img src="${resource(dir:'img',file:'layout-WB0BMF1K5/slider3.png')}" alt="clotheshop">
            <div class="carousel-caption hidden-xs">
              <h3>Third slide label</h3>
              <p>Praesent commodo cursus magna, vel scelerisque nisl consectetur.</p>
            </div>
          </div>
        </div>
        <a class="left carousel-control" href="#home-slider" data-slide="prev">
          <i class="fa fa-angle-left"></i>
        </a>
        <a class="right carousel-control" href="#home-slider" data-slide="next">
          <i class="fa fa-angle-right"></i>
        </a>
      </div>
      <!-- end:home-slider -->

      <g:layoutBody/>


      <g:render template="/layouts/footer"/>

    </div>
    <!-- end:content -->


    <!-- Le javascript -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="${resource(dir:'js',file:'jquery.js')}"></script>
    <script src="${resource(dir:'js',file:'bootstrap.min.js')}"></script>
    <script src="${resource(dir:'js',file:'masonry.pkgd.min.js')}"></script>
    <script src="${resource(dir:'js',file:'imagesloaded.pkgd.min.js')}"></script>
    <script src="${resource(dir:'js',file:'script.js')}"></script>

    <r:script>

        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
        (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
        m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

        ga('create', 'UA-43736713-1', 'arrasaamiga.com.br');
        ga('send', 'pageview');

        $(document).ready(function() {
          
          $.ajaxSetup({ cache: true });

          $.getScript('//connect.facebook.net/pt_BR/all.js#xfbml=1', function(){
              FB.init({
                appId: '592257150816024', xfbml: true  
              });     

          });


        });

    </r:script>



    <r:layoutResources />

  </body>
</html>
