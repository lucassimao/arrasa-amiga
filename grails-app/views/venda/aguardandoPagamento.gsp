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

        <h2>  <i class="icon-warning-sign icon-white"></i> Aguardando confirmação de pagamento... </h2>
        
        <h5> Aguarde a autorização da sua compra </h5>
        <h6>
          Estamos esperando receber a confirmação do seu pagamento! Assim que confirmado você será avisada(o)
          por e-mail e enviaremos rapidamente o seu pedido!
        </h6>
     
      </div>

  </body>
</html>



