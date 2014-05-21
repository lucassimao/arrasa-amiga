<%@ page import="br.com.arrasaamiga.Produto" %>
<%@ page import="br.com.arrasaamiga.FormaPagamento" %>
<%@ page contentType="text/html"%>

<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>

    <style type="text/css">
      table th, table td {
        text-align: center !important;
        vertical-align: middle !important;
      }

      .forma-pagamento-selecionado, .data-entrega-selecionada{
        font-weight: bold;
      }

      h5{
        margin: 1px 0px;
      }

      .caption{
        font-family:Arial;
        font-weight:bold;
        font-size:14px;
      }
    </style>

  </head>


    <body>

        <g:set var="ocultarRodape" value="${true}" scope="request"/>

        <div style="background-color:#F29BF2;color:white;border:1px solid white;text-align:center;">

            <h2>  Nova Venda </h2>

        </div>

        <hr>


        <table style="background-color:white;width:100%;margin-top:20px;margin-bottom:20px;">
            <thead>

                <tr>
                    <th> Cliente </th>
                    <th> UF </th>
                    <th> Cidade </th>
                    <th> Bairro </th>
                    <th> Complemento </th>
                    <th> 
                         <g:if test="${!cliente.isDentroDaAreaDeEntregaRapida()}">
                            Data da Entrega
                         </g:if>
                         <g:else>
                            CEP
                         </g:else>
                    </th>
                </tr>

            </thead>
            <tbody>

                <tr>
                    <td>
                       ${cliente.nome}
                    </td>

                    <td>
                       ${cliente.endereco.uf.nome}
                    </td>

                    <td>
                        ${cliente.endereco.cidade.nome}
                    </td>

                    <td>
                       ${cliente.endereco.bairro}
                    </td>

                    <td>
                        ${cliente.endereco.complemento}
                    </td>

                    <td>
                         <g:if test="${!cliente.isDentroDaAreaDeEntregaRapida()}">
                            <g:formatDate format="EEEE, dd/MM/yyyy" date="${venda.dataEntrega}"/>
                         </g:if>
                         <g:else>
                            ${cliente.endereco.cep}
                         </g:else>
                    </td>                    

                </tr>

            </tbody>
        </table>

        <hr>




        <table  style="background-color:white;width:100%;">
            <thead >
                <tr>
                    <th> Descrição </th>
                    <th> Quantidade </th>
                    <th> Preço Unitário </th>
                    <th> Valor Total do Item </th>
                </tr>             
            </thead>
            <tbody>

                <g:each in="${venda.itensVenda}" var="itemVenda">
                <g:set var="produto" value="${itemVenda.produto}"/>

                <tr>
                    <td style="text-align:left !important;">
                        <g:img absolute="true" dir="img/produtos" style="float:left;" file="${produto.fotoMiniatura}"/>

                        <div>
                            <label>${produto.nome }</label>

                            <g:if test="${produto.isMultiUnidade()}">
                                <p> <small> ${produto.tipoUnitario}: ${itemVenda.unidade} </small> </p>
                            </g:if>

                        </div>
                    </td>

                    <td style="position:relative;">
                        <p> <span class="badge badge-warning">${itemVenda.quantidade}</span>  </p>
                    </td>

                    <td>
                        <g:formatNumber number="${itemVenda.precoAPrazoEmReais}" type="currency" 
                        currencyCode="BRL" />
                    </td>

                    <td>
                        <g:formatNumber number="${itemVenda.subTotalAPrazo}" type="currency" 
                        currencyCode="BRL" />
                    </td>

                </tr>

                </g:each>

            </tbody>
        </table>






    <div style="background-color:white;">

        <div id="div-subtotal">
            <h4 style="display:inline;color:#666;">Subtotal</h4>

            <div style="float:right;font-weight:bold;font-size:15px;color:#666;"> 
                <g:formatNumber number="${venda.valorItensAPrazo}" type="currency" currencyCode="BRL" />
            </div>
        </div>

        <g:if test="${!cliente.isDentroDaAreaDeEntregaRapida()}">

            <div id="div-frete" style="clear:both;">
                <h5 style="display:inline;color:blue;">Frete</h5>

                <div style="float:right;font-weight:bold;"> 
                    <div style="color:blue;font-size:12px;text-align:right;">
                    + <g:formatNumber number="${venda.freteEmReais}" type="currency" currencyCode="BRL" />
                    </div>
                </div>
            </div>

        </g:if>

        <g:if test="${venda.descontoEmReais > 0}">

            <div id="div-desconto" style="clear:both;">
                <h5 style="display:inline;color:blue;">Desconto</h5>

                <div style="float:right;font-weight:bold;"> 
                    <div style="color:blue;font-size:12px;text-align:right;">
                        - <g:formatNumber number="${venda.descontoEmReais}" type="currency" currencyCode="BRL" />
                    </div>
                </div>
            </div>

        </g:if>

        <g:if test="${venda.taxaEntregaEmReais > 0}">

            <div style="clear:both;">
                <h5 style="display:inline;color:blue;">Taxa de Entrega</h5>

                <div style="float:right;font-weight:bold;"> 
                    <div style="color:blue;font-size:10px;text-align:right;">
                        + <g:formatNumber number="${venda.taxaEntregaEmReais}" type="currency" currencyCode="BRL" />
                    </div>
                </div>
            </div>

        </g:if>


        <div id="div-valor-total">
            <hr>

            <h2 style="color:#666;display:inline;">Valor Total</h2>

            <div style="float:right;font-weight:bold;font-size:35px;color:#00adef;"> 
                <g:formatNumber number="${venda.valorTotal}" type="currency" currencyCode="BRL" />
            </div>
        </div>

    </div>


        <div id="div-forma-pagamento" style="clear:both;">
            <hr>

            <h5 style="display:inline;color:blue;">Forma de Pagamento</h5>

            <div style="float:right;font-weight:bold;font-size:12px;color:#00adef;"> 
                 ${venda.detalhesPagamento}
            </div>
        </div>




    <hr  style="clear:both;">

    </body>
</html>



