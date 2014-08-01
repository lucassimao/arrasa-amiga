<%@ page import="br.com.arrasaamiga.Uf" %>
<%@ page import="br.com.arrasaamiga.Cidade" %>

<html>
<head>
	<meta name='layout' content='main'/>
	<title>Atualização de Dados</title>


      <asset:script>

        $(function(){


          var input =  $(".error:first");

          if (input.length == 0)
              $("#input-nome").focus();
          else
              input.focus();      
        

          $("#select-cidade").change(function(){

            var cidadeId = Number($("#select-cidade option:selected").val());

            if ( cidadeId === ${Cidade.teresina.id} ){

              $("#div-cep").css('display','none');

            }else{

              $("#div-cep").css('display','block');

            } 

          });

          $("#select-uf").change(function(){
              var idUf = $(this).val();

              $.ajax({
                
                url: "${createLink(controller:'shoppingCart',action:'getCidades',absolute:true)}",
                data: {'idUf': idUf},
                settings: {'cache':true}

              }).success(function( data, textStatus, jqXHR ) {

                    $("#select-cidade").empty();

                    $.each(data,function(index,objCidade){

                        var nomeCidade = objCidade.nome;
                        var idCidade = Number(objCidade.id);

                        var option = $("<option/>").text(nomeCidade).attr("value",idCidade);
                        
                        <g:if test="${cliente.endereco?.cidade}">
                          if (idCidade === ${cliente?.endereco?.cidade?.id}){
                            $(option).attr('selected',true);
                          }
                        </g:if>
                        <g:else>
                          if (idUf == "${Uf.piaui.id}" && idCidade === ${Cidade.teresina.id}){
                              $(option).attr('selected',true);
                          }                        
                        </g:else>
                        
                        $("#select-cidade").append(option);

                    });

                    $("#select-cidade").change();

              
              }).fail(function(){
                  
                  window.location = "${createLink(controller:'cliente',action:'cadastro',absolute:true)}"

              });


          });

          $("#select-uf").change();

        });
      </asset:script>

    <style type='text/css' media='screen'>
      h5{
        border-bottom: 1px solid black;
        font-size: 14px;
        line-height: 22px;
        color: #444;
        font-weight: 600;
      }
    </style>

</head>

<body>


    <g:set var="ocultarMenu" value="${true}"  scope="request"/>
    <g:set var="ocultarRodape" value="${true}" scope="request"/>

      <div class="row">
        <!-- breadcrumb -->
        <div class="col-md-12">
            <ol class="breadcrumb" style="background-color:white;border:1px solid #ccc;">
              <li><a href="${createLink(absolute:true,uri: '/' )}"> <i class="fa fa-home"></i> Home</a></li>
              <li class="active">
                <a href="#"> Minha Conta </a>
              </li>
            </ol>
        </div>
        <!-- end breadcumb -->
      </div>  

      <div class="row">
        
        <div class="col-md-3 visible-xs">
            <h5> Menu </h5>
            <nav>
                <div class="btn-group">
                    <a href="${createLink(controller:'cliente',action:'index',absolute:true)}" class="btn ">Minha Conta</a>
                    <a href="${createLink(controller:'cliente',action:'favoritos',absolute:true)}" class="btn">Favoritos</a>
                    <a href="${createLink(controller:'cliente',action:'pedidos',absolute:true)}" class="btn">Meus Pedidos</a>
                    <a href="${createLink(controller:'cliente',action:'atualizarCadastro',absolute:true)}" class="btn btn-primary">Editar Cadastro</a>
                </div>
            </nav>
        </div>

        <div class="col-md-3 hidden-xs">
            <h5> Menu </h5>
            <nav>
                <ul class="nav nav-pills nav-stacked">
                    <li ><a href="${createLink(controller:'cliente',action:'index')}">Minha Conta</a></li>
                    <li><a href="${createLink(controller:'cliente',action:'favoritos')}">Favoritos</a></li>
                    <li><a href="${createLink(controller:'cliente',action:'pedidos')}">Meus Pedidos</a></li>
                    <li class="active"><a href="${createLink(controller:'cliente',action:'atualizarCadastro')}">Editar Cadastro</a></li>
                </ul>
            </nav>
        </div>

        <div class="col-md-9">

              <h5>Editar Cadastro</h5>

              <g:form class="form-horizontal" action='salvarNovoCliente'>

                <div class="form-group">
                  <label class="col-sm-2 control-label">Nome</label>
                  <div class="col-sm-9">
                    <input id="input-nome" type="text" value="${cliente.nome}" name="nome" 
                          class="form-control ${hasErrors(bean: cliente, field: 'nome', 'error')}" placeholder="Nome Completo">
                  </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">Telefone</label>
                    <div class="col-sm-9">
                        <div class="form-inline">
                            <input type="text"  value="${cliente.dddTelefone}" name="dddTelefone" 
                                  class="form-control ${hasErrors(bean: cliente, field: 'dddTelefone', 'error')}" style="max-width:50px;" placeholder="DDD"/>
                            <input type="text" value="${cliente.telefone}" name="telefone" 
                                  class="form-control ${hasErrors(bean: cliente, field: 'telefone', 'error')}" placeholder="Número"/>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">Celular</label>
                    <div class="col-sm-9">
                        <div class="form-inline">
                            <input type="text" value="${cliente.dddCelular}" name="dddCelular" style="max-width:50px;"
                                    class="form-control ${hasErrors(bean: cliente, field: 'dddCelular', 'error')}"   placeholder="DDD"/>
                            <input type="text" value="${cliente.celular}" name="celular" 
                                  class="form-control ${hasErrors(bean: cliente, field: 'celular', 'error')}" placeholder="Número"/>
                        </div>
                    </div>
                </div>
             
                <div class="form-group">
                  <label class="col-sm-2 control-label">Estado</label>
                  <div class="col-sm-9">

                    <g:select class="form-control" value="${ (cliente?.endereco?.uf?.id)?:Uf.piaui.id }" id="select-uf"
                      name="endereco.uf.id" optionValue="nome" optionKey="id" from="${br.com.arrasaamiga.Uf.list()}" />

                  </div>
                </div>

                <div class="form-group">
                  <label class="col-sm-2 control-label">Cidade</label>
                  <div class="col-sm-9">
                    <g:select class="form-control" value="${cliente?.endereco?.cidade?.id}" 
                                      name="endereco.cidade.id" id="select-cidade" from="${[]}" />                    
                  </div>
                </div>                

                
                <div class="form-group">
                  <label class="col-sm-2 control-label">Bairro</label>
                  <div class="col-sm-9">
                    <input type="text" class="form-control ${hasErrors(bean: cliente, field: 'endereco.bairro', 'error')}" 
                                value="${cliente.endereco?.bairro}" name="endereco.bairro">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">Endereço</label>
                  <div class="col-sm-9">
                    <input type="text" class="form-control ${hasErrors(bean: cliente, field: 'endereco.complemento', 'error')}" 
                          value="${cliente.endereco?.complemento}" name="endereco.complemento">
                    <span class="help-block">Casa, quadra, apartamento, rua, número, ponto de referência ... </span>
                  </div>
                </div>
                <div class="form-group" id="div-cep">
                  <label class="col-sm-2 control-label">Cep</label>
                  <div class="col-sm-9">
                    <input type="text" class="form-control ${hasErrors(bean: cliente, field: 'endereco.cep', 'error')}" 
                            value="${cliente.endereco?.cep}" name="endereco.cep" style="max-width:100px;">
                  </div>
                </div>


                <div class="form-group">
                  <div class="">
                    <button type="submit" class="btn btn-primary">Atualizar</button>
                    <a href="${createLink(uri:'/',absolute:true)}" class="btn btn-default">Cancelar</a>
                  </div>
                </div>
              </g:form>


        </div>


      </div>        


</body>
</html>
