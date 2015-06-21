<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
    <asset:link rel="apple-touch-icon" href="apple-touch-icon.png" type="image/x-icon"/>
    <asset:link rel="apple-touch-icon" sizes="114x114" href="apple-touch-icon-retina.png" type="image/x-icon"/>
    <asset:stylesheet href="main.css"/>
    <asset:stylesheet href="mobile.css"/>

    <g:layoutHead/>

    <style type="text/css">


    #primary_nav_wrap {
        height: 25px;
    }

    #primary_nav_wrap ul {
        list-style: none;
        position: relative;
        float: left;
        overflow: visible;
        margin: 0;
        padding: 0;
    }

    #primary_nav_wrap ul li {
        position: relative;
        float: left;
        margin: 0;
        padding: 0
    }

    #primary_nav_wrap ul li:hover {
        background: #f6f6f6
    }

    #primary_nav_wrap ul ul {
        display: none;
        position: absolute;
        top: 100%;
        left: 0;
        background: #fff;
        padding: 0
    }

    #primary_nav_wrap ul ul li {
        float: none;
        width: 200px
    }

    #primary_nav_wrap ul ul a {
        line-height: 120%;
        padding: 10px 15px
    }

    #primary_nav_wrap ul ul ul {
        top: 0;
        left: 100%
    }

    #primary_nav_wrap ul li:hover > ul {
        display: block
    }
    </style>

</head>

<body>
<div class="nav" id="primary_nav_wrap" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}">Home</a></li>
        <li>
            <a style="${(controllerName.equals('produto')) ? 'background-color:#666;color:white;' : ''}"
               href="${createLink(controller: 'produto')}">
                Produtos
            </a>
        </li>


        <li>
            <a style="${(controllerName.equals('banner')) ? 'background-color:#666;color:white;' : ''}"
               href="${createLink(controller: 'banner')}">
                Banners
            </a>
        </li>

        <li>
            <a style="${(controllerName.equals('pedido')) ? 'background-color:#666;color:white;' : ''}"
               href="${createLink(controller: 'pedido')}">
                Pedidos
            </a>
        </li>

        <li>
            <a style="${(controllerName.equals('estoque')) ? 'background-color:#666;color:white;' : ''}"
               href="${createLink(controller: 'estoque', action: 'index')}">
                Estoque
            </a>
        </li>

        <li>
            <a style="${(controllerName.equals('venda')) ? 'background-color:#666;color:white;' : ''}"
               href="${createLink(controller: 'venda', action: 'index')}">
                Vendas
            </a>
      %{--      <ul>
                <li><a href="#">Sub Deep 1</a></li>
                <li><a href="#">Sub Deep 2</a></li>
                <li><a href="#">Sub Deep 3</a></li>
                <li><a href="#">Sub Deep 4</a></li>
            </ul>--}%
        </li>

        <li>
            <a style="${(controllerName.equals('aviso')) ? 'background-color:#666;color:white;' : ''}"
               href="${createLink(controller: 'aviso')}">
                Avisos
            </a>
        </li>

        <li>
            <a style="${(controllerName.equals('grupoDeProduto')) ? 'background-color:#666;color:white;' : ''}"
               href="${createLink(controller: 'grupoDeProduto')}">
                Grupos de Produtos
            </a>
        </li>

        <li>
            <a style="${(controllerName.equals('feriado')) ? 'background-color:#666;color:white;' : ''}"
               href="${createLink(controller: 'feriado')}">
                Feriados
            </a>
        </li>

    </ul>
</div>

<g:layoutBody/>
<div class="footer" role="contentinfo"></div>

<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>

<asset:deferredScripts/>

</body>
</html>
