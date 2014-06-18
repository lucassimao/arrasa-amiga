<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta name="description" content="responsive clothing store template">
    <meta name="author" content="afriq yasin ramadhan">
    <link rel="shortcut icon" href="img/favicon.png">

    <title>Clotheshop</title>

    <!-- Bootstrap core CSS -->
    <link href="${resource(dir:'css',file:'bootstrap.css') }" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="${resource(dir:'css',file:'style.css') }" rel="stylesheet">
    <link href="${resource(dir:'css',file:'responsive.css') }" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="${resource(dir:'js',file:'html5shiv.js') }"></script>
      <script src="${resource(dir:'js',file:'respond.min.js') }"></script>
    <![endif]-->
  </head>

  <body>

    <!-- begin:navbar -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#clotheshop-navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>

        <div class="collapse navbar-collapse" id="clotheshop-navbar">
          <ul class="nav navbar-nav navbar-right">
            <li><a class="disabled">CURRENCY</a></li>
            <li class="active"><a href="#">USD</a></li>
            <li><a href="#">EUR</a></li>
            <li><a href="#">IDR</a></li>
            <li class="divider-vertical"></li>
            <li class="dropdown">
              <a data-toggle="dropdown" class="dropdown-toggle" href="#">LANGUAGE <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li><a href="#">EN</a></li>
                <li><a href="#">ID</a></li>
                <li><a href="#">FR</a></li>
                <li class="divider"></li>
                <li><a href="#">IT</a></li>
              </ul>
            </li>
          </ul>
        </div><!-- /.navbar-collapse -->
      </div>
    </nav>
    <!-- end:navbar -->

    <!-- begin:content -->
    <div class="container">
      <!-- begin:logo -->
      <div class="row">
        <div class="col-md-6 col-sm-6 col-xs-6">
          <div class="logo">
            <h1><a href="index.html">Clothe<span>shop</span> </a></h1>
            <p>Clean and simple shopping cart</p>
          </div>
        </div>
        <div class="col-md-6 col-sm-6 col-xs-6">
          <div class="account">
            <ul>
              <li id="your-account">
                <div class="hidden-xs">
                  <h4><a href="#">Your Account</a></h4>
                  <p>Welcome, <a href="login.html">log in</a></p>
                </div>
                <div class="visible-xs">
                  <a href="login.html" class="btn btn-primary"><i class="fa fa-user"></i></a>
                </div>
              </li>
              <li>
                <div class="hidden-xs">
                  <h4><a href="cart.html">Cart</a></h4>
                  <p><strong>3 Product(s)</strong></p>
                </div>
                <div class="visible-xs">
                  <a href="cart.html" class="btn btn-primary"><span class="cart-item">3</span> <i class="fa fa-shopping-cart"></i></a>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div> 
      <!-- end:logo -->

      <!-- begin:nav-menus -->
      <div class="row">
        <div class="col-md-12">
          <div class="nav-menus">
            <ul class="nav nav-pills">
              <li class="active"><a href="index.html">Home</a></li>
              <li><a href="#">Acessories</a></li>
              <li class="dropdown">
                <a href="#" data-toggle="dropdown" class="dropdown-toggle">Boy <b class="caret"></b></a>
                <ul class="dropdown-menu" id="menu1">
                  <li>
                    <a href="#">Shirts <b class="caret"></b></a>
                    <ul class="dropdown-menu sub-menu">
                      <li><a href="#">Shirts</a></li>
                      <li><a href="#">T-shirts</a></li>
                      <li><a href="#">Polo Shirts</a></li>
                      <li><a href="#">Tanktop</a></li>
                    </ul>
                  </li>
                  <li><a href="#">Jacket</a></li>
                  <li><a href="categories.html">Pants</a></li>
                  <li><a href="#">Boxer</a></li>
                  <li class="divider"></li>
                  <li><a href="#">SweatShirts</a></li>
                </ul>
              </li>
              <li class="dropdown">
                <a href="#" data-toggle="dropdown" class="dropdown-toggle">Girl <b class="caret"></b></a>
                <ul class="dropdown-menu" id="menu1">
                  <li><a href="#">Shirts</a></li>
                  <li><a href="#">Pants</a></li>
                  <li><a href="#">Skirts</a></li>
                </ul>
              </li>
              <li><a href="#">Edition</a></li>
              <li><a href="#">Authorized Dealer</a></li>
              <li><a href="about.html">About</a></li>
              <li><a href="contact.html">Contact</a></li>
            </ul>
          </div>
        </div>
      </div>
      <!-- end:nav-menus -->

      <!-- begin:home-slider -->
      <div id="home-slider" class="carousel slide" data-ride="carousel">
        <ol class="carousel-indicators">
          <li data-target="#home-slider" data-slide-to="0" class="active"></li>
          <li data-target="#home-slider" data-slide-to="1" class=""></li>
          <li data-target="#home-slider" data-slide-to="2" class=""></li>
        </ol>
        <div class="carousel-inner">
          <div class="item active">
            <img src="${resource(dir:'img',file:'layout-WB0BMF1K5/slider1.png')}" alt="clotheshop">
            <div class="carousel-caption hidden-xs">
              <h3>First slide label</h3>
              <p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
            </div>
          </div>
          <div class="item">
            <img src="${resource(dir:'img',file:'layout-WB0BMF1K5/slider2.png')}" alt="clotheshop">
            <div class="carousel-caption hidden-xs">
              <h3>Second slide label</h3>
              <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
            </div>
          </div>
          <div class="item">
            <img src="${resource(dir:'img',file:'layout-WB0BMF1K5/slider3.png')}" alt="clotheshop">
            <div class="carousel-caption hidden-xs">
              <h3>Third slide label</h3>
              <p>Praesent commodo cursus magna, vel scelerisque nisl consectetur.</p>
            </div>
          </div>
        </div>
        <a class="left carousel-control" href="#home-slider" data-slide="prev">
          <i class="fa fa-angle-left"></i>
        </a>
        <a class="right carousel-control" href="#home-slider" data-slide="next">
          <i class="fa fa-angle-right"></i>
        </a>
      </div>
      <!-- end:home-slider -->

      <!-- begin:best-seller -->
      <div class="row">
        <div class="col-md-12">
          <div class="page-header">
            <h2>Best Seller <small>Most sold product in this month</small></h2>
          </div>
        </div>
      </div>

      <div class="row product-container">
        <div class="col-md-3 col-sm-3 col-xs-6">
          <div class="thumbnail product-item">
            <a href="product_detail.html"><img alt="" src="${resource(dir:'img',file:'layout-WB0BMF1K5/product1.jpg')}"></a>
            <div class="caption">
              <h5>Casual Rock Pants</h5>
              <p>$54.00</p>
              <p>Available</p>
            </div>
            <div class="product-item-badge">New</div>
          </div>
        </div>
        <div class="col-md-3 col-sm-3 col-xs-6">
          <div class="thumbnail product-item">
            <a href="product_detail.html"><img alt="" src="${resource(dir:'img',file:'layout-WB0BMF1K5/product2.jpg')}"></a>
            <div class="caption">
              <h5>T-shirt</h5>
              <p class="product-item-price">$54.00</p>
              <p>Available</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-sm-3 col-xs-6">
          <div class="thumbnail product-item">
            <a href="product_detail.html"><img alt="" src="${resource(dir:'img',file:'layout-WB0BMF1K5/product3.jpg')}"></a>
            <div class="caption">
              <h5>Casual Rock Pants</h5>
              <p><del>$54.00</del> $32.00</p>
              <p>Available</p>
            </div>
            <div class="product-item-badge badge-sale">Sale</div>
          </div>
        </div>
        <div class="col-md-3 col-sm-3 col-xs-6">
          <div class="thumbnail product-item">
            <a href="product_detail.html"><img alt="" src="${resource(dir:'img',file:'layout-WB0BMF1K5/product4.jpg')}"></a>
            <div class="caption">
              <h5>Casual Rock T-Shirt</h5>
              <p>$54.00</p>
              <p>Available</p>
            </div>
          </div>
        </div>
      </div>
      <!-- end:best-seller -->

      <!-- begin:new-arrival -->
      <div class="row">
        <dov class="col-md-12">
          <div class="page-header">
            <h2>New Arrival <small>New products in this month</small></h2>
          </div>
        </dov>
      </div>

      <div class="row product-container">
        <div class="col-md-3 col-sm-3 col-xs-6">
          <div class="thumbnail product-item">
            <a href="product_detail.html"><img alt="" src="${resource(dir:'img',file:'layout-WB0BMF1K5/product1.jpg')}"></a>
            <div class="caption">
              <h5>Pants</h5>
              <p>$54.00</p>
              <p>Available</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-sm-3 col-xs-6">
          <div class="thumbnail product-item">
            <a href="product_detail.html"><img alt="" src="${resource(dir:'img',file:'layout-WB0BMF1K5/product2.jpg')}"></a>
            <div class="caption">
              <h5>Pants</h5>
              <p>$54.00</p>
              <p>Available</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-sm-3 col-xs-6">
          <div class="thumbnail product-item">
            <a href="product_detail.html"><img alt="" src="${resource(dir:'img',file:'layout-WB0BMF1K5/product3.jpg')}"></a>
            <div class="caption">
              <h5>Pants</h5>
              <p>$54.00</p>
              <p>Available</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-sm-3 col-xs-6">
          <div class="thumbnail product-item">
            <a href="product_detail.html"><img alt="" src="${resource(dir:'img',file:'layout-WB0BMF1K5/product4.jpg')}"></a>
            <div class="caption">
              <h5>Pants</h5>
              <p>$54.00</p>
              <p>Available</p>
            </div>
          </div>
        </div>
      </div>
      <!-- end:new-arrival -->

      <!-- begin:random-product -->
      <div class="row">
        <div class="col-md-12">
          <div class="page-header">
            <h2>Random Product <small>Other products</small></h2>
          </div>
        </div>
      </div>

      <div class="row product-container">
        <div class="col-md-3 col-sm-3 col-xs-6">
          <div class="thumbnail product-item">
            <a href="product_detail.html"><img alt="" src="${resource(dir:'img',file:'layout-WB0BMF1K5/product1.jpg')}"></a>
            <div class="caption">
              <h5>Pants</h5>
              <p>$54.00</p>
              <p>Available</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-sm-3 col-xs-6">
          <div class="thumbnail product-item">
            <a href="product_detail.html"><img alt="" src="${resource(dir:'img',file:'layout-WB0BMF1K5/product2.jpg')}"></a>
            <div class="caption">
              <h5>Pants</h5>
              <p>$54.00</p>
              <p>Available</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-sm-3 col-xs-6">
          <div class="thumbnail product-item">
            <a href="product_detail.html"><img alt="" src="${resource(dir:'img',file:'layout-WB0BMF1K5/product3.jpg')}"></a>
            <div class="caption">
              <h5>Pants</h5>
              <p>$54.00</p>
              <p>Available</p>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-sm-3 col-xs-6">
          <div class="thumbnail product-item">
            <a href="product_detail.html"><img alt="" src="${resource(dir:'img',file:'layout-WB0BMF1K5/product4.jpg')}"></a>
            <div class="caption">
              <h5>Pants</h5>
              <p>$54.00</p>
              <p>Available</p>
            </div>
          </div>
        </div>
      </div>
      <!-- end:random-product -->

      <!-- begin:footer -->
      <div class="row">
        <div class="col-md-12 footer">
          <div class="row">
            <div class="col-md-3 col-sm-6">
              <div class="widget">
                <h3><span>Contact Info</span></h3>
                <address>
                  No. 22, Bantul, Yogyakarta, Indonesia<br>
                  Call Us : (0274) 4411005<br>
                  Email : avriqq@gmail.com<br>
                </address>
              </div>
            </div>

            <div class="col-md-3 col-sm-6">
              <div class="widget">
                <h3><span>Customer Support</span></h3>
                <ul class="list-unstyled list-star">
                  <li><a href="#">FAQ</a></li>
                  <li><a href="#">Payment Option</a></li>
                  <li><a href="#">Booking Tips</a></li>
                  <li><a href="#">Information</a></li>
                </ul>
              </div>
            </div>

            <div class="col-md-3 col-sm-6">
              <div class="widget">
                <h3><span>Discover our store</span></h3>
                <ul class="list-unstyled list-star">
                    <li><a href="#">California</a></li>
                    <li><a href="#">Bali</a></li>
                    <li><a href="#">Singapore</a></li>
                    <li><a href="#">Canada</a></li>
                </ul>
              </div>
            </div>

            <div class="col-md-3 col-sm-6">
              <div class="widget">
                <h3><span>Get Our Newsletter</span></h3>
                <p>Subscribe to our newsletter and get exclusive deals straight to your inbox!</p>
                <form>
                  <input type="email" class="form-control" name="email" placeholder="Your Email : "><br>
                  <input type="submit" class="btn btn-warning" value="Subscribe">
                </form>
              </div>
            </div>

          </div>
        </div>
      </div>
      <!-- end:footer -->

      <!-- begin:copyright -->
      <div class="row">
        <div class="col-md-12 copyright">
          <div class="row">
            <div class="col-md-6 col-sm-6 copyright-left">
              <p>Copyright &copy; Clotheshop 2012-2014. All right reserved.</p>
            </div>
            <div class="col-md-6 col-sm-6 copyright-right">
              <ul class="list-unstyled list-social">
                <li><a href="#"><i class="fa fa-twitter"></i></a></li>
                <li><a href="#"><i class="fa fa-facebook-square"></i></a></li>
                <li><a href="#"><i class="fa fa-google-plus"></i></a></li>
                <li><a href="#"><i class="fa fa-instagram"></i></a></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <!-- end:copyright -->

    </div>
    <!-- end:content -->


    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="${resource(dir:'js',file:'jquery.js')}"></script>
    <script src="${resource(dir:'js',file:'bootstrap.min.js')}"></script>
    <script src="${resource(dir:'js',file:'masonry.pkgd.min.js')}"></script>
    <script src="${resource(dir:'js',file:'imagesloaded.pkgd.min.js')}"></script>
    <script src="${resource(dir:'js',file:'script.js')}"></script>

  </body>
</html>
