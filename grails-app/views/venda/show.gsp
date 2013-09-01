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

      <hr>


      <div class="well" style="background-color:#F29BF2;color:white;border:1px solid white;">

        <h2>  <i class="icon-ok icon-white"></i> Recebemos seu pedido</h2>
        
        <h5>  Obrigada, ${cliente.nome}.   </h5>
        <small>  Você será informada(o) por e-mail sobre o andamento do pedido até a chegada ao endereço escolhido.  </small>
     
      </div>


      <div class="well" style="text-align:center;background-color:white;color:rgb(102, 102, 102);">

        <h3 >  O número do seu Pedido é ${numeroPedido} </h3>

        <hr>

        <g:img style="margin-top:45px;" dir="img" file="timeline-01.png"/>
        
      </div>


        <div class="well" style="background-color:white;">

          <legend> <i class="icon-truck"></i> Entrega </legend>

          <div class="row-fluid">
            <div class="span5">
              <label> <span class="caption"> CEP: </span> ${enderecoEntrega.cep} </label>

              <label> <span class="caption"> Estado: </span> ${enderecoEntrega.uf}</label>

              <label> <span class="caption"> Cidade: </span> ${enderecoEntrega.cidade}</label>

              <label> <span class="caption"> Bairro: </span> ${enderecoEntrega.bairro} </label>

              <label> <span class="caption"> Endereço: </span> ${enderecoEntrega.endereco}</label>

              <label> <span class="caption"> Complemento: </span>  ${enderecoEntrega.complemento}</label>

              <g:if test="${enderecoEntrega.pontoDeReferencia}">
                <label> <span class="caption"> Ponto de Referência: </span> ${enderecoEntrega.pontoDeReferencia}</label>
              </g:if>
            </div>
            
            <g:if test="${enderecoEntrega.fromTeresina}">

              <div class="span7">

                <p style="font-family:Arial;">            
                    <span style="font-family:Arial;font-weight:bold;font-size:14px;"> Dia da entrega: </span> 
                    <g:formatDate format="EEEE, dd/MM/yyyy" date="${dataEntrega}"/>
                </p>

                <hr>

                <p style="font-family:Arial;font-weight:bold;font-size:14px;">
                  Informações adicionais:
                </p>
                
                <p style="font-family:Arial;text-align:justify;">
                    ${informacoesAdicionaisEntrega}
                </p>
                
              </div>

            </g:if>

          </div>

        </div>





      <div class="row-flow" style="margin:20px 0px;"> 

        <table class="table table-bordered" style="background-color:white;">
          <thead >
            <tr>
              <th> Descrição </th>
              <th> Quantidade </th>
              <th> Preço Unitário </th>
              <th> Valor Total do Item </th>
            </tr>             
          </thead>
          <tbody>
            <g:each in="${itens}" var="itemVenda">
              <g:set var="produto" value="${itemVenda.produto}"/>
              
              <tr>
                <td style="text-align:left !important;">
                  <g:img dir="img/produtos" style="float:left;" file="${produto.fotoMiniatura}"/>

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

      </div>  






        <div class="well" style="background-color:white;">


          <div id="div-subtotal">
            <h4 style="display:inline;color:#666;">Subtotal</h4>

            <div style="float:right;font-weight:bold;font-size:15px;color:#666;"> 
              <g:formatNumber number="${subTotal}" type="currency" currencyCode="BRL" />
            </div>
          </div>

          <g:if test="${!enderecoEntrega.fromTeresina}">

            <div id="div-frete" style="clear:both;">
              <h5 style="display:inline;color:blue;">Frete</h5>

              <div style="float:right;font-weight:bold;border-bottom:1px solid red;"> 
                <div style="color:blue;font-size:10px;text-align:right;">
                  + <g:formatNumber number="${frete}" type="currency" currencyCode="BRL" />
                </div>
              </div>

            </div>

          </g:if>

          <div id="div-desconto" style="clear:both;">
            <h5 style="display:inline;color:blue;">Desconto</h5>

            <div style="float:right;font-weight:bold;"> 
              <div style="color:blue;font-size:10px;text-align:right;">
                - <g:formatNumber number="${desconto}" type="currency" currencyCode="BRL" />
              </div>
            </div>

          </div>


          <div id="div-valor-total">
            <hr>
            
            <h4 style="color:#666;display:inline;">Valor Total</h4>

            <div style="float:right;font-weight:bold;font-size:35px;color:#00adef;"> 
              <g:formatNumber number="${valorTotal}" type="currency" currencyCode="BRL" />
            </div>
          </div>

        </div>



      <div class="well" style="background-color:white;">

        <legend> <i class="icon-money"></i> Forma de Pagamento </legend>
        <div class="row-fluid">

          <div class="span6">
              ${detalhesPagamento}
          </div>

        </div>

      </div>

      <hr>

      <div  class="row-fluid">
          <div>
              <a href="${createLink(uri:'/')}" style="float:right;" id="btnFecharVenda" class="btn btn-success">
                 <i class="icon-home icon-white"></i>
                 Ir Para Página Principal
              </a>
          </div>
      </div>


  </body>
</html>



