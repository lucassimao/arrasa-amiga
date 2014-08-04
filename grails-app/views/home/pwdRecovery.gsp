<html>
<head>
	
	
	<meta name='layout' content='main'/>

	<style type='text/css' media='screen'>

      .form-signin {
        max-width: 330px;
        padding: 19px 29px 29px;
        margin: 10px auto 20px;

        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin .form-signin-heading {
        margin-bottom: 10px;
      }

	</style>
</head>

<body>


<g:set var="ocultarRodape" value="${true}" scope="request"/>

<g:if test='${flash.error}'>
    <div class='alert alert-error' style="margin-top:10px;">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        ${flash.error}
    </div>
</g:if>
<g:elseif test="${flash.message}">
    <div class='alert alert-info' style="margin-top:10px;">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        ${flash.message}
    </div>
</g:elseif>

<hr>

<div class="row">

  <div class="col-md-4 col-sm-4">
  </div>  

  <div class="col-md-4 col-sm-4">
    <g:form action="pwdRecovery" controller="home">

        <h4 class="form-signin-heading"> Informe seu email: </h4>

        <div class="form-group">
          <input type='text' class="form-control" value="${email}" 
            style="margin-top:10px;"  placeholder="Seu e-mail aqui" name='email'/>
        </div>
                        
        <a href="${createLink(uri:'/',absolute:true)}" class="btn btn-default"> Voltar</a>
        <button class="btn btn-large btn-primary" type="submit">Enviar nova senha</button>
    </g:form>
  </div>

  <div class="col-md-4 col-sm-4">
  </div>
</div>

<hr>


</body>
</html>
