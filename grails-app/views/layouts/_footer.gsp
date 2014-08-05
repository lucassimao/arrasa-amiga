<%@ page import="br.com.arrasaamiga.Cidade" %>

<!-- begin:footer -->
<div class="row">
  <div class="col-md-12 footer">
    <div class="row">
      <div class="col-md-3 col-sm-6">
        <div class="widget">
          <h3> <span class="whatsapp"> Contato </span></h3>
          <address>
            Contato : (86) 9910-8321  <br>
            Contato : (86) 9915-9682  <br>
            Contato : (86) 8835-3101  <br>
            Email : arrasa.amiga@gmail.com<br>
          </address>
        </div>
      </div>

      <div class="col-md-3 col-sm-6">
        <div class="widget">
          <h3><span> Ajuda </span></h3>
          <ul class="list-unstyled list-star">
            <li><a href="${createLink(controller:'home',action:'comocomprar',params: [cidade:Cidade.teresina.id])}"> Como Comprar </a></li>
            <li><a href="#"> Quem somos </a></li>
          </ul>
        </div>
      </div>

      <div class="col-md-6 col-sm-6">
        <div class="widget">
          <h3><span> Facebook</span></h3>
          
          <div class="fb-like" style="margin-left:4px;float:right;width: 100% !important;" 
              data-href="https://www.facebook.com/arrasaamiga" 
              data-send="true" data-show-faces="true" data-font="tahoma"></div>

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
        <p>Copyright &copy; Arrasa Amiga 2012-2014. All right reserved.</p>
      </div>
      <div class="col-md-6 col-sm-6 copyright-right">
        <ul class="list-unstyled list-social">
          <li><a target="_blank" href="http://www.facebook.com/arrasaamiga"><i class="fa fa-facebook-square"></i></a></li>
          <li><a target="_blank" href="http://instagram.com/arrasaamiga/"><i class="fa fa-instagram"></i></a></li>
          <!-- <li><a href="#"><i class="fa fa-twitter"></i></a></li>
          <li><a href="#"><i class="fa fa-google-plus"></i></a></li> -->
        </ul>
      </div>
    </div>
  </div>
</div>
<!-- end:copyright -->