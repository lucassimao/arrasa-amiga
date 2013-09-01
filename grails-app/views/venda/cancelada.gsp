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

    </style>

  </head>


  <body>

       <g:set var="ocultarRodape" value="${true}" scope="request"/>

      <hr>


      <div class="well" style="background-color:#F29BF2;color:white;border:1px solid white;">

        <h2>  <i class="icon-warning-sign icon-white"></i> Ocorreu um problema amiga ... </h2>
        
        <h5> O pagamento da sua compra não foi autorizado ;-( </h5>
        <h6> 
          Você pode tentar uma outra forma de pagamento ou tentar a mesma novamente. 
          Clique <g:link controller="shoppingCart" action="index"> aqui </g:link> e tente novamente
        </h6>
     
      </div>

  </body>
</html>



