<!-- begin:navbar -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#clotheshop-navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${createLink(controller: 'produto')}">Administração</a>
        </div>

        <div class="collapse navbar-collapse" id="clotheshop-navbar">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="${createLink(controller: 'produto')}">PRODUTOS</a></li>
                <li><a href="${createLink(controller: 'venda',action:'index')}">VENDAS</a></li>
                <li class="divider-vertical"></li>
                <li class="dropdown">
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#">MAIS <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="${createLink(controller: 'estoque',action:'index')}">ESTOQUE</a></li>
                        <li><a href="${createLink(controller: 'banner')}">BANNERS</a></li>
                        <li><a href="${createLink(controller: 'aviso')}">AVISOS</a></li>
                        <li><a href="${createLink(controller: 'feriado')}">FERIADOS</a></li>
                        <li class="divider"></li>
                        <li><a href="${createLink(controller: 'logout')}">SAIR</a></li>
                    </ul>
                </li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div>
</nav>
<!-- end:navbar -->
