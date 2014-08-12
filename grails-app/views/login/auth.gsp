<html>
<head>
    <meta name='layout' content='main'/>

    <script type="text/javascript">
        window.setTimeout(function () {
            $(".alert").fadeTo(1000, 0).slideUp(500, function () {
                $(this).alert('close');
            });
        }, 3000);
    </script>

</head>

<body>

    <hr/>
    <g:if test="${flash.message}">
        <div class="alert alert-danger alert-dismissible">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            ${flash.message}
        </div>
    </g:if>

    <div class="well" style="background-color: #ffffff;">

        <div class="row">

            <div class="col-md-6 col-sm-6">
                <h3>Primeira vez por aqui ?</h3>
                <h5>Abra sua conta na Arrasa Amiga</h5>
                <hr/>
                <a href="${createLink(controller: 'cliente', action: 'cadastro', absolute: true)}" class="btn btn-primary">
                    Abrir uma conta
                </a>

            </div>

            <div class="col-md-6 col-sm-6">
                <h3>Já tem cadastro?</h3>
                <h5>Informe seu email e senha para continuar</h5>
                <hr/>

                <form role="form" action="${postUrl}" method='POST'>
                    <div class="form-group">
                        <label for="username">Email</label>
                        <input name='j_username' type="text" class="form-control" placeholder="Informe aqui seu email">
                    </div>

                    <div class="form-group">
                        <label for="password">Senha</label>
                        <input name='j_password' type="password" class="form-control" placeholder="Senha">
                    </div>
                    <button class="btn btn-large btn-primary" type="submit">Entrar</button>
                    <a href="${createLink(uri: '/pwdrecovery')}" style="float:right;">Esqueceu sua senha?</a><br><br>
                </form>
            </div>
        </div>
    </div>

</body>
</html>
