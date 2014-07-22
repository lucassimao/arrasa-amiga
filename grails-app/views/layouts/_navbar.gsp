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
      <a class="navbar-brand" href="#">Administração</a>
    </div>

    <div class="collapse navbar-collapse" id="clotheshop-navbar">
      <ul class="nav navbar-nav navbar-right">
        <!--<li><a href="${createLink(controller:'produto',absolute:true)}">ADMINISTRAÇÃO</a></li>
         <li class="active"><a href="#">USD</a></li> -->
        <li><a href="${createLink(controller:'produto',absolute:true)}">PRODUTOS</a></li>
        <li><a href="${createLink(controller:'venda',absolute:true)}">VENDAS</a></li>
        <li class="divider-vertical"></li>
        <li class="dropdown">
          <a data-toggle="dropdown" class="dropdown-toggle" href="#"> MAIS <b class="caret"></b></a>
          <ul class="dropdown-menu">
            <li><a href="${createLink(controller:'estoque',absolute:true)}">ESTOQUE</a></li>
            <li><a href="${createLink(controller:'aviso',absolute:true)}">AVISOS</a></li>
            <li class="divider"></li>
            <li><a href="${createLink(controller:'logout',absolute:true)}">SAIR</a></li>
          </ul>
        </li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div>
</nav>
<!-- end:navbar -->
