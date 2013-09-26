<div style="height:80px;margin-top:10px;padding:5px;" class="produto-unidade-foto">


	<div style="position:relative;margin-right:5px;width:16px;height:70px;float:left;">
		<g:img dir="img" class="imagem-up" file="arrow-up.png" style="display:none;position:absolute;cursor:pointer;width:15px;top:20%;"/>
		<g:img dir="img" class="imagem-down" file="arrow-down.png" style="display:none;position:absolute;cursor:pointer;width:15px;bottom:20%;"/>						
	</div>


	<g:img dir="img/produtos" file="${foto}" class="produto-foto" style="margin-right:5px;border:1px solid blue;width:70px;height:70px;float:left;"/>

	<div class="comentario" style="position:relative;height:70px;overflow: hidden;">
		<p> ${ (comentario)?:'Forneça uma descrição para essa foto'} </p>
		<g:img dir="img" class="btn-Editar-Descricao" file="edit.png" style="cursor:pointer;position:absolute;width:15px;bottom:0px;right:20px;"/>
		<g:img dir="img" class="btn-Excluir-Foto" file="delete.png" style="cursor:pointer;position:absolute;width:15px;bottom:0px;right:0px;"/>
	</div>

</div>	