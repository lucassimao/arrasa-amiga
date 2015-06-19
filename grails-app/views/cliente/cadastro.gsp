<%@ page import="br.com.arrasaamiga.Uf" %>
<%@ page import="br.com.arrasaamiga.Cidade" %>

<html>
<head>
    <meta name='layout' content='main'/>
    <title>Cadastro</title>


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
                
                url: "${createLink(controller: 'home', action: 'getCidades', absolute: true)}",
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

  window.location = "${createLink(controller: 'cliente', action: 'cadastro', absolute: true)}"

              });


          });

          $("#select-uf").change();

        });
    </asset:script>
</head>

<body>

<g:set var="ocultarRodape" value="${true}" scope="request"/>


<g:hasErrors bean="${cliente}">
    <div class="alert alert-danger" style="margin-top:10px;">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <i class="icon-edit"></i>
        Amiga, você esqueceu de preencher algumas informações!
    </div>
</g:hasErrors>

<!-- begin:content -->
<div class="container">

    <!-- begin:article -->
    <div class="row">

        <!-- begin:content -->
        <div class="content">

            <div class="row">
                <div class="col-md-10" style="float: none; margin: 0 auto;">
                    <h2>Cadastro</h2>
                    <hr/>
                    <g:form class="form-horizontal" action='save'>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">Nome</label>

                            <div class="col-sm-9">
                                <input id="input-nome" type="text" value="${cliente.nome}" name="nome"
                                       class="form-control ${hasErrors(bean: cliente, field: 'nome', 'error')}"
                                       placeholder="Nome Completo">
                            </div>
                        </div>

                        <div class="form-group">

                            <label class="col-md-3 control-label">Telefone:</label>

                            <div class="col-md-4 col-xs-3" style="max-width:70px;padding-right: 5px;">
                                <input type="text" value="${cliente.dddTelefone}" name="dddTelefone"
                                       class="form-control ${hasErrors(bean: cliente, field: 'dddTelefone', 'error')}"
                                       placeholder="DDD"/>
                            </div>

                            <div class="col-md-6 col-xs-9" style="max-width: 140px;padding: 0px;">
                                <input type="text" value="${cliente.telefone}" name="telefone"
                                       class="form-control ${hasErrors(bean: cliente, field: 'telefone', 'error')}"
                                       placeholder="Número"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">Celular</label>

                            <div class="col-md-4 col-xs-3" style="max-width:70px;padding-right: 5px;">
                                <input type="text" value="${cliente.dddCelular}" name="dddCelular"
                                       style="max-width:50px;"
                                       class="form-control ${hasErrors(bean: cliente, field: 'dddCelular', 'error')}"
                                       placeholder="DDD"/>
                            </div>

                            <div class="col-md-6 col-xs-9" style="max-width: 140px;padding: 0px;">
                                <input type="text" value="${cliente.celular}" name="celular"
                                       class="form-control ${hasErrors(bean: cliente, field: 'celular', 'error')}"
                                       placeholder="Número"/>
                            </div>
                        </div>

                        <h3>Dados da Conta</h3>
                        <hr/>


                        <div class="form-group">
                            <label class="col-sm-3 control-label">Email</label>

                            <div class="col-sm-9">
                                <input type="email" value="${cliente.email}" name="email" placeholder="Email"
                                       class="form-control ${hasErrors(bean: cliente, field: 'email', 'error')}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">Senha</label>

                            <div class="col-sm-9">
                                <input type="password"
                                       class="form-control ${hasErrors(bean: cliente, field: 'senha', 'error')}"
                                       name="senha">
                            </div>
                        </div>

                        <h3>Endereço para entregas</h3>
                        <hr/>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">Estado</label>

                            <div class="col-sm-9">

                                <g:select class="form-control" value="${(cliente?.endereco?.uf?.id) ?: Uf.piaui.id}"
                                          id="select-uf"
                                          name="endereco.uf.id" optionValue="nome" optionKey="id"
                                          from="${br.com.arrasaamiga.Uf.list()}"/>

                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">Cidade</label>

                            <div class="col-sm-9">
                                <g:select class="form-control" value="${cliente?.endereco?.cidade?.id}"
                                          name="endereco.cidade.id" id="select-cidade" from="${[]}"/>
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-3 control-label">Bairro</label>

                            <div class="col-sm-9">
                                <input type="text"
                                       class="form-control ${hasErrors(bean: cliente.endereco, field: 'bairro', 'error')}"
                                       value="${cliente.endereco?.bairro}" name="endereco.bairro">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">Endereço</label>

                            <div class="col-sm-9">
                                <input type="text"
                                       class="form-control ${hasErrors(bean: cliente.endereco, field: 'complemento', 'error')}"
                                       value="${cliente.endereco?.complemento}" name="endereco.complemento">
                                <span class="help-block">Casa, quadra, apartamento, rua, número, ponto de referência ...</span>
                            </div>
                        </div>

                        <div class="form-group" id="div-cep">
                            <label class="col-sm-3 control-label">Cep</label>

                            <div class="col-sm-9">
                                <input type="text"
                                       class="form-control ${hasErrors(bean: cliente.endereco, field: 'cep', 'error')}"
                                       value="${cliente.endereco?.cep}" name="endereco.cep" style="max-width:100px;">
                            </div>
                        </div>


                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-9">
                                <button type="submit" class="btn btn-primary">Confirmar</button>
                                <a href="${createLink(uri: '/', absolute: true)}" class="btn btn-default">Cancelar</a>
                            </div>
                        </div>
                    </g:form>
                </div>
            </div>
        </div>
        <!-- end:content -->
    </div>
    <!-- end:article -->

</div>
<!-- end:content -->

</body>
</html>
