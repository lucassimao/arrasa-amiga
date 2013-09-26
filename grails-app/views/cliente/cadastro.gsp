<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title>
	<style type='text/css' media='screen'>

      .form-signin {
        max-width: 100%;
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
      .form-signin input{
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }

      #dataNascimento_month{
          width: 100px !important;
      }

      #dataNascimento_day,#dataNascimento_year {
           width: 70px !important;
      }

	</style>
</head>

<body>
  
  <g:set var="ocultarRodape" value="${true}" scope="request"/>

  <g:hasErrors bean="${cliente}">
  	<div class='alert alert-error' style="margin-top:10px;">
  		<button type="button" class="close" data-dismiss="alert">&times;</button>
      <i class="icon-edit"></i>
  		Amiga, tem umas informações que você esqueceu de preencher :-)
  	</div>
  </g:hasErrors>

  <g:form action='salvarNovoCliente'  id='form' class="form-signin">
    <fieldset>
        <legend style="font-weight:bold;"> Dados Pessoais</legend>

          <div class="control-group ${hasErrors(bean: cliente, field: 'nome', 'error')}">
              <label style="font-weight:bold;"> Nome: </label>
              <input type="text" value="${cliente.nome}" id="nome" name="nome" placeholder="Nome completo …" class="input-xxlarge" required>

              <g:eachError bean="${cliente}" field="nome">
                  <span class="help-inline" style="padding-bottom:10px;"> <g:message error="${it}" /></span>
              </g:eachError>
            
          </div>

          <div class="control-group ${hasErrors(bean: cliente, field: 'telefone', 'error')} ${hasErrors(bean: cliente, field: 'dddTelefone', 'error')}">
              <label style="font-weight:bold;"> Telefone: </label>

              
              <input type="text" value="${cliente.dddTelefone}" placeholder="DDD" name="dddTelefone" maxlength="2" class="input-mini" required>
              <input type="text" value="${cliente.telefone}" placeholder="numero" name="telefone" maxlength="9" class="input-large" required>

              <g:eachError bean="${cliente}" field="telefone">
                  <span class="help-inline" style="padding-bottom:10px;"> 
                      <g:message error="${it}" />
                  </span>
              </g:eachError>

              <g:if test="${ !hasErrors(bean:cliente)}">
                <g:eachError bean="${cliente}" field="dddTelefone">
                    <span class="help-inline" style="padding-bottom:10px;"> 
                        <g:message error="${it}" />
                    </span>
                </g:eachError>
              </g:if>
          </div>

          <div class="control-group ${hasErrors(bean: cliente, field: 'celular', 'error')} ${hasErrors(bean: cliente, field: 'dddCelular', 'error')}">
              <label style="font-weight:bold;"> Celular: </label> 

              <input type="text" value="${cliente.dddCelular}" placeholder="DDD" maxlength="2" name="dddCelular" class="input-mini" required>
              <input type="text" value="${cliente.celular}" placeholder="numero" maxlength="9" name="celular" class="input-large" required>

              <g:eachError bean="${cliente}" field="celular">
                  <span class="help-inline" style="padding-bottom:10px;"> 
                      <g:message error="${it}" />
                  </span>
              </g:eachError>

              <g:if test="${ !hasErrors(bean:cliente)}">
                <g:eachError bean="${cliente}" field="dddCelular">
                    <span class="help-inline" style="padding-bottom:10px;"> 
                        <g:message error="${it}" />
                    </span>
                </g:eachError>
              </g:if>

          </div>

          <div style="margin-bottom:50px;" class="control-group ${hasErrors(bean: cliente, field: 'dataNascimento', 'error')}" >
              <label style="font-weight:bold;"> Data de Nascimento: </label>
              <g:datePicker value="${cliente.dataNascimento}" 
                            name="dataNascimento" precision="day"/>

              <g:eachError bean="${cliente}" field="dataNascimento">
                  <span class="help-inline" style="padding-bottom:10px;"> 
                      <g:message error="${it}" />
                  </span>
              </g:eachError>
          </div>


        <legend style="font-weight:bold;"> Endereço para entregas </legend>

          <div class="control-group ${hasErrors(bean: cliente, field: 'endereco.cep', 'error')}">
              
              <label style="font-weight:bold;"> CEP: </label>
              <input name="endereco.cep" value="${cliente?.endereco?.cep}" type="text" required>

              <g:eachError bean="${cliente}" field="endereco.cep">
                  <span class="help-inline" style="padding-bottom:10px;"> 
                      <g:message error="${it}" />
                  </span>
              </g:eachError>

          </div>

          <div class="control-group ${hasErrors(bean: cliente, field: 'endereco.uf', 'error')}">
              <label style="font-weight:bold;"> Estado: </label>
              <g:select name="endereco.uf" value="${cliente?.endereco?.uf?.key}" optionKey="key" from="${br.com.arrasaamiga.Uf.values()}" />

              <g:eachError bean="${cliente}" field="endereco.uf">
                  <span class="help-inline" style="padding-bottom:10px;"> 
                      <g:message error="${it}" />
                  </span>
              </g:eachError>
          </div>

          <div class="control-group ${hasErrors(bean: cliente, field: 'endereco.cidade', 'error')}">
              <label style="font-weight:bold;"> Cidade: </label>
              <input name="endereco.cidade" value="${cliente?.endereco?.cidade}" type="text" required >

              <g:eachError bean="${cliente}" field="endereco.cidade">
                  <span class="help-inline" style="padding-bottom:10px;"> 
                      <g:message error="${it}" />
                  </span>
              </g:eachError>
          </div>

          <div class="control-group ${hasErrors(bean: cliente, field: 'endereco.bairro', 'error')}">
              <label style="font-weight:bold;"> Bairro: </label>
              <input name="endereco.bairro"  value="${cliente?.endereco?.bairro}" type="text" required >

              <g:eachError bean="${cliente}" field="endereco.bairro">
                  <span class="help-inline" style="padding-bottom:10px;"> 
                      <g:message error="${it}" />
                  </span>
              </g:eachError>
          </div>


          <div class="control-group ${hasErrors(bean: cliente, field: 'endereco.endereco', 'error')}">
              <label style="font-weight:bold;"> Endereço: </label>
              <input class="input-xxlarge" value="${cliente?.endereco?.endereco}" name="endereco.endereco" type="text" required >

              <g:eachError bean="${cliente}" field="endereco.endereco">
                  <span class="help-inline" style="padding-bottom:10px;"> 
                      <g:message error="${it}" />
                  </span>
              </g:eachError>
          </div>

          <label> Complemento: </label>
          <input class="input-xxlarge" value="${cliente?.endereco?.complemento}" placeholder="casa, apartamento, número ... "  name="endereco.complemento" type="text" >

          <label> Ponto de Referência: </label>
          <input style="margin-bottom:50px;" class="input-xlarge" placeholder="da uma dica amiga ..." value="${cliente?.endereco?.pontoDeReferencia}" name="endereco.pontoDeReferencia" type="text" >


        <legend style="font-weight:bold;"> Dados da Conta </legend>

          <div class="control-group ${hasErrors(bean: cliente, field: 'email', 'error')}">
              <label style="font-weight:bold;"> E-mail: </label>
              <input class="input-xlarge" value="${cliente.email}" name="email" type="text" required >

              <g:eachError bean="${cliente}" field="email">
                  <span class="help-inline" style="padding-bottom:10px;"> 
                      <g:message error="${it}" />
                  </span>
              </g:eachError>

          </div>

          <div class="control-group ${hasErrors(bean: cliente, field: 'senha', 'error')}">
              <label style="font-weight:bold;"> Senha: </label>
              <input class="input-large" name="senha" type="password" required>

              <g:eachError bean="${cliente}" field="senha">
                  <span class="help-inline" style="padding-bottom:10px;"> 
                      <g:message error="${it}" />
                  </span>
              </g:eachError>
          </div>

          
          <div class="form-actions">
            <button type="submit" class="btn btn-primary"> Enviar </button>
            <button type="button" class="btn">Cancelar </button>
          </div>

    </fieldset>
  </g:form>


  <g:javascript>
    $(function() {
        var input =  $(".error:first input");

        if (input.length == 0)
            $("#nome").focus();
        else
            input.focus();
    
    });
  </g:javascript>
</body>
</html>
