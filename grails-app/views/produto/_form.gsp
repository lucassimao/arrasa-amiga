<%@ page import="br.com.arrasaamiga.Produto" %>

<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'nome', 'error')} required">
	<label for="nome">
		Nome:
		<span class="required-indicator">*</span>
	</label>
	<g:textField style="width:400px;" name="nome" required="" value="${produtoInstance?.nome}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'descricao', 'error')} required">
	<label for="descricao">
		Descrição:
		<span class="required-indicator">*</span>
	</label>	
	<g:textArea name="descricao" style="width:400px;" cols="40" rows="5" maxlength="100000" required="" value="${produtoInstance?.descricao}"/>
</div>

<div class="fieldcontain" id="tagedit-keywords">
	<label>
		Palavras-Chave:
	</label>
	<g:if test="${produtoInstance.keywords}">
		<g:each in="${produtoInstance.keywords}" var="keyword" status="idx">
			<input type="text" style="" name="palavrasChave[${idx}-a]" value="${keyword}" class="tag" >
		</g:each>
	</g:if>
	<g:else>
		<input type="text" class="tag" style="" name="palavrasChave[]" value="">
	</g:else>
</div>


<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'marca', 'error')}">
	<label for="marca">
		Marca:
	</label>
	<g:textField style="width:400px;" name="marca" value="${produtoInstance?.marca}"/>
</div>

<div class="fieldcontain" id="tagedit-grupos">
	<label>
		Grupos:
	</label>
	<g:if test="${produtoInstance.grupos}">
		<g:each in="${produtoInstance.grupos}" var="grupo">
			<input type="text" style="" name="_grupos[${grupo.id}-a]" value="${grupo.nome}" class="tag" >
		</g:each>
	</g:if>
	<g:else>
		<input type="text" style="" name="_grupos[]" value="" >
	</g:else>
</div>


<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'visivel', 'error')} required">
	<label for="visivel">
		Visível:
		<span class="required-indicator">*</span>
	</label>	
	<g:checkBox name="visivel" value="true" checked="${produtoInstance?.visivel}"  />

</div>


<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'precoAVistaEmReais', 'error')} required">
	<label for="precoAVistaEmReais">
		Valor Unitário A Vista
		<span class="required-indicator">*</span>
	</label>
	<g:field name="precoAVistaEmReais"  min="0" value="${produtoInstance.precoAVistaEmReais.toString().replace('.',',')}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'precoAPrazoEmReais', 'error')} required">
	<label for="precoAPrazoEmReais">
		Valor Unitário A Prazo
		<span class="required-indicator">*</span>
	</label>
	<g:field name="precoAPrazoEmReais"  min="0" value="${produtoInstance.precoAPrazoEmReais.toString().replace('.',',')}" required=""/>
</div>


<div class="fieldcontain ${hasErrors(bean: produtoInstance, field: 'tipoUnitario', 'error')} required">
	<label for="tipoUnitario">
		Tipo Unitario:
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="tipoUnitario" required="" value="${produtoInstance?.tipoUnitario}"/>
</div>


<div>

	<ul style="margin-left:26%;list-style-type: none;margin-top:25px;" id="list-unidades">
		<g:each in="${produtoInstance.unidades}" var="unidade">
			<g:set var="fotos" value="${produtoInstance.fotos.findAll{f-> f.unidade.equals(unidade) } }"/>
			<g:render template="addNewUnidade" model="['unidade':unidade,'fotos': fotos ]"/>			
		</g:each>
	</ul>
	
</div>

<div class="fieldcontain" style="margin-top:20px;border">
	<label></label>

	<assets:image file="plus.png" style="width:12px;"/>
	<a id="btn-Add-Unidade" href="#">  Adicionar Unidade </a>
</div>




<div class="fieldcontain required">
	<label for="fotoMiniatura">
		Foto Miniatura
		<span class="required-indicator">*</span>
	</label>
	<g:field type="file" name="fotoMiniaturaFile"/>
	
</div>

<asset:stylesheet href="tagedit.manifest.css"/>
<asset:javascript src="crud.produtos.manifest.js"/>


<script type="text/javascript">

	$(function(){
	
		$("#tagedit-keywords input").tagedit({
			autocompleteURL: "${createLink(action:'getTags',controller:'produto',absolute:true)}",
			additionalListClass: 'keywords'
		});

		
		var localJSON = JSON.parse('${raw(gruposDeProdutos)}');

		$("#tagedit-grupos input").tagedit({
			autocompleteOptions:{
				source:localJSON					
			},
			allowAdd:false,
			allowEdit:false,
			additionalListClass: 'keywords'
		});


		// reconstruindo modelo de dados
		<g:each in="${produtoInstance.unidades}" status="idx" var="unidade">
			model.adicionarUnidade("${unidade}");

			$("#list-unidades li:nth-child(${idx+1})").data('unidade',"${unidade}");
			
			<g:each in="${produtoInstance.fotos.findAll{f-> f.unidade.equals(unidade)}}" var="foto"> 
				$("#list-unidades li").eq(${idx}).children("div.produto-unidade-foto").eq(${foto.posicao}).data('nomeArquivo',"${foto.arquivo}");

				model.addFoto("${unidade}","${foto.arquivo}");
				model.setComentario("${foto.arquivo}","${foto.comentario}");
			</g:each>

			updateUpDownArrows( $("#list-unidades li:nth-child(${idx+1})") );
			
		</g:each>


		$("form").submit(function(){

			var inputUnidades = $("<input type='hidden' name='unidades'/>");
			$(inputUnidades).val(model.getUnidadesAsJSON());

			$(this).append(inputUnidades);

			var inputFotosUnidades = $("<input type='hidden' name='fotosUnidades'/>");
			$(inputFotosUnidades).val(model.getFotosUnidadesAsJSON());

			$(this).append(inputFotosUnidades);

			var inputFotoComentario = $("<input type='hidden' name='fotoComentario'/>");
			$(inputFotoComentario).val(model.getFotoComentarioAsJSON());
			$(this).append(inputFotoComentario);

			return true;

		});


		$("#btn-Add-Unidade").click(function(event){	

			event.preventDefault();

			var unid = prompt("Nome da unidade","");

			if (!model.contains(unid)){

				adicionarUnidade(unid);
							
			}else{

				alert("Unidade ja foi adicionada!");
			}
			

		});

		$("body").on('click','.label-excluir-unidade', function(){

			if (confirm("Deseja realmente excluir essa unidade assim como as fotos associadas ?")){

				var li = $(this).parents('li');
				var unidade = $(li).data('unidade');

				model.removerUnidade(unidade);
				$(li).remove();


			}
		});		


		$("body").on('click','.btn-Adicionar-Foto a',function(event){

			event.preventDefault();
			var li = $(this).parents('li');
			var unidade = $(li).data('unidade');


			var inputFile = $("<input type='file' name='foto'/>");
			
			var form = $("<form enctype='multipart/form-data' method='POST' action='${createLink(action:'asyncFotoUpload',controller:'produto',absolute:true)}'></form>");

			$(form).append(inputFile);

			$(inputFile).change(function(){

				$(form).ajaxSubmit({

					data: { },

					success: function(responseText){

						var divFoto = $(responseText);
						
						var imgHtmlElement = $(divFoto).children('.produto-foto');

						var imagemSrc = $(imgHtmlElement).attr('src'); 
						var nomeArquivo = imagemSrc.substr(imagemSrc.lastIndexOf('/') + 1); 
						$(divFoto).data('nomeArquivo',nomeArquivo);
						

						model.addFoto(unidade,nomeArquivo);
						$(li).children('.btn-Adicionar-Foto').before( divFoto );

						updateUpDownArrows(li);	

					},

					error: function(){
						alert("Não foi possível adicionar a foto ...");
					}
				});

			});

			$(inputFile).click();


		});



		$("body").on("click",".btn-Excluir-Foto",function(){
			
			if (confirm("Deseja realmente excluir essa foto ?")){

				var divFoto = $(this).parents(".produto-unidade-foto");
				var nomeArquivo = $(divFoto).data('nomeArquivo');

				var li = $(divFoto).parents('li');
				var unidade = $(li).data('unidade');

				model.removeFoto(unidade,nomeArquivo);

				$(divFoto).remove();

				updateUpDownArrows(li);

			}
		});


		$("body").on("click",".imagem-up",function(){
			
			var divFoto = $(this).parents(".produto-unidade-foto");
			var nomeArquivo = $(divFoto).data('nomeArquivo');


			var li = $(this).parents('li');
			var unidade = $(li).data('unidade');

			if(model.upFoto(unidade,nomeArquivo)){
				
				var divFotoAcima = $(divFoto).prev();
				$(divFoto).detach();
				$(divFotoAcima).before(divFoto);

				updateUpDownArrows(li);

			}

		});

		$("body").on("click",".imagem-down",function(){
			var divFoto = $(this).parents(".produto-unidade-foto");
			var nomeArquivo = $(divFoto).data('nomeArquivo');


			var li = $(this).parents('li');
			var unidade = $(li).data('unidade');

			if(model.downFoto(unidade,nomeArquivo)){
				var divFotoAbaixo = $(divFoto).next();
				$(divFoto).detach();
				$(divFotoAbaixo).after(divFoto);

				updateUpDownArrows(li);
			}


		});

		$("body").on("click",".btn-Editar-Descricao",function(){
		     
		     var divFoto = $(this).parents(".produto-unidade-foto");
		     var nomeArquivo = $(divFoto).data('nomeArquivo');


		     $(this).prev('p').editable(function(value,settings){

		     	model.setComentario(nomeArquivo,value);
		     	return(value);
		     
		     },{ 
		         type      : 'textarea',
		         cancel    : 'Cancelar',
		         submit    : 'Salvar',
		     });

		     // forçando exibição do campo de texto
		     $(this).prev('p').click();


		});


		function adicionarUnidade(unidade,fotos){
			
			$.ajax({
					
				url: "${createLink(controller:'produto',action:'addNewUnidade',absolute:true)}",
				data: { 'unidade': unidade },
				settings: {'cache': false}

			}).success(function( data, textStatus, jqXHR ) {

				model.adicionarUnidade(unidade);
				
				var listItem = $(data);
				$(listItem).data("unidade",unidade);

				$("#list-unidades").append( listItem );


			}).fail(function(){
				alert("Não foi possível adicionar a unidade ...");
			});				
			
		}

	});


</script>

