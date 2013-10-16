<div class="modal fade hide" id="modal"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">

	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h3 id="myModalLabel">  <i class="icon-envelope"></i> Cadastro para Contato </h3> 
	</div>

	<div class="modal-body"></div>
</div>

<g:javascript>


	$('#modal').on('hidden', function () {
		$(this).children(".modal-body").empty();
	});


	<%= 
		"""
			jQuery('#modal').on('show', function () {

				var unidade = '';

				
				if ( jQuery('input#unidade').length > 0 ){
					
					unidade = jQuery('input#unidade').val();

				}else{
					unidade = jQuery('#select-unidade').val();
				}

				unidade = unidade.replace(' ','.'); // bug do plugin, nao aceita espaço em branco

				var modalBody =  jQuery(this).children(".modal-body").first();

				var tag = '<fb:registration fields=[{name:"name"},{name:"email"},{name:"celular",type:"text",description:"DDD+Celular"},{name:"unidade",type:"hidden",description:"unidade",default:"';
				tag += unidade + '"},{name:"idProduto",description:"idProduto",type:"hidden",default:${produtoInstance.id}}] width="520px" redirect-uri="${createLink(controller:'produto',action:'salvarAviso',absolute:true)}"></fb:registration>'
				
				jQuery(modalBody).append(tag);

				FB.XFBML.parse(jQuery(modalBody).get(0));
			});
		"""
	%>

</g:javascript>