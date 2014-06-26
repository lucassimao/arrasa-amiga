<div style="height:80px;margin-top:10px;padding:5px;" class="produto-unidade-foto">


	<div style="position:relative;margin-right:5px;width:16px;height:70px;float:left;">
		<asset:image class="imagem-up" style="display:none;position:absolute;cursor:pointer;width:15px;top:20%;" src="arrow-up.png"/>
		<asset:image class="imagem-down" style="display:none;position:absolute;cursor:pointer;width:15px;bottom:20%;" src="arrow-down.png"/>
	</div>


	<asset:image src="produtos/${foto.arquivo}" class="produto-foto" style="margin-right:5px;border:1px solid blue;width:70px;height:70px;float:left;"/>

	<div class="comentario" style="position:relative;height:70px;overflow: hidden;">
		<p> ${ (foto.comentario)?:'Forneça uma descrição para essa foto'} </p>
		<asset:image class="btn-Editar-Descricao" src="edit.png" style="cursor:pointer;position:absolute;width:15px;bottom:0px;right:20px;"/>
		<asset:image class="btn-Excluir-Foto" src="delete.png" style="cursor:pointer;position:absolute;width:15px;bottom:0px;right:0px;"/>
	</div>

</div>	