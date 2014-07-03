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
	</head>
	<body>


 <!-- begin:article -->
      <div class="row">
        <!-- begin:sidebar -->
        <div class="col-md-3 col-sm-4 sidebar">
          <div class="row">
            <div class="col-md-12">
              <div class="widget">
                <div class="widget-title">
                  <h3> Meu Carrinho </h3>
                </div>
                <ul class="cart list-unstyled">
                  <g:each in="${shoppingCart.itens}" var="item">
                    <li>
                      <div class="row">
                        <div class="col-sm-7 col-xs-7">
                          ${item.quantidade} <a href="${createLink(uri:item.produto.nomeAsUrl,absolute:true)}">${item.produto.nome}</a> <span>[ ${item.unidade} ]</span></div>
                        <div class="col-sm-5 col-xs-5 text-right"><strong>${item.subTotalAPrazo}</strong> <a href="#"><i class="fa fa-trash-o"></i></a></div>
                      </div>
                    </li>
                  </g:each>
                </ul>
                <ul class="list-unstyled total-price">
                    <li>
                      <div class="row">
                        <div class="col-sm-8 col-xs-8">Entrega</div>
                        <div class="col-sm-4 col-xs-4 text-right">R$ 2.00</div>
                      </div>
                    </li>
                    <li>
                      <div class="row">
                        <div class="col-sm-8 col-xs-8">Total</div>
                        <div class="col-sm-4 col-xs-4 text-right">${shoppingCart.valorTotalAPrazo}</div>
                      </div>
                    </li>
                    <li>
                      <div class="row">
                        <!--<div class="col-sm-6 col-xs-6">
                          <a class="btn btn-default" href="cart.html">Cart</a>
                        </div>
                        -->
                        <div class="col-sm-12 col-xs-6 text-right">
                          <a class="btn btn-primary" href="login.html">Concluir Pedido</a>
                        </div>
                      </div>
                    </li>
                </ul>
              </div>
              <!-- break
              <div class="widget">
                <div class="widget-title">
                  <h3>Category</h3>
                </div>
                <ul class="nav nav-pills nav-stacked">
                    <li class="active"><a href="#">Acessories</a></li>
                    <li><a href="#">Girl</a></li>
                    <li><a href="#">Boy</a></li>
                    <li><a href="#">Edition</a></li>
                </ul>
              </div>
              -->
              <!-- break
              <div class="widget">
                <div class="widget-title">
                  <h3>Payment Confirmation</h3>
                </div>
                <p>Already make a payment ? please confirm your payment by filling <a href="confirm.html">this form</a></p>
              </div>
              -->

            </div>
          </div>
        </div>
        <!-- end:sidebar -->

        <!-- begin:content -->
        <div class="col-md-9 col-sm-8 content">
          <div class="row">
            <div class="col-md-12">
                <ol class="breadcrumb">
                  <li><a href="${createLink(absolute:true,uri: '/' )}">Home</a></li>
                  <g:each in="${produtoInstance.grupos[0]?.ancestrais}" var="grupo" status="i">
                  	<li><a href="${createLink(absolute:true,uri: ('/produtos/' + grupo.nome) )}">${grupo.nome}</a></li>
              	  </g:each>
              	  <li class="active">
              	  	<a href="${createLink(absolute:true,uri: ('/produtos/' + produtoInstance.grupos[0]?.nome) )}"> ${produtoInstance.grupos[0]?.nome}</a>
              	  </li>
                </ol>
            </div>
          </div>
          <div class="row">
            <div class="col-md-12">
              
              <div class="heading-title">
                <h2> <span>${produtoInstance.nome}</span> <span class="text-yellow">.</span></h2>
              </div>

              <div class="row">

                <!-- begin:product-image-slider -->
                <div class="col-md-6 col-sm-6">
                  <div id="product-single" class="carousel slide" data-ride="carousel">
                    <div class="carousel-inner">

          						<g:each in="${produtoInstance.fotos}" var="foto" status="i">
          							<div class="item ${ (i == 0)?'active':'' }"> <!-- <%-- usar classe 'active p/ selecionar item' --%> -->
          								<div class="product-single">
          									<asset:image id="img${foto.id}" src="produtos/${foto.arquivo}" class="img-responsive"/>
          								</div>
          							</div>
          						</g:each>

                    </div>

                    <a class="left carousel-control" href="#product-single" data-slide="prev">
                      <i class="fa fa-angle-left"></i>
                    </a>
                    <a class="right carousel-control" href="#product-single" data-slide="next">
                      <i class="fa fa-angle-right"></i>
                    </a>
                  </div>
                </div>
                <!-- end:product-image-slider -->

                <!-- begin:product-spesification -->
                <div class="col-md-6 col-sm-6">
                  <div class="single-desc">

                    <g:form  action="add" controller="shoppingCart" method="post" id="${produtoInstance.id}">
                      
                      <span class="visible-xs">
                          <strong>Blackbox / AF0012 / In Stock</strong>
                      </span>

                      <table>
                        <tbody>

                            <tr>
                                <td colspan="3"> 
                                  <h3> ${produtoInstance.nome} </h3>
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
                                  <g:if test="${produtoInstance.precoAVistaEmCentavos <  produtoInstance.precoAPrazoEmCentavos}">
                                    <p> * Desconto à vista </p>
                                  </g:if>
                                </td>
                            </tr>

                          <g:if test="${produtoInstance.isMultiUnidade()}">
                              
                              <tr>
                                  <td><strong>${produtoInstance.tipoUnitario}</strong></td>
                                  <td>:</td>
                                  <td>
                                      <select class="form-control" id="select-unidade" name="unidade"> 
                                        <g:each in="${unidades}" var="unidade" status="i">
                                          <option value="${raw(unidade)}" ${ (i==0)?'selected':''}>${raw(unidade)}</option>
                                        </g:each>
                                      </select>
                                  </td>
                              </tr>

                              <tr class="tr-quantidade">
                                  <td><strong>Quantidade</strong></td>
                                  <td>:</td>
                                  <td>
                                    <select class="form-control" id="select-quantidade" name="quantidade"/> 
                                  </td>
                              </tr>


                          </g:if>
                          <g:else>

                          </g:else>


                          <tr id="tr-comprar">
                              <td colspan="3">
                                <a href="#" class="btn btn-sm btn-primary">Adicionar ao Carrinho</a>
                              </td>  
                          </tr>

                        </tbody>
                      </table>

                      <g:if test="${produtoInstance.isMultiUnidade()}">

                          <p id="msg-estoque-esgotado" style="display:none;"> 
                            Ow Amiga, ${produtoInstance.tipoUnitario} 
                            <span style="font-weight:bold;" id="unidade"></span>
                            está em falta
                          </p>

                          <p style="font-size:14px;color:blue;display:none;" id="aviso-ativado">
                            Avisaremos você assim que novas unidades chegarem 
                          </p>

                          <p style="display:none;" id="ativar-aviso">
                            <a data-toggle="modal" style="cursor:pointer;" data-target="#modal">Quer saber assim que chegar ?</a>
                          </p>

                      </g:if>

                    </g:form>
                  </div>
                </div>
                <!-- end:product-specification -->
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
                        <div class="fb-comments" data-width="100%"   data-href="${request.requestURL}" style=""></div>
                      </div>
                    </div>
                </div>
              </div>
              <!-- end:product-detail -->

              <!-- begin:related-product -->
              <div class="row">
                <div class="col-md-12">
                  <div class="heading-title">
                    <h2>Produtos relacionados <span> </span> <span class="text-yellow">.</span></h2>
                  </div>

                  <div class="row product-container" style="position: relative; height: 463.75px;">
                    
                    <g:each in="${1..4}">
                      <div class="col-md-3 col-sm-6 col-xs-6">
                        <div class="thumbnail product-item">
                          <a href="product_detail.html"><img alt="" src="images/product1.jpg"></a>
                          <div class="caption">
                            <h5>Pants</h5>
                            <p>$54.00</p>
                            <p>Available</p>
                          </div>
                        </div>
                      </div>
                    </g:each>

                  </div>
                </div>
              </div>
              <!-- end:related-product -->

            </div>
          </div>
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

        });

      </asset:script>

	</body>

</html>