<%@ page import="br.com.arrasaamiga.Produto" %>
<%@ page import="br.com.arrasaamiga.Aviso" %>

<!DOCTYPE html>
<html>
	<head>
		<title> ${produtoInstance.nome}  </title>
		<parameter name="description" value="${produtoInstance.descricao}" />
		<parameter name="keywords" value="${produtoInstance.keywords?.join(',')}" />
		<parameter name="og:image" value="${assetPath(src:'produtos/'+produtoInstance.fotoMiniatura)}"/>
    
    <meta name="layout" content="main"/>  

    <script type="text/javascript">
        window.setTimeout(function() {
            $(".alert").fadeTo(500, 0).slideUp(500, function(){
                $(this).alert('close'); 
            });
        }, 3000);
    </script>

	</head>
	<body>


 <!-- begin:article -->
      <div class="row">

        <!-- begin:content -->
        <div class="col-md-12 col-sm-12 content">

          <div class="row">
            <!-- breadcrumb -->
            <div class="col-md-12">
                <ol class="breadcrumb" style="background-color:white;border:1px solid #ccc;">
                  <li><a href="${createLink(absolute:true,uri: '/' )}"> <i class="fa fa-home"></i> Home</a></li>
                  <g:each in="${produtoInstance.grupoPadrao?.ancestrais}" var="grupo" status="i">
                  	<li><a href="${createLink(absolute:true,uri: ('/produtos/' + grupo.nome) )}">${grupo.nome}</a></li>
              	  </g:each>
              	  <li class="active">
              	  	<a href="${createLink(absolute:true,uri: ('/produtos/' + produtoInstance.grupoPadrao?.nome) )}"> ${produtoInstance.grupoPadrao?.nome}</a>
              	  </li>
                </ol>
            </div>
            <!-- end breadcumb -->
          </div>

          <div class="row">

            <div class="col-md-12">

              <g:if test='${flash.message}'>
                <div class='alert alert-info'>
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  ${flash.message}
                </div>
              </g:if>

              <g:if test='${flash.info}'>
                <div class='alert alert-warning'>
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <i class="icon-ok"> </i> ${flash.info}
                </div>
              </g:if>
                

               <!-- begin:product-image-slider-->
                <div class="col-md-7 col-sm-7">
                  <div id="product-single" class="carousel slide" data-ride="carousel">

                    <div class="carousel-inner">
          						<g:each in="${produtoInstance.fotos}" var="foto" status="i">
          							<div class="item ${ (i == 0)?'active':'' }">  
          								 <div class="product-single">
          									<asset:image id="img${foto.id}" src="produtos/${foto.arquivo}" class="img-responsive"/>
                            <div class="carousel-caption">
                                <h3> ${foto.comentario} </h3>
                            </div>
          								 </div>
          							</div>
          						</g:each>

                    </div>

                    <a class="left carousel-control" href="#product-single" role="button" data-slide="prev">
                      <!-- <i class="fa fa-angle-left"></i> -->
                      <span class="glyphicon glyphicon-chevron-left"></span>
                    </a>
                    <a class="right carousel-control" href="#product-single" data-slide="next">
                      <!--<i class="fa fa-angle-right"></i>-->
                      <span class="glyphicon glyphicon-chevron-right"></span>
                    </a>
                  </div>
                </div>
                 <!-- end:product-image-slider -->

                <!-- begin:product-specification -->
                <div class="col-md-5 col-sm-5">
                  <div class="single-desc">

                    <g:form  name="form-produto" action="add" controller="shoppingCart" method="post" id="${produtoInstance.id}">
                      
                      <table>
                        <tbody>

                            <tr>
                                <td colspan="3" > 
                                  <div class="heading-title" >
                                      <h2> <span>${produtoInstance.nome}</span> </h2>
                                  </div>
                                </td>
                            </tr>

                            <tr>
                                <td colspan="3"> 
                                  <h5> ${produtoInstance.marca} </h5>                                  
                                </td>
                            </tr>

                            <tr>
                                <td colspan="3"> 
                                    <span class="price">
                                        <g:formatNumber number="${produtoInstance.precoAPrazoEmReais}" 
                                        type="currency" currencyCode="BRL" /> 
                                    </span>

                                    <p>
                                      <small> 
                                      Em até 5x de R$ 
                                      ${formatNumber(number:produtoInstance.precoAPrazoEmReais/5,type:'currency',currencyCode:'BRL')}
                                      no cartão  
                                      </small>
                                    </p>

                                    <g:if test="${produtoInstance.precoAVistaEmCentavos < produtoInstance.precoAPrazoEmCentavos }">                       
                                      <p><small> * Desconto à vista </small></p>     
                                    </g:if>

                                    <div class="fb-like" style="float:right;margin-right:1px;" 
                                          data-href="${createLink(uri:produtoInstance.nomeAsURL, absolute:true)}" data-width="100px" 
                                          data-colorscheme="light" data-layout="button_count" data-action="recommend" 
                                          data-show-faces="true" data-send="true"></div>                                                               
                                </td>
                            </tr>

                            <tr>
                                <td colspan="3"> 
                                  <hr style="margin:10px 0;">
                                </td>
                            </tr>                            


                          <g:if test="${produtoInstance.isMultiUnidade()}">
                              
                              <tr>
                                  <td><strong>${produtoInstance.tipoUnitario} :</strong></td>
                                  <td colspan="2">
                                      <select class="form-control" id="select-unidade" name="unidade"> 
                                        <g:each in="${unidades}" var="unidade" status="i">
                                          <option value="${raw(unidade)}" ${ (i==0)?'selected':''}>${raw(unidade)}</option>
                                        </g:each>
                                      </select>
                                  </td>
                              </tr>

                              <tr class="tr-quantidade">
                                  <td><strong>Quantidade :</strong></td>
                                  <td colspan="2">
                                    <select class="form-control" id="select-quantidade" name="quantidade"/> 
                                  </td>
                              </tr>

                                            
                              <tr>
                                  <td colspan="3">
                                      <p id="msg-estoque-esgotado" style="display:none;"> 
                                        Ow Amiga, ${produtoInstance.tipoUnitario} 
                                        <span style="font-weight:bold;" id="unidade"></span>
                                        está em falta
                                      </p>   

                                      <p style="font-size:14px;color:blue;display:none;" id="aviso-ativado">
                                        Avisaremos você assim que novas unidades chegarem 
                                      </p>   
                                                                         
                                      <p id="ativar-aviso">
                                        <a data-toggle="modal" style="cursor:pointer;" data-target="#modal">
                                          Quer saber assim que chegar ?
                                        </a>
                                      </p>     
                                  </td>  
                              </tr>  

                              <tr id="tr-comprar">
                                  <td colspan="3">
                                    <a href="#" id="btn-comprar" class="btn btn-sm btn-primary">Adicionar ao Carrinho</a>
                                  </td>  
                              </tr>            

                          </g:if>
                          <g:else>

                              <g:set var="estoque" value="${estoques[0]}"/>
                              <g:hiddenField id="unidade" name="unidade" value="${estoque.unidade}" />

                              <g:if test="${estoque.quantidade > 0}">

                                  <tr class="tr-quantidade">
                                    <td><strong>Quantidade</strong></td>
                                    <td>:</td>
                                    <td>
                                        <g:select from="${1..(estoque.quantidade)}" class="form-control" id="select-quantidade" name="quantidade"/> 
                                    </td>
                                  </tr>

                                  <tr id="tr-comprar">
                                      <td colspan="3">
                                        <a href="#" id="btn-comprar" class="btn btn-sm btn-primary">Adicionar ao Carrinho</a>
                                      </td>  
                                  </tr>                                  
                              
                              </g:if>
                              <g:else>

                                  <tr>
                                      <td colspan="3">

                                        <h5> 
                                          Ow Amiga, estamos aguardando novas unidades !! 
                                        </h5> 

                                        <p style="font-size:14px;color:blue;display:none;" id="aviso-ativado">
                                          Avisaremos você assim que novas unidades chegarem 
                                        </p>   
                                                                           
                                        <p id="ativar-aviso">
                                          <a data-toggle="modal" style="cursor:pointer;" data-target="#modal">
                                            Quer saber assim que chegar ?
                                          </a>
                                        </p> 

                                      </td>  
                                  </tr>

                              </g:else>

                          </g:else>

                        </tbody>
                      </table>


                    </g:form>

                  </div>
                </div>
                <!-- end:product-specification -->
              </div>
            </div>
              <!-- break -->

              <!-- begin:product-detail -->
              <div class="row">
                <div class="col-md-12 content-detail">
                    <ul id="myTab" class="nav nav-tabs">
                      <li class="active"><a href="#desc" data-toggle="tab">Descrição</a></li>
                      <!-- tag do facebook deve ser uma div -->
                      <li class=""><a href="#comentarios" data-toggle="tab"> <span class="fb-comments-count" data-href="${request.requestURL}"></span> Comentários </a></li>
                    </ul>

                    <div id="myTabContent" class="tab-content">
                      <div class="tab-pane fade active in" id="desc">
                        <p class="comentarios">${produtoInstance.descricao}</p>
                      </div>
                      <div class="tab-pane fade" id="comentarios">
                        <div class="fb-comments" data-width="100%" 
                            data-href="${request.requestURL}" data-colorscheme="light"></div>
                      </div>
                    </div>
                </div>
              </div>
              <!-- end:product-detail -->

              <!-- begin:related-product -->
              
              <%
                def produtosRelacionados = produtoInstance.getProdutosRelacionados(4)
              %>

              <g:if test="${produtosRelacionados}">
                  <div class="row">
                    <div class="col-md-12">
                      <div class="heading-title" style="margin-bottom:5px;">
                        <h2>Produtos relacionados <span> </span> <span class="text-yellow">.</span></h2>
                      </div>

                      <div class="row product-container" style="position: relative; height: 463.75px;">
                        
                        <g:each in="${produtosRelacionados}" var="pRelacionado">
                          
                          <div class="col-md-3 col-sm-6 col-xs-6">
                            <div class="thumbnail product-item">
                              <a href="${createLink(uri:pRelacionado.nomeAsURL, absolute:true)}">
                                <asset:image src="produtos/${pRelacionado.fotoMiniatura}" alt="${pRelacionado.nome}" title="${pRelacionado.nome}"/>
                              </a>
                              <div class="caption">
                                <h5>${pRelacionado.nome}</h5>
                                <p>  ${pRelacionado.marca} </p>
                                <p>
                                  <g:formatNumber number="${pRelacionado.precoAPrazoEmReais}" type="currency" currencyCode="BRL" />
                                </p>

                                <div class="buy-item"> 
                                  <a class="btn btn-primary" href="${createLink(uri:pRelacionado.nomeAsURL, absolute:true)}"> 
                                    <span><i class="fa fa-shopping-cart"></i> Comprar </span> 
                                  </a> 
                                </div>

                              </div>
                            </div>
                          </div>

                        </g:each>

                      </div>
                    </div>
                  </div>
              </g:if>
              <!-- end:related-product -->

        </div>
        <!-- end:content -->
      </div>
      <!-- end:article -->


      <g:render template="aviseme" model="[produtoInstance:produtoInstance]"/>

      <asset:script type="text/javascript">


        $(function(){

          
            <g:each in="${produtoInstance.fotos}" var="fotoProduto" status="i">
              $("#img${fotoProduto.id}").data('unidade',"${fotoProduto.unidade}");
            </g:each>

            $("#btn-comprar").click(function(event){
              event.preventDefault();
              $('#form-produto').submit();
            });

            $("#btn-delete-cart-item").click(function(event){
              event.preventDefault();
              $(this).parents('form').submit();
            });

            


            <g:if test="${produtoInstance.isMultiUnidade()}">

                $("#select-unidade").change(function(){

                  var unidade = $(this).val();

                  $(".carousel-inner img").each(function(index,img){

                    if ( $(img).data('unidade') === unidade ){

                      $(".carousel").carousel(index);
                      $(".carousel").carousel('pause');
                      return false;
                    }

                  });

                  $.ajax({
                      
                      url: "${createLink(controller:'produto',action:'quantidadeEmEstoque',absolute:true)}",
                      data: {'produtoId': ${produtoInstance.id} , 'unidade': unidade},
                      settings: {'cache':false}

                    }).success(function( data, textStatus, jqXHR ) {

                      var qtdeEmEstoque = parseInt(data.quantidade);

                      $("#select-quantidade").empty();
                      $("#msg-estoque-esgotado").hide();
                      $("#aviso-ativado").hide();
                      $("#ativar-aviso").hide();

                      if ( qtdeEmEstoque > 0 ){

                        $(".tr-quantidade").css('visibility','visible');
                        $("#tr-comprar").show();

                        // adicionando as quantidades
                        for(var count=1; count <= qtdeEmEstoque;++count){
                          var option = $("<option/>").text(count).attr("value",count);
                          $("#select-quantidade").append(option);
                        }

                      }else{

                      
                        $(".tr-quantidade").css('visibility','hidden');
                        $("#tr-comprar").hide();
                        $("#msg-estoque-esgotado #unidade").text(unidade);
                        $("#msg-estoque-esgotado").show({duration:250});

                        var marcadoParaAvisar = Boolean(data.marcadoParaAvisar);
                        
                        if (marcadoParaAvisar){
                          $("#aviso-ativado").show();
                          $("#ativar-aviso").hide();

                        }else{
                          $("#aviso-ativado").hide();
                          $("#ativar-aviso").show();

                        }

                      }


                    }).fail(function(){


                    });

                });

                // carregando estoque para o primeiro item da lista de unidades
                $("#select-unidade").change();

            </g:if>
            <g:else>

                var unidade = $("#unidade").val();

                $.ajax({
                  
                  url: "${createLink(controller:'produto',action:'quantidadeEmEstoque',absolute:true)}",
                  data: {'produtoId': ${produtoInstance.id} , 'unidade': unidade},
                  settings: {'cache':false}

                }).success(function( data, textStatus, jqXHR ) {

                  var qtdeEmEstoque = parseInt(data.quantidade);
                  var marcadoParaAvisar = Boolean(data.marcadoParaAvisar);

                  if ( qtdeEmEstoque == 0 ){

                    if (marcadoParaAvisar){
                      $("#aviso-ativado").show({duration:250});
                      $("#ativar-aviso").hide({duration:250});

                    }else{
                      $("#aviso-ativado").hide({duration:250});
                      $("#ativar-aviso").show({duration:250});

                    }

                    
                  }

                
                }).fail(function(){

                });

            </g:else>



        });

      </asset:script>


	</body>

</html>