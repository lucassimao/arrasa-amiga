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


      <sec:ifNotGranted roles="ROLE_ADMIN">
        <div class="well" style="background-color:#F29BF2;color:white;border:1px solid white;">

          <h2>  <i class="icon-ok icon-white"></i> Recebemos seu pedido</h2>
          
          <h5>  Obrigada, ${venda.cliente.nome}.   </h5>
          <small>  Você será informada(o) por e-mail sobre o andamento do pedido até a chegada ao endereço escolhido.  </small>
       
        </div>
      </sec:ifNotGranted>


      <div class="well" style="text-align:center;background-color:white;color:rgb(102, 102, 102);">

        <sec:ifNotGranted roles="ROLE_ADMIN">
            <h3>  O número do seu Pedido é #${numeroPedido} </h3>
        </sec:ifNotGranted>
        <sec:ifAllGranted roles="ROLE_ADMIN">
            <h3>  Pedido #${numeroPedido} </h3>
        </sec:ifAllGranted>        

        <hr>

        <g:if test="${venda.formaPagamento.equals(FormaPagamento.AVista)}">
            <asset:image class="hidden-xs" style="margin-top:45px;" src="timeline-02.png"/>
        </g:if>
        <g:else>
            <asset:image class="hidden-xs" style="margin-top:45px;" src="timeline-01.png"/>
        </g:else>
        
      </div>


        <div class="well" style="background-color:white;">

          <legend> <i class="fa fa-truck"></i> Entrega </legend>

          <sec:ifAllGranted roles="ROLE_ADMIN">
            <div class="row">
              <div class="col-md-3"> <label> <span class="caption"> Nome: </span> </label> ${venda.cliente.nome}</div>
              <div class="col-md-3"><label> <span class="caption"> Telefone: </span> </label>${venda.cliente.dddTelefone}-${venda.cliente.telefone}</div>
              <div class="col-md-3"><label> <span class="caption"> Celular: </span> </label>${venda.cliente.dddCelular}-${venda.cliente.celular}</div>
              <div class="col-md-3"><label> <span class="caption"> Email: </span> </label>${venda.cliente.email}</div>
            </div>   
          </sec:ifAllGranted>
          <div class="row">
            <div class="col-md-4"> <label> <span class="caption"> Estado: </span> </label> ${venda.cliente.endereco.uf.nome}</div>
            <div class="col-md-4"><label> <span class="caption"> Cidade: </span> </label> ${venda.cliente.endereco.cidade.nome}</div>
            <div class="col-md-4"><label> <span class="caption"> Bairro: </span> </label>${venda.cliente.endereco.bairro}</div>
          </div>          
          <div class="row">
            <div class="col-md-12"><label> <span class="caption"> Endereço: </span>  </label>${venda.cliente.endereco.complemento}</div> 
          </div>
          <div class="row">
                         
            <g:if test="${venda.cliente.isDentroDaAreaDeEntregaRapida()}">
              <div class="col-md-4">
                <label> <span class="caption"> Dia da Entrega: </span> </label><g:formatDate format="EEEE, dd/MM/yyyy" date="${venda.dataEntrega}"/>
              </div>              
            </g:if>
            <g:else>

              <div class="col-md-4">
                <label> <span class="caption"> CEP: </span> </label>${venda.cliente.endereco.cep}
              </div>              

               <g:if test="${venda.codigoRastreio}">
                  <div class="col-md-4">
                    <label> <span class="caption"> Código de Rastreio: </span> </label> <a target="_blank" href="${venda.trackingURL}"> ${venda.codigoRastreio} </a> 
                  </div>                   
              </g:if>       

            </g:else>

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
            <g:each in="${venda.itensVenda}" var="item">
                <g:set var="produto" value="${item.produto}"/>
                <tr>
                  <td> 
                    <a href="${createLink(uri:produto.nomeAsURL,absolute:true)}">
                      <asset:image src="produtos/${produto.fotoMiniatura}" alt="${produto.nome}" title="${produto.nome}" class="img-cart" />
                    </a>
                  </td>
                  <td>
                    <a href="${createLink(uri:produto.nomeAsURL,absolute:true)}">
                      <strong>${produto.nome} </strong> ${ (produto.marca)? (" - " + produto.marca):''}
                    </a>
                    <g:if test="${produto.unidades.size() > 1}">
                      <p>${produto.tipoUnitario} : ${item.unidade}</p>
                    </g:if>
                  </td>
                  <td><g:formatNumber number="${item.precoAPrazoEmReais}" type="currency" currencyCode="BRL" /></td>
                  <td>
                    <p style="text-align:center;font-size:15px;"> 
                      <span class="badge alert-success">${item.quantidade}</span>  
                    </p>
                  </td>
                  <td><g:formatNumber number="${item.subTotalAPrazo}" type="currency" currencyCode="BRL" /></td>
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
              <g:formatNumber number="${venda.valorItensAPrazo}" type="currency" currencyCode="BRL" />
            </div>
          </div>

          <g:if test="${!venda.cliente.isDentroDaAreaDeEntregaRapida()}">

            <div id="div-frete" style="clear:both;">
              <h5 style="display:inline;color:blue;">Frete</h5>

              <div style="float:right;font-weight:bold;"> 
                <div style="color:blue;font-size:10px;text-align:right;">
                  + <g:formatNumber number="${venda.freteEmReais}" type="currency" currencyCode="BRL" />
                </div>
              </div>

            </div>

          </g:if>

          <g:if test="${venda.descontoEmReais > 0}">
              
              <div id="div-desconto" style="clear:both;">
                <h5 style="display:inline;color:blue;">Desconto</h5>

                <div style="float:right;font-weight:bold;"> 
                  <div style="color:blue;font-size:10px;text-align:right;">
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
            
            <h4 style="color:#666;display:inline;">Valor Total</h4>

            <div style="float:right;font-weight:bold;font-size:35px;color:#00adef;"> 
              <g:formatNumber number="${venda.valorTotal}" type="currency" currencyCode="BRL" />
            </div>
          </div>

        </div>



      <div class="well" style="background-color:white;">

        <legend> <i class="fa fa-money"></i> Forma de Pagamento </legend>
        <div class="row-fluid">

          <div class="span6">
              ${venda.detalhesPagamento}
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



