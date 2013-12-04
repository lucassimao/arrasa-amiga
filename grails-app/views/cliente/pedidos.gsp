<%@ page import="br.com.arrasaamiga.Produto" %>
<%@ page import="br.com.arrasaamiga.FormaPagamento" %>


<html>
<head>
	<meta name='layout' content='main'/>
	
  <style type='text/css' media='screen'>


	</style>


</head>

<body>
  
  <g:set var="ocultarRodape" value="${true}" scope="request"/>


  <div class="well" style="background-color:white;margin-top:10px;">

	<div class="accordion" id="accordion">
		  	
	<g:each in="${pedidos}" var="pedido" status="idx">

		
		  
		  <div class="accordion-group">
		    
		    <div class="accordion-heading">
		      <a class="accordion-toggle" style="font-weight:bold;font-size:16px;" data-toggle="collapse" data-parent="#accordion" href="#collapse${idx}">
		        Pedido #${ String.format("%05d",pedido.id) }
		      </a>
		    </div>

		    <div id="collapse${idx}" class="accordion-body collapse">
		      <div class="accordion-inner">

			      <div class="well" style="text-align:center;background-color:white;color:rgb(102, 102, 102);">

			        <g:if test="${pedido.formaPagamento.equals(FormaPagamento.AVista)}">
			            <g:img style="margin-top:45px;" dir="img" file="timeline-02.png"/>
			        </g:if>
			        <g:else>
			            <g:img style="margin-top:45px;" dir="img" file="timeline-01.png"/>
			        </g:else>
			        
			      </div>


			        <div class="well" style="background-color:white;">

			          <legend> <i class="icon-truck"></i> Entrega </legend>

			          <div class="row-fluid">
			            <div>

			              <g:set var="cliente" value="${pedido.cliente}"/>
			              	
			              <label> <span class="caption"> Estado: </span> ${cliente.endereco.uf.nome}</label>

			              <label> <span class="caption"> Cidade: </span> ${cliente.endereco.cidade.nome}</label>

			              <label> <span class="caption"> Bairro: </span> ${cliente.endereco.bairro} </label>

			              <label> <span class="caption"> Complemento: </span>  ${cliente.endereco.complemento}</label>
			              
			              <g:if test="${!cliente.isDentroDaAreaDeEntregaRapida()}">
			                <label> <span class="caption"> CEP: </span> ${cliente.endereco.cep} </label>
			              </g:if>

			              <g:if test="${cliente.isDentroDaAreaDeEntregaRapida()}">
			                <label> <span class="caption"> Dia da Entrega: </span> <g:formatDate format="EEEE, dd/MM/yyyy" date="${pedido.dataEntrega}"/> </label>
			              </g:if>


			            </div>
			            
			          </div>

			        </div>

 <div class="row-flow" style="margin:20px 0px;"> 

        <table class="table table-bordered" style="background-color:white;">
          
          <thead>
            <tr>
              <th> Descrição </th>
              <th> Quantidade </th>
              <th> Preço Unitário </th>
              <th> Valor Total do Item </th>
            </tr>             
          </thead>
          
          <tbody>
            <g:each in="${pedido.itensVenda}" var="itemVenda">
              <g:set var="produto" value="${itemVenda.produto}"/>
              
              <tr>
                <td style="text-align:left !important;">
                  <g:img dir="img/produtos" style="float:left;" file="${produto.fotoMiniatura}"/>

                  <div>
                    <label>${produto.nome }</label>

                    <g:if test="${produto.isMultiUnidade()}">
                      <p> <small> ${produto.tipoUnitario}: ${itemVenda.unidade} </small> </p>
                    </g:if>

                  </div>
                </td>



                <td style="position:relative;">

                  <p> <span class="badge badge-warning">${itemVenda.quantidade}</span>  </p>

                </td>
                
                <td>
                  <g:formatNumber number="${itemVenda.precoAPrazoEmReais}" type="currency" currencyCode="BRL" />
                </td>
                
                <td>
                  <g:formatNumber number="${itemVenda.subTotalAPrazo}" type="currency" currencyCode="BRL" />
                </td>

              </tr>

            </g:each>
                  
          </tbody>
        </table>

      </div>  


		      </div>
		    </div>
		  </div>

		

	</g:each>
	</div>

  </div>

</body>
</html>
