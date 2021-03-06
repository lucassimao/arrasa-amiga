<%@ page import="br.com.arrasaamiga.Produto" %>

<!DOCTYPE html>
<html>
<head>
    <title>Arrasa Amiga: Produtos para maquiagem à pronta entrega</title>
    <parameter name="description" value="Produtos para maquiagem à pronta entrega"/>
    <parameter name="keywords" value="Maquiagem, blushes, NYX, batom, sombra, paleta,Teresina, Piauí, maquiadora"/>
    <parameter name="og:image" value="${assetPath(src: 'top.jpg')}"/>
    <meta name="layout" content="main"/>
    <asset:stylesheet href="jquery.raty.css"/>

    <asset:script>
        window.setTimeout(function() {
            $(".alert").fadeTo(1000, 0).slideUp(500, function(){
                $(this).alert('close');
            });
        }, 2000);
    </asset:script>

</head>


<body>

<g:if test="${flash.message}">
    <div class="alert alert-success">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <i class="fa fa-check-circle"></i> ${flash.message}
    </div>
</g:if>

<g:if test="${banners}">
    <!-- begin:home-slider -->

    <div id="home-slider" class="carousel slide" data-ride="carousel">
        <ol class="carousel-indicators">
            <g:each in="${0..<banners.size()}" var="idx">
                <li data-target="#home-slider" data-slide-to="${idx}" class="${(idx==0)?'active':''}"></li>
            </g:each>
        </ol>

        <div class="carousel-inner">

            <g:each in="${banners}" status="i" var="banner">

                    <div class="item ${(i == 0) ? 'active' : ''}">
                        <a href="${banner.link}" target="_blank">
                            <img  src="${resource(dir: 'images/banners',file: banner.arquivo)}" >
                            <div class="carousel-caption hidden-xs">
                                <h1 style="color:#F06EC2"><strong> ${banner.titulo} </strong></h1>
                                <p><h3><strong> ${banner.comentario} </strong></h3></p>
                            </div>
                        </a>
                    </div>

            </g:each>
        </div>
        <a class="left carousel-control" href="#home-slider" data-slide="prev">
            <i class="fa fa-angle-left"></i>
        </a>
        <a class="right carousel-control" href="#home-slider" data-slide="next">
            <i class="fa fa-angle-right"></i>
        </a>
    </div>
    <!-- end:home-slider -->
</g:if>



<!-- mais vendidos, novidades -->
<g:set var="count" value="${0}"/>
<g:set var="qtdeProdutosVisiveis" value="${produtos.size()}"/>


<div class="row">
    <div class="col-md-12">
        <div class="page-header">
             %{--<h2> Mais procurados <small> Produtos mais vendidos este mês</small></h2>--}%
        </div>
    </div>
</div>

<g:each in="${produtos}" var="produto" status="i">

    <g:if test="${count == 0}">
        <div class="row product-container">
    </g:if>


    <div class="col-md-3 col-sm-3 col-xs-6">
        <a class="product-item-link" href="${createLink(uri: produto.nomeAsURL)}">
            <div class="thumbnail product-item">
                <img src="${resource(dir:'images/produtos',file:produto.fotoMiniatura)}" alt="${produto.nome}" title="${produto.nome}"/>

                <div class="caption">
                    <h5>${produto.nome}</h5>

                    <p class="product-item-brand">${produto.marca ?: ''}</p>
                    <div class="star-rating" data-score="${produto.stars}"></div>

                    <p class="product-item-price">
                        <g:formatNumber number="${produto.precoAPrazoEmReais}"
                                        type="currency" currencyCode="BRL"></g:formatNumber>
                    <h6>
                        ou 5x de
                        <g:formatNumber number="${produto.calcularValorParcela(5)}" type="currency" currencyCode="BRL"/>
                        sem juros
                    </h6>
                </p>
                </div>


                <g:if test="${produto.isNovidade()}">
                    <div class="product-item-badge badge-sale">Novo </div>
                </g:if>
            </div>
        </a>
    </div>


    <g:set var="count" value="${++count}"/>

    <g:if test="${(i + 1) == qtdeProdutosVisiveis && count < 4}">

        <g:each in="${1..(4 - count)}">
            <g:set var="count" value="${++count}"/>
            <div class="col-md-3 col-sm-3 col-xs-6"></div>
        </g:each>

    </g:if>

    <g:if test="${count == 4}">
        </div>
        <g:set var="count" value="${0}"/>
    </g:if>

</g:each>


<!-- end:best-seller -->
<browser:isNotMobile>
    <div class="row">
        <div class="col-md-12">
            <div class="fb-comments" data-width="100%"
                 data-href="http://www.arrasaamiga.com.br" data-colorscheme="light"></div>
        </div>
    </div>
</browser:isNotMobile>

</body>
</html>



