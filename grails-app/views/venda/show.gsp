<%@ page import="br.com.arrasaamiga.Produto" %>
<%@ page import="br.com.arrasaamiga.FormaPagamento" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>

    <style type="text/css">
    table th, table td {
        text-align: center !important;
        vertical-align: middle !important;
    }

    .forma-pagamento-selecionado, .data-entrega-selecionada {
        font-weight: bold;
    }

    h5 {
        margin: 1px 0px;
    }

    .caption {
        font-family: Arial;
        font-weight: bold;
        font-size: 14px;
    }
    </style>

</head>


<body>

<g:set var="ocultarRodape" value="${true}" scope="request"/>

<hr>


<sec:ifNotGranted roles="ROLE_ADMIN">
    <div class="well" style="background-color:#F29BF2;color:white;border:1px solid white;">

        <h2><i class="icon-ok icon-white"></i> Recebemos seu pedido</h2>

        <h5>Obrigada, ${vendaInstance.cliente.nome}.</h5>
        <small>Você será informada(o) por e-mail sobre o andamento do pedido até a chegada ao endereço escolhido.</small>

    </div>
</sec:ifNotGranted>


<div class="well" style="text-align:center;background-color:white;color:rgb(102, 102, 102);">

    <sec:ifNotGranted roles="ROLE_ADMIN">
        <h3>O número do seu Pedido é #${vendaInstance.id.toString().padLeft(6,'0')}</h3>
    </sec:ifNotGranted>
    <sec:ifAllGranted roles="ROLE_ADMIN">
        <h3>Pedido #${vendaInstance.id.toString().padLeft(6,'0')}</h3>
    </sec:ifAllGranted>

    <hr>

    <g:if test="${vendaInstance.formaPagamento.equals(FormaPagamento.AVista)}">
        <asset:image class="hidden-xs" style="margin-top:45px;" src="timeline-02.png"/>
    </g:if>
    <g:else>
        <asset:image class="hidden-xs" style="margin-top:45px;" src="timeline-01.png"/>
    </g:else>

</div>


<div class="well" style="background-color:white;">

    <legend><i class="fa fa-truck"></i> Entrega</legend>

    <div class="row">

        <div class="col-md-1 col-xs-3"><span class="caption">Nome:</span></div>
        <div class="col-md-5 col-xs-9">${vendaInstance.cliente.nome}</div>

        <div class="col-md-1 col-xs-3"><span class="caption">Email:</span></div>
        <div class="col-md-5 col-xs-9">${vendaInstance.cliente.email}</div>

    </div>
    <div class="row">

        <div class="col-md-1 col-xs-3"><span class="caption">Telefone:</span></div>
        <div class="col-md-5 col-xs-9">${vendaInstance.cliente.dddTelefone}-${vendaInstance.cliente.telefone}</div>

        <div class="col-md-1 col-xs-3"><span class="caption">Celular:</span></div>
        <div class="col-md-5 col-xs-9">${vendaInstance.cliente.dddCelular}-${vendaInstance.cliente.celular}</div>
    </div>
    <div class="row">
        <div class="col-md-1 col-xs-3"><span class="caption">Estado:</span></div>
        <div class="col-md-5 col-xs-9">${vendaInstance.cliente.endereco.uf?.nome}</div>

        <div class="col-md-1 col-xs-3"><span class="caption">Cidade:</span></div>
        <div class="col-md-5 col-xs-9">${vendaInstance.cliente.endereco.cidade?.nome}</div>
    </div>
    <div class="row">
        <div class="col-md-1 col-xs-3"><span class="caption">Bairro:</span></div>
        <div class="col-md-5 col-xs-9">${vendaInstance.cliente.endereco.bairro}</div>

        <div class="col-md-1 col-xs-3"><span class="caption">Endereço:</span></div>
        <div class="col-md-5 col-xs-9">${vendaInstance.cliente.endereco.complemento}</div>
    </div>

    <div class="row">

        <g:if test="${vendaInstance.cliente.isDentroDaAreaDeEntregaRapida()}">
            <div class="col-md-1 col-xs-3"><span class="caption">Entrega:</span></div>
            <div class="col-md-11 col-xs-9">
                <g:formatDate format="EEEE, dd/MM/yyyy"  date="${vendaInstance.dataEntrega}"/>
            </div>
        </g:if>
        <g:else>

            <div class="col-md-1 col-xs-3"><span class="caption">CEP:</span></div>
            <div class="col-md-5 col-xs-9">${vendaInstance.cliente.endereco.cep}</div>

            <g:if test="${vendaInstance.codigoRastreio}">
                <div class="col-md-1 col-xs-3"><span class="caption">Código de Rastreio:</span></div>
                <div class="col-md-5 col-xs-9">${vendaInstance.codigoRastreio}</div>
            </g:if>
        </g:else>

    </div>

</div>


<div class="row" style="margin:20px 0px;">

    <table class="table table-bordered table-striped table-condensed">
        <thead>
        <tr>
            <th class="col-sm-1">Produto</th>
            <th>Descrição</th>
            <th class="col-sm-1">Preço</th>
            <th class="col-sm-1">Quantidade</th>
            <th class="col-sm-1">Total</th>
        </tr>
        </thead>
        <tbody>
        <!-- renderizando items -->
        <g:each in="${vendaInstance.itensVenda}" var="item">
            <g:set var="produto" value="${item.produto}"/>
            <tr>
                <td>
                    <a href="${createLink(uri: produto.nomeAsURL, absolute: true)}">
                        <img src="${resource(dir:'images/produtos',file:produto.fotoMiniatura)}" alt="${produto.nome}"
                                     title="${produto.nome}" class="img-cart"/>
                    </a>
                </td>
                <td>
                    <a href="${createLink(uri: produto.nomeAsURL, absolute: true)}">
                        <strong>${produto.nome}</strong> ${(produto.marca) ? (" - " + produto.marca) : ''}
                    </a>
                    <g:if test="${produto.unidades.size() > 1}">
                        <p>${produto.tipoUnitario} : ${item.unidade}</p>
                    </g:if>
                </td>
                <td><g:formatNumber number="${item.precoAPrazoEmReais}" type="currency" currencyCode="BRL"/></td>
                <td>
                    <p style="text-align:center;font-size:15px;">
                        <span class="badge alert-success">${item.quantidade}</span>
                    </p>
                </td>
                <td><g:formatNumber number="${item.subTotalAPrazo}" type="currency" currencyCode="BRL"/></td>
            </tr>
        </g:each>

        <tr>
            <td colspan="6">&nbsp;</td>
        </tr>
        </tbody>

    </table>

</div>


<div class="well" style="background-color:white;">

    <div id="div-subtotal">
        <h4 style="display:inline;color:#666;">Subtotal</h4>

        <div style="float:right;font-weight:bold;font-size:15px;color:#666;">
            <g:formatNumber number="${vendaInstance.valorItensAPrazo}" type="currency" currencyCode="BRL"/>
        </div>
    </div>

    <div id="div-frete" style="clear:both;">
        <h5 style="display:inline;color:blue;">${vendaInstance.cliente.isDentroDaAreaDeEntregaRapida() ? 'Taxa de Entrega' : 'Frete'}</h5>

        <div style="float:right;font-weight:bold;">
            <div style="color:blue;font-size:10px;text-align:right;">
                + <g:formatNumber number="${vendaInstance.freteEmReais}" type="currency" currencyCode="BRL"/>
            </div>
        </div>

    </div>


    <g:if test="${vendaInstance.descontoEmReais > 0}">

        <div id="div-desconto" style="clear:both;">
            <h5 style="display:inline;color:blue;">Desconto</h5>

            <div style="float:right;font-weight:bold;">
                <div style="color:blue;font-size:10px;text-align:right;">
                    - <g:formatNumber number="${vendaInstance.descontoEmReais}" type="currency" currencyCode="BRL"/>
                </div>
            </div>
        </div>

    </g:if>


    <div id="div-valor-total">
        <hr>

        <h4 style="color:#666;display:inline;">Valor Total</h4>

        <div style="float:right;font-weight:bold;font-size:35px;color:#00adef;">
            <g:formatNumber number="${vendaInstance.valorTotal}" type="currency" currencyCode="BRL"/>
        </div>
    </div>

</div>


<div class="well" style="background-color:white;">

    <legend><i class="fa fa-money"></i> Forma de Pagamento</legend>

    <div class="row-fluid">

        <div class="span6">
            ${vendaInstance.detalhesPagamento}
        </div>

    </div>

</div>

<hr>

<div class="row">
    <div>
        <a href="${createLink(uri: '/', absolute: true)}" style="float:right;" id="btnFecharVenda"
           class="btn btn-success">
            <i class="icon-home icon-white"></i>
            Ir Para Página Principal
        </a>
    </div>
</div>

</body>
</html>



