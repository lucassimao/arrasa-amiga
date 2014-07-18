<!DOCTYPE html>
<html lang="en">
  <head>
    <meta name="layout" content="main"/>  
    <title>Carrinho</title>

    <style type="text/css">
      table th.col-sm-1 { text-align: center;}
    </style>

    <script type="text/javascript">
        window.setTimeout(function() {
            $(".alert").fadeTo(500, 0).slideUp(500, function(){
                $(this).alert('close'); 
            });
        }, 3000);
    </script>

  </head>

  <body>

    <g:set var="ocultarRodape" value="${true}" scope="request"/>


    <!-- begin:content -->
    <div class="container">

      <!-- begin:article -->
      <div class="row">

        <!-- begin:content -->
        <div class="content">

          <div class="row">

            <div class="col-md-12">           

              <h3>Carrinho de Compras</h3>
              <hr />

              <g:if test="${flash.message}">
                <div class="alert alert-success">
                   <button type="button" class="close" data-dismiss="alert">&times;</button>
                   ${flash.message}
                </div>
              </g:if>              
                
              <table class="table table-bordered table-striped table-condensed">
               <thead>
                <tr>
                  <th class="col-sm-1">Produto</th>
                  <th>Descrição</th>
                  <th class="col-sm-1">Preço</th>
                  <th class="col-sm-1">Quantidade</th>
                  <th class="col-sm-1">Total</th>
                  <th class="col-sm-1">Excluir</th>
                </tr>
               </thead>
               <tbody>
                <!-- renderizando items -->
                <g:each in="${itens}" var="item">
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

                        <div style="width:80px;display:block;margin-left:auto;margin-right:auto;">

                            <g:form action="add" class="form-inline" controller="shoppingCart" style="float:left;margin-right:5px;">
                              <g:hiddenField name="id" value="${produto.id}"/>
                              <g:hiddenField name="unidade" value="${item.unidade}"/>
                              <g:hiddenField name="quantidade" value="${1}"/>

                              <button type="submit" class="btn btn-primary">
                                <i class="fa fa-plus"></i>
                              </button>  
                            </g:form>

                            <g:form action="removerProduto" class="form-inline" controller="shoppingCart">
                              <g:hiddenField name="id" value="${produto.id}"/>
                              <g:hiddenField name="unidade" value="${item.unidade}"/>
                              <g:hiddenField name="quantidade" value="${1}"/>

                              <button type="submit" class="btn btn-primary">
                                <i class="fa fa-minus"></i>
                              </button>                            
                            </g:form>  

                        </div>                     
                      </td>
                      <td><g:formatNumber number="${item.subTotalAPrazo}" type="currency" currencyCode="BRL" /></td>
                      <td style="text-align: center;vertical-align:middle;">

                            <g:form class="form-inline" action="removerProduto" controller="shoppingCart">
                              <g:hiddenField name="id" value="${produto.id}"/>
                              <g:hiddenField name="unidade" value="${item.unidade}"/>
                              <g:hiddenField name="quantidade" value="${item.quantidade}"/>

                              <button type="submit" class="btn btn-primary">
                                <i class="fa fa-trash-o"></i>
                              </button>                          
                            </g:form> 

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
                  <td colspan="2" class="text-left"><g:formatNumber number="${valorTotal}" type="currency" currencyCode="BRL" /></td>
                </tr>

               </tbody>

              </table>
              
              <a class="btn btn-primary" href="${createLink(uri:'/',absolute:true)}"><i class="fa fa-angle-double-left"></i> Escolher + produtos</a>
              <a class="btn btn btn-success pull-right" href="${createLink(controller:'shoppingCart',action:'confirmAddress')}">
                Continuar <i class="fa fa-angle-double-right"></i>
              </a>

            </div>
          </div>
        </div>
        <!-- end:content -->
      </div>
      <!-- end:article -->


    </div>
    <!-- end:content -->

  </body>
</html>
