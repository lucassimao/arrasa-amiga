
<div class="modal fade hide" id="modal"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">

	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h3 id="myModalLabel">  <i class="icon-envelope"></i> Informações para Contato </h3> 
	</div>

	<g:form action="salvarAviso" name="form-salvarAviso" controller="produto">

		<g:hiddenField name="produto.id" value="${produtoInstance.id}"/>
		<g:hiddenField name="unidade" value=""/>

		<div class="modal-body">

            <fieldset>

                  <div class="control-group ${hasErrors(bean: aviso, field: 'nome', 'error')}">
                      <label style="font-weight:bold;"> Nome: </label>
                      <input type="text" name="nome" required placeholder="Nome completo …" class="span12">
                  </div>

                  <div class="control-group ${hasErrors(bean: aviso, field: 'email', 'error')}">
                      <label style="font-weight:bold;"> E-mail: </label>
                      <input class="input-large" required name="email" type="email" >
                  </div>

                  <div style="float:left;" class="control-group ${hasErrors(bean: aviso, field: 'dddTelefone', 'error')}">
                      <label style="font-weight:bold;"> Telefone: </label>
                      <input type="tel" required placeholder="DDD" name="dddTelefone" maxlength="2" class="input-mini" >

                  </div>

                  <div style="float:left;margin-right:10px;" class="control-group ${hasErrors(bean: aviso, field: 'telefone', 'error')}">
                      <input style="margin-top:25px;margin-left:5px;" type="tel" required value="${aviso?.telefone}" placeholder="numero" maxlength="9" name="telefone" class="input-small">
                  </div>                      

                  <div style="float:left;"  class="control-group ${hasErrors(bean: aviso, field: 'dddCelular', 'error')}">
                      <label style="font-weight:bold;"> Celular: </label> 
                      <input type="tel" required placeholder="DDD" maxlength="2" name="dddCelular" class="input-mini" >
                  </div>

                  <div  class="control-group ${hasErrors(bean: aviso, field: 'celular', 'error')}">
                      <input style="margin-top:25px;margin-left:5px;" required type="tel" placeholder="numero" maxlength="9" name="celular" class="input-small">
                  </div>

            </fieldset>

		</div>
	
		<div class="modal-footer">
			<button class="btn" data-dismiss="modal" aria-hidden="true">Cancelar</button>
			<button type="submit" class="btn btn-primary">Salvar</button>
		</div>

	</g:form>

</div>

<g:javascript>

	$("#form-salvarAviso").submit(function(event){

		<g:if test="${produtoInstance.isMultiUnidade()}">
			var unidade = $("#select-unidade").val();
		</g:if>
		<g:else>
			var unidade = $("#unidade").val();
		</g:else>

		$(this).children("#unidade").val(unidade);

	});

</g:javascript>