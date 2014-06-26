<li>

	<div style="padding:2px;border-bottom:1px solid black;position:relative;" class="unidade-header"> 
		<h4> ${unidade} </h4>
	
		<label style="position:absolute;cursor:pointer;bottom:0px;right:0px;" class="label-excluir-unidade">
			<g:img dir="img" file="delete.png" style="width:12px;"/> Excluir
		</label>

	</div>


	<g:if test="${fotos}">
		<g:each in="${fotos}" var="foto">
			<g:render template="addNewFoto" model="['foto': foto]"/>
		</g:each>
	</g:if> 				

	

	<div style="margin-top:10px;margin-left:21px;margin-bottom:20px;" class="btn-Adicionar-Foto">
		<a href="#"> Adicionar Foto </a>
	</div>		

</li>