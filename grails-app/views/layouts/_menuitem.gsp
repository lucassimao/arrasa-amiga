<%@ page import="br.com.arrasaamiga.GrupoDeProduto" %>


<g:if test="${GrupoDeProduto.countByPai(grupo) == 0}">
  
  <!-- <%-- renderiza grupos que não possuem subGrupos --%> -->
  <li class="${ grupoRaiz?.equals(grupo.nome)?'active':''}"><a href="${createLink(absolute:true,uri: ('/produtos/' + grupo.nome) )}">${grupo.nome}</a></li>

</g:if>
<g:else>

  <g:if test="${isRoot}"> 
      <!-- <%-- renderiza Grupos que possuem subGrupos mas que nao tem pai, menus que ficam no topo de todos --%>-->

      <li class="dropdown ${grupoRaiz?.equals(grupo.nome)?'active':''}">
        <a href="#" data-toggle="dropdown" class="dropdown-toggle">${grupo.nome} <b class="caret"></b></a>
        <ul class="dropdown-menu" id="menu1">

          <li><a href="${createLink(absolute:true,uri: ('/produtos/' + grupo.nome) )}">Todos</a></li>
          <li class="divider"></li>
          <g:each in="${GrupoDeProduto.findAllByPai(grupo)}" var="subGrupo">
              <g:render template="/layouts/menuitem" model="[grupo:subGrupo,isRoot:false,grupoRaiz:grupoRaiz]"/>
          </g:each>

        </ul>
      </li>

  </g:if>
  <g:else>
      <!-- <%-- renderiza Grupos que possuem subGrupos mas que TEM grupo pai --%> -->

      <li class="${ grupoRaiz?.equals(grupo.nome)?'active':''}">
        <a href="#">${grupo.nome} <b class="caret"></b></a>
        <ul class="dropdown-menu sub-menu">
          <li><a href="${createLink(absolute:true,uri: ('/produtos/' + grupo.nome) )}">Todos</a></li>
          <li class="divider"></li>
          <g:each in="${GrupoDeProduto.findAllByPai(grupo)}" var="subGrupo">
              <g:render template="/layouts/menuitem" model="[grupo:subGrupo,isRoot:false,grupoRaiz:grupoRaiz]"/>
          </g:each>
        </ul>
      </li>

  </g:else>

</g:else>