<%@ page import="br.com.arrasaamiga.Cidade" %>
<%@ page import="br.com.arrasaamiga.GrupoDeProduto" %>

<!DOCTYPE html>
<html lang="en" xmlns:fb="http://www.facebook.com/2008/fbml">
<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="${pageProperty(name: 'page.description')}"/>
    <meta name="keywords" content="${pageProperty(name: 'page.keywords')}">


    <!-- metatags facebook -->

    <meta property="fb:admins" content="1665191676"/>
    <meta property="fb:admins" content="100002204592399"/>
    <meta property="fb:admins" content="100002253748681"/>
    <!--<meta property="fb:app_id" content="592257150816024"/> -->

    <meta property="og:site_name" content="Arrasa Amiga"/>
    <meta property="og:type" content="website"/>
    <meta property="og:url"  content="${(request.requestURL?.equals('/')) ? 'http://www.arrasaamiga.com.br' : request.requestURL}"/>
    <meta property="og:title" name="title" content="${layoutTitle()}"/>
    <meta property="og:description" content="${pageProperty(name: 'page.description')}"/>
    <meta property="og:image" content="${pageProperty(name: 'page.og:image')}"/>

    <!-- fim metatags facebook -->

    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>

    <!-- Custom styles and dependencies  for this template -->
    <asset:stylesheet href="style.css"/>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
        <asset:javascript src="ie8support.js"/>
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
<div class="container" style="${sec.ifAllGranted(roles: 'ROLE_ADMIN') { 'padding-top: 65px;' }}">

    <!-- begin:logo -->
    <div class="row" style="max-height: 150px;overflow: hidden; ">

        <div class="col-md-7 col-sm-7 col-xs-6">
            <div class="logo">
                <a href="${createLink(uri: '/')}">
                    <asset:image src="logo3.png" class="img-responsive"/>
                </a>
            </div>
        </div>

        <div class="col-md-5 col-sm-5 col-xs-6">
            <div class="account">
                <ul>

                    <sec:ifLoggedIn>

                        <li id="your-account">
                            <div class="hidden-xs">
                                <h4>Bem vinda</h4>

                                <p>
                                    <a href="${createLink(controller: 'cliente', action: 'index')}">Minha conta</a>
                                    |
                                    <a href="${createLink(controller: 'logout')}">Sair</a>
                                </p>
                            </div>

                            <div class="visible-xs">
                                <a href="${createLink(controller: 'cliente', action: 'pedidos')}"
                                   class="btn btn-primary"><i class="fa fa-user"></i></a>
                            </div>
                        </li>

                    </sec:ifLoggedIn>

                    <sec:ifNotLoggedIn>

                        <li id="your-account">
                            <div class="hidden-xs">
                                <h4><a href="${createLink(controller: 'login')}">Login</a></h4>

                                <p>
                                    <a href="${createLink(controller: 'cliente', action: 'cadastro')}">
                                        Cadastre-se aqui
                                    </a>
                                </p>
                            </div>

                            <div class="visible-xs">
                                <a href="${createLink(controller: 'login')}" class="btn btn-primary">
                                    <i class="fa fa-user"></i>
                                </a>
                            </div>
                        </li>

                    </sec:ifNotLoggedIn>


                    <li>
                        <div class="hidden-xs">
                            <h4><a href="${createLink(controller: 'shoppingCart')}">Carrinho</a></h4>
                            <p><a href="${createLink(controller: 'shoppingCart')}"><strong><cart:qtdeTotalItens/> Produto(s)</strong>
                            </a></p>
                        </div>

                        <div class="visible-xs">
                            <a href="${createLink(controller: 'shoppingCart')}" class="btn btn-primary">
                                <span class="cart-item">
                                    <cart:qtdeTotalItens/>
                                </span>
                                <i class="fa fa-shopping-cart"></i>
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

                        <li class="${(!grupoRaiz && controllerName == 'home' && actionName == 'index') ? 'active' : ''}">
                            <a href="${createLink(uri: '/')}">Home</a>
                        </li>

                        <g:each in="${GrupoDeProduto.findAllByPaiIsNull()}" var="grupo">
                            <g:render template="/layouts/menuitem"
                                      model="[grupo: grupo, isRoot: true, grupoRaiz: grupoRaiz]"/>
                        </g:each>

                        <li class="divider-vertical"></li>
                        <li class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">MAIS <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li class="${(request.forwardURI?.endsWith('comoComprar')) ? 'active' : ''}">
                                    <a class="btnComoComprar" data-target="${createLink(action:'comoComprar',controller:'home')}" href="#">Como Comprar</a>
                                </li>
                                <li class="divider"></li>
                                <li class="${(request.forwardURI?.endsWith('contato')) ? 'active' : ''}">
                                    <a href="${createLink(uri: '/contato')}">Contato</a>
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

                        <li class="${(!grupoRaiz && controllerName == 'home' && actionName == 'index') ? 'active' : ''}"><a
                                href="${createLink(uri: '/', absolute: true)}">Home</a></li>

                        <g:each in="${GrupoDeProduto.findAllByPaiIsNull()}" var="grupo">
                            <g:render template="/layouts/menuitem" model="[grupo: grupo, isRoot: true, grupoRaiz: grupoRaiz]"/>
                        </g:each>

                        <li class="${(request.forwardURI?.endsWith('comoComprar')) ? 'active' : ''}">
                            <a href="#" data-target="${createLink(action:'comoComprar',controller:'home')}"
                                        class="btnComoComprar">Como Comprar</a>
                        </li>

                        <li class="${(request.forwardURI?.endsWith('contato')) ? 'active' : ''}">
                            <a href="${createLink(uri: '/contato')}">Contato</a>
                        </li>

                    </ul>
                </div>
            </div>
        </div>
        <!-- end:nav-menus -->
    </g:if>


    <g:layoutBody/>

    <g:if test="${!ocultarRodape}">
        <g:render template="/layouts/footer"/>
    </g:if>

</div>
<!-- end:content -->

<!-- Le javascript -->
<!-- Placed at the end of the document so the pages load faster -->
<asset:javascript src="application.js"/>

<asset:deferredScripts/>

</body>
</html>
