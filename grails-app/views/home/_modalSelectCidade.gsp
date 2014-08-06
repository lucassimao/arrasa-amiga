<%@ page import="br.com.arrasaamiga.Uf"%>

<div class="modal fade" id="myModal">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">

      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
        	<span aria-hidden="true">&times;</span><span class="sr-only">Fechar</span>
        </button>
        <h4 class="modal-title">Qual sua cidade?</h4>
      </div>

      <div class="modal-body">

          <div>
              <label style="font-weight:bold;"> Estado: </label>
              <g:select value="${ Uf.get(17).id }" id="myModal-select-uf" name="uf"
              		optionValue="nome" optionKey="id" from="${Uf.list()}" />
          </div>

          <div style="float:left;">
              <label style="font-weight:bold;"> Cidade: </label>
              <select name="cidade" id="myModal-select-cidade"></select>
          </div>

      </div>
      <div class="modal-footer">
     		<button class="btn btn-default" data-dismiss="modal">Cancelar</button>
    		<a href="#" id="btn-ok" class="btn btn-primary"> OK </a>	
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->	


<script type="text/javascript">
	$(function(){

		$("#myModal-select-uf").change(function(){
			var idUf = $(this).val();

			$.ajax({
				
				url: "${createLink(controller:'home',action:'getCidades',absolute:true)}",
				data: {'idUf': idUf},
				settings: {'cache':true}

			}).success(function( data, textStatus, jqXHR ) {

				$("#myModal-select-cidade").empty();

				$.each(data,function(index,objCidade){

					var nomeCidade = objCidade.nome;
					var idCidade = objCidade.id;
					
					var option = $("<option/>").text(nomeCidade).attr("value",idCidade);
					if (nomeCidade === 'Teresina'){
						$(option).attr('selected',true);
					}
					$("#myModal-select-cidade").append(option);

				});

				$("#myModal #select-cidade").change();
			
			}).fail(function(){
				window.location = 'http://www.arrasaamiga.com.br';
			});
		});

		$("#myModal #btn-ok").click(function(event){
			event.preventDefault();

			var idCidade = $("#myModal-select-cidade").val();
			var url = "${createLink(action:'comoComprar',controller:'home',absolute:true)}?";
			url += $.param({cidade: idCidade});

			window.location =  url  ;

		});

		$('#myModal').on('shown.bs.modal', function (e) {
			$("#myModal-select-uf").change();
		});

	});
</script>	