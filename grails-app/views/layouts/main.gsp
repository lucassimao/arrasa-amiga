<%@ page import="br.com.arrasaamiga.Cidade" %>
<%@ page import="br.com.arrasaamiga.GrupoDeProduto" %>


<!DOCTYPE html>
<html lang="en" xmlns:fb="http://www.facebook.com/2008/fbml">
  <head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="${ pageProperty(name:'page.description') }" />
    <meta name="keywords" content="${ pageProperty(name:'page.keywords') }">


    <!-- metatags facebook -->
    
    <meta property="fb:admins" content="1665191676"/>
    <meta property="fb:admins" content="100002204592399"/>
    <meta property="fb:admins" content="100002253748681"/>

    <g:if env="production">
        <meta property="fb:app_id" content="592257150816024"/> 
    </g:if>    

    <g:if env="development">
        <meta property="fb:app_id" content="538200826283779"/> 
    </g:if>
    
    <meta property="og:site_name" content="Arrasa Amiga"/>
    <meta property="og:type" content="website" /> 
    <meta property="og:url" content="${ (request.requestURL?.equals('/'))?'http://www.arrasaamiga.com.br': request.requestURL }" /> 
    <meta property="og:title" name="title" content="${ layoutTitle() }" /> 
    <meta property="og:description" content="${pageProperty(name:'page.description')}" />
    <meta property="og:image" content="${ pageProperty(name:'page.og:image') }" /> 

    <!-- fim metatags facebook -->

    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>

    <!-- Bootstrap core CSS -->
    <asset:stylesheet href="bootstrap.css"/>

    <!-- Custom styles for this template -->
    <asset:stylesheet href="style.css"/>
    <asset:stylesheet href="responsive.css"/>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <asset:javascript src="html5shiv.js"/>
      <asset:javascript src="respond.min.js"/>
    <![endif]-->

    <g:layoutHead/>
    
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

        <div class="col-md-8 col-sm-8 col-xs-8">
          <div class="logo">
            <!--
            <h1><a href="${createLink(uri:'/',absolute:true)}">Arrasa<span> amiga</span> </a></h1>
            -->
            <a href="${createLink(uri:'/',absolute:true)}">
              <asset:image  src="logo3.png" class="img-responsive"/>
            </a>
            <!-- <p>Produtos para maquiagem a pronta entrega</p> -->
          </div>
        </div>

        <div class="col-md-4 col-sm-4 col-xs-4">
          <div class="account">
            <ul>

              <sec:ifLoggedIn>

                  <li id="your-account">
                    <div class="hidden-xs">
                      <h4> Bem vinda </h4>
                      <p>
                        <a href="${createLink(controller:'cliente',action:'pedidos',absolute:true)}"> Minha conta </a>
                        |
                        <a href="${createLink(controller:'logout',absolute:true)}"> Sair </a>
                      </p>
                    </div>
                    <div class="visible-xs">
                      <a href="${createLink(controller:'cliente',action:'pedidos',absolute:true)}" class="btn btn-primary"><i class="fa fa-user"></i></a>
                    </div>
                  </li>

              </sec:ifLoggedIn>

              <sec:ifNotLoggedIn>
                      
                  <li id="your-account">
                    <div class="hidden-xs">
                      <h4><a href="${createLink(controller:'login')}"> Login </a></h4>
                      <p><a href="${createLink(controller:'cliente',action:'cadastro')}"> Cadastre-se aqui </a></p>
                    </div>
                    <div class="visible-xs">
                      <a href="${createLink(controller:'login')}" class="btn btn-primary">
                        <i class="fa fa-user"></i>                        
                      </a>
                    </div>
                  </li>

              </sec:ifNotLoggedIn>


              <li>
                <div class="hidden-xs">
                  <h4><a href="${createLink(controller:'shoppingCart')}"> Carrinho </a></h4>
                  <p><a href="${createLink(controller:'shoppingCart')}"> <strong> <cart:qtdeTotalItens/> Produto(s)</strong> </a></p>
                </div>
                <div class="visible-xs">
                  <a href="${createLink(controller:'shoppingCart')}" class="btn btn-primary">
                      <span class="cart-item"><cart:qtdeTotalItens/></span> <i class="fa fa-shopping-cart"></i>
                  </a>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div> 
      <!-- end:logo -->

      <g:if test="${!ocultarMenu}">
          <!-- begin:menu p/ mobile -->
          <nav class="navbar navbar-default visible-xs" role="navigation">
            <div class="container">
              <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-mobile">
                  <span class="sr-only">Menu</span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">Menu</a>
              </div>

              <div class="collapse navbar-collapse" id="navbar-mobile">
                <ul class="nav navbar-nav navbar-right">
                  
                  <li class="${(!grupoRaiz && controllerName == 'home' && actionName =='index')?'active':''}">
                    <a href="${createLink(uri:'/',absolute:true)}">Home</a>
                  </li>

                  <g:each in="${GrupoDeProduto.findAllByPaiIsNull()}" var="grupo">
                      <g:render template="/layouts/menuitem" model="[grupo:grupo,isRoot:true,grupoRaiz:grupoRaiz]"/>
                  </g:each>

                  <li class="divider-vertical"></li>
                  <li class="dropdown">
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#"> MAIS <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                      <li class="${(controllerName == 'home' && actionName =='comocomprar')?'active':''}" >
                        <a href="${createLink(action:'comocomprar',controller:'home',absolute:true,params: [cidade:Cidade.teresina.id])}">Como Comprar </a>
                      </li>
                      <li class="divider"></li>
                      <li class="${ (request.forwardURI?.endsWith('contato') )?'active':''}">
                        <a href="${createLink(uri:'/contato',absolute:true)}">Contato</a>
                      </li>
                    </ul>
                  </li>
                </ul>
              </div>
              
            </div>
          </nav>
          <!-- end: menu p/ mobile -->
      </g:if>


      <g:if test="${!ocultarMenu}">
          <!-- begin:nav-menus -->
          <div class="row hidden-xs">
            
            <div class="col-md-12">
              <div class="nav-menus" id="navbar-mobile">
                <ul class="nav nav-pills">
                  
                  <li class="${(!grupoRaiz && controllerName == 'home' && actionName =='index')?'active':''}"><a href="${createLink(uri:'/',absolute:true)}">Home</a></li>

                  <g:each in="${GrupoDeProduto.findAllByPaiIsNull()}" var="grupo">
                      <g:render template="/layouts/menuitem" model="[grupo:grupo,isRoot:true,grupoRaiz:grupoRaiz]"/>
                  </g:each>

                  <li class="${(controllerName == 'home' && actionName =='comocomprar')?'active':''}" >
                    <a href="${createLink(action:'comocomprar',controller:'home',absolute:true,params: [cidade:Cidade.teresina.id])}">Como Comprar </a>
                  </li>
                  
                  <li class="${ (request.forwardURI?.endsWith('contato') )?'active':''}">
                    <a href="${createLink(uri:'/contato',absolute:true)}">Contato</a>
                  </li>

                </ul>
              </div>
            </div>
          </div>
          <!-- end:nav-menus -->
      </g:if>


      <g:layoutBody/>


      <g:render template="/layouts/footer"/>

    </div>
    <!-- end:content -->


    <!-- Le javascript -->
    <!-- Placed at the end of the document so the pages load faster -->
    <g:javascript library="jquery" plugin="jquery"/>
    <asset:javascript src="bootstrap.js"/>
    <asset:javascript src="masonry.pkgd.min.js"/>
    <asset:javascript src="imagesloaded.pkgd.min.js"/>
    <asset:javascript src="script.js"/>


    <asset:script type="text/javascript">

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

                <g:if env="production">
                    appId: '592257150816024', xfbml: true 
                </g:if>    
                <g:elseif env="development">
                    appId: '538200826283779', xfbml: true
                </g:elseif>
              });     

          });


        });

    </asset:script>

    <asset:deferredScripts/>

  </body>
</html>
