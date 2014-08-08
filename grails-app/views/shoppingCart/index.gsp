<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="layout" content="main"/>
    <title>Carrinho</title>

    <style type="text/css">
        table th.col-sm-1, table th.col-sm-2 {
            text-align: center;
        }

        h6{
            margin: 0px;
            padding: 0px;
        }
    </style>

    <asset:script>
        window.setTimeout(function () {
            $(".alert").fadeTo(500, 0).slideUp(500, function () {
                $(this).alert('close');
            });
        }, 2000);

        $(".mini-button").click(function(){
            $(this).parent().submit();
        });
    </asset:script>

</head>

<body>

<g:set var="ocultarRodape" value="${false}" scope="request"/>

<div class="col-md-12">

    <h3>Carrinho de Compras</h3>
    <hr/>

    <g:if test="${flash.message}">
        <div class="alert alert-success">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            ${flash.message}
        </div>
    </g:if>

    <table class="table table-bordered table-striped table-condensed">
        <thead>
        <tr>
            <th class="col-sm-6">Produto</th>
            <th class="col-sm-1">Preço</th>
            <th class="col-sm-2">Quantidade</th>
            <th class="col-sm-1">Excluir</th>
            <th class="col-sm-1">Total</th>
        </tr>
        </thead>
        <tbody>
        <!-- renderizando items -->
        <g:each in="${itens}" var="item">
            <g:set var="produto" value="${item.produto}"/>

            <tr>
                <td>
                    <a href="${createLink(uri: produto.nomeAsURL, absolute: true)}">
                        <asset:image style="float:left;" src="produtos/${produto.fotoMiniatura}" alt="${produto.nome}"
                                     title="${produto.nome}" class="img-cart"/>
                    </a>

                    <span class="hidden-xs">
                        <strong>${produto.nome}</strong> ${(produto.marca) ? (" - " + produto.marca) : ''}
                    </span>

                    <g:if test="${produto.unidades.size() > 1}">
                        <h6 class="hidden-xs">${produto.tipoUnitario}: ${item.unidade}</h6>
                    </g:if>

                </td>

                <td style="text-align: center;vertical-align: middle;">
                    ${formatNumber(number: produto.precoAPrazoEmReais, type: 'currency', currencyCode: 'BRL')}
                </td>

                <td style="text-align: center;vertical-align: middle;">

                    <g:form action="add" controller="shoppingCart" style="display:inline;">
                        <g:hiddenField name="id" value="${produto.id}"/>
                        <g:hiddenField name="unidade" value="${item.unidade}"/>
                        <g:hiddenField name="quantidade" value="${1}"/>

                        <button type="submit" class="btn btn-primary hidden-xs">
                            <i class="fa fa-plus"></i>
                        </button>
                        <a href="#" class="mini-button visible-xs pull-left">
                            <i class="fa fa-plus"></i>
                        </a>
                    </g:form>

                    <span class="badge alert-success" style="clear: both;margin:0 3px;">${item.quantidade}</span>

                    <g:form action="removerProduto" controller="shoppingCart" style="display:inline;">
                        <g:hiddenField name="id" value="${produto.id}"/>
                        <g:hiddenField name="unidade" value="${item.unidade}"/>
                        <g:hiddenField name="quantidade" value="${1}"/>

                        <button type="submit" class="btn btn-primary hidden-xs">
                            <i class="fa fa-minus"></i>
                        </button>

                        <a href="#" class="mini-button visible-xs pull-right">
                            <i class="fa fa-minus"></i>
                        </a>
                    </g:form>

                </td>
                <td style="text-align: center;vertical-align: middle;">

                    <div class="btn-group">
                        <g:form action="removerProduto" controller="shoppingCart">
                            <g:hiddenField name="id" value="${produto.id}"/>
                            <g:hiddenField name="unidade" value="${item.unidade}"/>
                            <g:hiddenField name="quantidade" value="${item.quantidade}"/>

                            <button type="submit" class="btn btn-default">
                                <i class="fa fa-trash-o"></i>
                            </button>
                        </g:form>
                    </div>

                </td>
                <td style="text-align: center;vertical-align: middle;">
                    <g:formatNumber number="${item.subTotalAPrazo}" type="currency" currencyCode="BRL"/>
                </td>
            </tr>
        </g:each>

        <tr>
            <td colspan="6">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="4" class="text-right">
                <strong>SubTotal</strong>
            </td>
            <td colspan="2" class="text-left">
                <g:formatNumber number="${valorTotal}" type="currency" currencyCode="BRL"/>
            </td>
        </tr>

        </tbody>

    </table>





    <a class="btn btn-primary" href="${createLink(uri: '/', absolute: true)}"><i
            class="fa fa-angle-double-left"></i> Escolher + produtos</a>
    <a class="btn btn btn-success pull-right"
       href="${createLink(controller: 'shoppingCart', action: 'confirmAddress')}">
        Continuar <i class="fa fa-angle-double-right"></i>
    </a>
</div>

</body>
</html>
