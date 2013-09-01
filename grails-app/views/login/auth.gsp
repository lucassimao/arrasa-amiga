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
      .form-signin .form-signin-heading,
      .form-signin .checkbox {
        margin-bottom: 10px;
      }
      .form-signin input[type="text"],
      .form-signin input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }

	</style>
</head>

<body>


<g:set var="ocultarRodape" value="${true}" scope="request"/>

<g:if test='${flash.message}'>
	<div class='alert alert-error' style="margin-top:10px;">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		${flash.message}
	</div>
</g:if>

<form action='${postUrl}'  method='POST' id='loginForm' class="form-signin">
        <h4 class="form-signin-heading"> Já possui uma conta na Arrasa Amiga?</h4>
        <small> Para continuar, digite seu email e senha: </small>
       
        <input type='text' class="input-block-level" style="margin-top:10px;"
        		placeholder="Seu e-mail aqui" name='j_username' id='username'/>

        <input type='password' placeholder="Sua senha aqui" 
        		class='input-block-level' name='j_password' id='password'/>


          <button class="btn btn-large btn-primary" type="submit">Entrar</button>


        <p style="text-align:center;margin-top:15px;">
    			<a id="pop" title="Psiu!" 
    			data-content="Clica aqui se essa for sua primeira vez na Arrasa Amiga :-)" 
    			data-placement="right" data-toggle="popover"  
    			href="${createLink(controller:'cliente',action:'cadastro')}" data-delay="500" data-animation="animation"
    			data-html="true">Ainda não criou sua conta amiga ? </a>
        </p>
</form>


<g:javascript>

  $(function () {
        $('body').popover({
            selector: '[data-toggle="popover"]'
        });

        $('body').tooltip({
            selector: 'a[rel="tooltip"], [data-toggle="tooltip"]'
        });

        $("#pop").popover('show');

        $("#username").focus();
    });

</g:javascript>
</body>
</html>
