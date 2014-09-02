//= require jquery
//= require jquery.form.min.js
//= require jquery.jeditable.js
//= require_self
//= require tagedit.manifest.js

var model =  (function(){
	
	var unidades = [];
	var fotosUnidades = {};
	var fotoComentario = {};



	return {

		getUnidadesAsJSON: function(){
			return JSON.stringify(unidades);
		},

		getFotosUnidadesAsJSON: function(){
			return JSON.stringify(fotosUnidades);
		},
		
		getFotoComentarioAsJSON: function(){
			return JSON.stringify(fotoComentario);
		},

		adicionarUnidade : function(str){
			unidades.push(str);
			fotosUnidades[str] = [];

		},


		removerUnidade : function(str){
			var index = $.inArray(str,unidades);

			if (index > -1){
				unidades.splice(index,1);
				delete fotosUnidades[str];

				$("#descricao").val();
			}
		},



		addFoto : function(unidade,nomeArquivo){
			fotosUnidades[unidade].push(nomeArquivo);

		},

		removeFoto : function(unidade,nomeArquivo){
			var index = $.inArray(nomeArquivo,fotosUnidades[unidade]);

			if (index > -1){
				fotosUnidades[unidade].splice(index,1);
			}

		},

		setComentario : function(nomeArquivo,comentario){
			fotoComentario[nomeArquivo] = comentario;
		},

		upFoto : function(unidade,foto){
			var index = $.inArray(foto,fotosUnidades[unidade]);

			if (index > 0){
				var idxAcima = index - 1
				var fotoAcima = fotosUnidades[unidade][idxAcima];

				fotosUnidades[unidade][idxAcima] = foto;
				fotosUnidades[unidade][index] = fotoAcima;

				return true;

			}

			return false;

		},

		downFoto: function(unidade,foto){
			var index = $.inArray(foto,fotosUnidades[unidade]);

			if (index < (fotosUnidades[unidade].length -1) ){
				var idxAbaixo = index + 1;
				var fotoAbaixo = fotosUnidades[unidade][idxAbaixo];

				fotosUnidades[unidade][idxAbaixo] = foto;
				fotosUnidades[unidade][index] = fotoAbaixo;

				return true;

			}

			return false;

		},

		contains : function(str){

			var rslt = null;

			$.each(unidades, function(index, value) {

			  if (rslt == null && value.toLowerCase() === str.toLowerCase()) {
			    rslt = index;
			    return false;
			  }
			
			});	

			return (rslt !== null)?true:false;		
		}


	}

})();	


function updateUpDownArrows(li){
	var qtdeFotos = $(li).children('.produto-unidade-foto').length;

	$(li).find('.imagem-up, .imagem-down').css('display','none');

	$(li).children('.produto-unidade-foto').each(function(index,divProdutoUnidade){


		if (index > 0 ){
			$(divProdutoUnidade).find('.imagem-up').toggle();
		}

		if (index < (qtdeFotos - 1)){
			$(divProdutoUnidade).find('.imagem-down').toggle();
		}

	});
}

