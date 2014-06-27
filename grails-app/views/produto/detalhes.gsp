
<%@ page import="br.com.arrasaamiga.Produto" %>
<%@ page import="br.com.arrasaamiga.Aviso" %>

<!DOCTYPE html>
<html>
	<head>
		<title> ${produtoInstance.nome}  </title>
		<parameter name="description" value="${produtoInstance.descricao}" />
		<g:set var="keywords" value="${produtoInstance.keywords.join(',')}" />	
		<parameter name="keywords" value="${keywords}" />
		<parameter name="og:image" value="${resource(dir:'img/produtos',file:produtoInstance.fotoMiniatura,absolute:true)}"/>
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
                  <h3>Cart</h3>
                </div>
                <ul class="cart list-unstyled">
                  <li>
                    <div class="row">
                      <div class="col-sm-7 col-xs-7">1 <a href="product_detail.html">Blackbox</a> <span>[ 26 ]</span></div>
                      <div class="col-sm-5 col-xs-5 text-right"><strong>$54.00</strong> <a href="#"><i class="fa fa-trash-o"></i></a></div>
                    </div>
                  </li>
                  <li>
                    <div class="row">
                      <div class="col-sm-7 col-xs-7">1 <a href="product_detail.html">JunkShirt</a> <span>[ M ]</span></div>
                      <div class="col-sm-5 col-sm-5 text-right"><strong>$26.00</strong> <a href="#"><i class="fa fa-trash-o"></i></a></div>
                    </div>
                  </li>
                </ul>
                <ul class="list-unstyled total-price">
                    <li>
                      <div class="row">
                        <div class="col-sm-8 col-xs-8">Shipping</div>
                        <div class="col-sm-4 col-xs-4 text-right">$1.00</div>
                      </div>
                    </li>
                    <li>
                      <div class="row">
                        <div class="col-sm-8 col-xs-8">Total</div>
                        <div class="col-sm-4 col-xs-4 text-right">$71.00</div>
                      </div>
                    </li>
                    <li>
                      <div class="row">
                        <div class="col-sm-6 col-xs-6">
                          <a class="btn btn-default" href="cart.html">Cart</a>
                        </div>
                        <div class="col-sm-6 col-xs-6 text-right">
                          <a class="btn btn-primary" href="login.html">Checkout</a>
                        </div>
                      </div>
                    </li>
                </ul>
              </div>
              <!-- break -->
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
              <!-- break -->
              <div class="widget">
                <div class="widget-title">
                  <h3>Payment Confirmation</h3>
                </div>
                <p>Already make a payment ? please confirm your payment by filling <a href="confirm.html">this form</a></p>
              </div>

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
							<div class="item ${ (i == 0)?'active':'' }"> <!-- usar classe 'active p/ selecionar item' -->
								<div class="product-single">
									<asset:image src="produtos/${foto.arquivo}" class="img-responsive"/>
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
                    <form>
                      <span class="visible-xs">
                          <strong>Blackbox / AF0012 / In Stock</strong>
                      </span>

                      <table>
                        <tbody>
                          <tr class="hidden-xs">
                              <td><strong>Brand</strong></td>
                              <td>:</td>
                              <td>Blackbox</td>
                          </tr>
                          <tr class="hidden-xs">
                              <td><strong>Product Code</strong></td>
                              <td>:</td>
                              <td>AF0012</td>
                          </tr>
                          <tr class="hidden-xs">
                              <td><strong>Availability</strong></td>
                              <td>:</td>
                              <td>In Stock</td>
                          </tr>
                          <tr>
                              <td colspan="3"><span class="price-old">$32.91</span> <span class="price">$21.42</span></td>
                          </tr>
                          <tr>
                              <td><strong>Color</strong></td>
                              <td>:</td>
                              <td>
                                <select class="form-control">
                                  <option>Black</option>
                                  <option>Green</option>
                                  <option>Blue</option>
                                  <option>Yellow</option>
                                </select>
                            </td>   
                          </tr>
                          <tr>
                              <td><strong>Size</strong></td>
                              <td>:</td>
                              <td>
                                <select class="form-control">
                                  <option>XS</option>
                                  <option>S</option>
                                  <option>M</option>
                                  <option>L</option>
                                  <option>XL</option>
                                </select>
                              </td>
                          </tr>
                          <tr>
                              <td><strong>Quantity</strong></td>
                              <td>:</td>
                              <td>
                                <input type="text" class="form-control" value="1">
                              </td>
                          </tr>
                          <tr>
                              <td colspan="3">
                                <a href="#" class="btn btn-sm btn-primary">Add to Cart</a>
                              </td>  
                          </tr>
                        </tbody>
                      </table>
                    </form>
                  </div>
                </div>
                <!-- end:product-spesification -->
              </div>
              <!-- break -->
              <!-- begin:product-detail -->
              <div class="row">
                <div class="col-md-12 content-detail">
                    <ul id="myTab" class="nav nav-tabs">
                      <li class="active"><a href="#desc" data-toggle="tab">Description</a></li>
                      <li class=""><a href="#care" data-toggle="tab">Care</a></li>
                      <li class=""><a href="#size" data-toggle="tab">Sizing</a></li>
                    </ul>

                    <div id="myTabContent" class="tab-content">
                      <div class="tab-pane fade active in" id="desc">
                        <p>${produtoInstance.descricao}</p>
                      </div>
                      <div class="tab-pane fade" id="care">
                        <p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid. Exercitation +1 labore velit, blog sartorial PBR leggings next level wes anderson artisan four loko farm-to-table craft beer twee. Qui photo booth letterpress, commodo enim craft beer mlkshk aliquip jean shorts ullamco ad vinyl cillum PBR.</p>
                      </div>
                      <div class="tab-pane fade" id="size">
                        <p>Standard swim suit sizing comes in S/M/L and come in Body measurements are given in inches. </p>
                        <table class="table table-striped table-bordered">
                          <tbody>
                            <tr>
                              <th>#</th>
                              <th>Size</th>
                              <th>Bust</th>
                              <th>Waist</th>
                              <th>Hip</th>
                              <th>Soulder Width</th>
                              <th>Frong Body length</th>
                            </tr>
                            <tr>
                              <td>1</td>
                              <td>XXL</td>
                              <td>96</td>
                              <td>76</td>
                              <td>102</td>
                              <td>41</td>
                              <td>44</td>
                            </tr>
                            <tr>
                              <td>2</td>
                              <td>XL</td>
                              <td>92</td>
                              <td>72</td>
                              <td>98</td>
                              <td>40</td>
                              <td>43.5</td>
                            </tr>
                            <tr>
                              <td>3</td>
                              <td>L</td>
                              <td>88</td>
                              <td>68</td>
                              <td>94</td>
                              <td>39</td>
                              <td>42</td>
                            </tr>
                            <tr>
                              <td>4</td>
                              <td>M</td>
                              <td>84</td>
                              <td>64</td>
                              <td>92</td>
                              <td>38</td>
                              <td>40.5</td>
                            </tr>
                            <tr>
                              <td>5</td>
                              <td>S</td>
                              <td>80</td>
                              <td>60</td>
                              <td>88</td>
                              <td>37</td>
                              <td>39</td>
                            </tr>
                            <tr>
                              <td>6</td>
                              <td>XS</td>
                              <td>76</td>
                              <td>56</td>
                              <td>84</td>
                              <td>36</td>
                              <td>37.5</td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </div>
                </div>
              </div>
              <!-- end:product-detail -->

              <!-- begin:related-product -->
              <div class="row">
                <div class="col-md-12">
                  <div class="heading-title">
                    <h2>Related <span>Product</span> <span class="text-yellow">.</span></h2>
                  </div>

                  <div class="row product-container" style="position: relative; height: 463.75px;">
                    <div class="col-md-3 col-sm-6 col-xs-6">
                      <div class="thumbnail product-item">
                        <a href="product_detail.html"><img alt="" src="img/product1.jpg"></a>
                        <div class="caption">
                          <h5>Pants</h5>
                          <p>$54.00</p>
                          <p>Available</p>
                        </div>
                      </div>
                    </div>
                    <div class="col-md-3 col-sm-6 col-xs-6">
                      <div class="thumbnail product-item">
                        <a href="product_detail.html"><img alt="" src="img/product2.jpg"></a>
                        <div class="caption">
                          <h5>Pants</h5>
                          <p>$54.00</p>
                          <p>Available</p>
                        </div>
                      </div>
                    </div>
                    <div class="col-md-3 col-sm-6 col-xs-6">
                      <div class="thumbnail product-item">
                        <a href="product_detail.html"><img alt="" src="img/product3.jpg"></a>
                        <div class="caption">
                          <h5>Pants</h5>
                          <p>$54.00</p>
                          <p>Available</p>
                        </div>
                      </div>
                    </div>
                    <div class="col-md-3 col-sm-6 col-xs-6">
                      <div class="thumbnail product-item">
                        <a href="product_detail.html"><img alt="" src="img/product4.jpg"></a>
                        <div class="caption">
                          <h5>Pants</h5>
                          <p>$54.00</p>
                          <p>Available</p>
                        </div>
                      </div>
                    </div>
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

	</body>

</html>