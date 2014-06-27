<%@ page import="br.com.arrasaamiga.GrupoDeProduto" %>


<g:if test="${GrupoDeProduto.countByPai(grupo) == 0}">
  
  <li><a href="${createLink(absolute:true,uri: ('/produtos/' + grupo.nome) )}">${grupo.nome}</a></li>

</g:if>
<g:else>

  <g:if test="${isRoot}">

      <li class="dropdown">
        <a href="#" data-toggle="dropdown" class="dropdown-toggle">${grupo.nome} <b class="caret"></b></a>
        <ul class="dropdown-menu" id="menu1">

          <li><a href="${createLink(absolute:true,uri: ('/produtos/' + grupo.nome) )}">Todos</a></li>
          <li class="divider"></li>
          <g:each in="${GrupoDeProduto.findAllByPai(grupo)}" var="subGrupo">
              <g:render template="/layouts/menuitem" model="[grupo:subGrupo,isRoot:false]"/>
          </g:each>

        </ul>
      </li>

  </g:if>
  <g:else>

      <li>
        <a href="#">${grupo.nome} <b class="caret"></b></a>
        <ul class="dropdown-menu sub-menu">
          <li><a href="${createLink(absolute:true,uri: ('/produtos/' + grupo.nome) )}">Todos</a></li>
          <li class="divider"></li>
          <g:each in="${GrupoDeProduto.findAllByPai(grupo)}" var="subGrupo">
              <g:render template="/layouts/menuitem" model="[grupo:subGrupo,isRoot:false]"/>
          </g:each>
        </ul>
      </li>

  </g:else>

</g:else>