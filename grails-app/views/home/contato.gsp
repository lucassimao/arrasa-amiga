<!DOCTYPE html>
<html>
	<head>

		<title> Arrasa Amiga: Como Comprar  </title>
		<parameter name="description" value="Dicas úteis sobre como comprar na Arrasa Amiga" />
		<parameter name="keywords" value="Maquiagem, blushes, NYX, batom, sombra, paleta,Teresina, Piauí, maquiadora" />
		<parameter name="og:image" value="${resource(dir:'img',file:'top.jpg',absolute:true)}"/>


		<meta name="layout" content="main"/>

		<style type="text/css">

			.container p{
				text-align:justify;
				text-indent:20px;
			}

		</style>

	</head>


	<body>

	
		<div class="jumbotron" style="background-color:white;">

			<legend> <i class="fa fa-envelope"></i> Envie sua sugestão ou dúvida </legend>

			<g:hasErrors bean="${cmd}">
				<div class="alert alert-danger" style="margin-top:10px;">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
			  <i class="icon-edit"></i>
				Preencha os campos destacados 
				</div>
			</g:hasErrors>

			<g:if test="${flash.message}">
				<div id="flash" class="alert alert-success" style="margin-top:10px;">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
			  		<i class="icon-edit"></i>${flash.message}
				</div>							
			</g:if>

			<g:form action="salvarContato" class="form-horizontal" controller="home">

                <div class="form-group">
                  <label class="col-sm-2 control-label">Nome:</label>
                  <div class="col-sm-10">
                    <input id="input-nome" type="text" value="${cmd?.nome}" name="nome" 
                          class="form-control ${hasErrors(bean: cmd, field:'nome','error')}" placeholder="Nome">
                  </div>
                </div>

                <div class="form-group">
                  <label class="col-sm-2 control-label">Email:</label>
                  <div class="col-sm-10">
                    <input id="input-email" type="text" value="${cmd?.email}" name="email" 
                          class="form-control ${hasErrors(bean: cmd, field:'email','error')}" placeholder="Email">
                  </div>
                </div>

                <div class="form-group">
                  <label class="col-sm-2 control-label">Celular:</label>
                  <div class="col-sm-10" style="padding-left:0;">
	                  <div class="col-xs-4">
	                    <input id="input-dddCelular" type="text" value="${cmd?.dddCelular}" name="dddCelular" 
	                          class="form-control ${hasErrors(bean: cmd, field:'dddCelular','error')}" placeholder="DDD">
	                  </div>
	                  <div class="col-xs-6">
	                    <input id="input-celular" type="text" value="${cmd?.celular}" name="celular" 
	                          class="form-control ${hasErrors(bean: cmd, field:'celular','error')}" placeholder="Numero">
	                  </div>		        
                  </div>          
                </div>

                <div class="form-group">
                  <label class="col-sm-2 control-label">Mensagem:</label>
                  <div class="col-sm-10">
                    <textarea class="form-control ${hasErrors(bean: cmd, field:'mensagem','error')}" rows="7" value="${cmd?.mensagem}" name="mensagem"></textarea>
                  </div>
                </div>		

				<div class="pull-right">
	                <a href="${createLink(uri:'/',absolute:true)}" class="btn btn-default">
	                    <i class="fa fa-backward icon-white"></i> Voltar
	                </a>	
	                <button type="submit" class="btn btn-primary">
	                   <i class="fa fa-check-circle icon-white"></i>  Enviar
	                </button>
				</div>                                

			</g:form>

		</div>


			
	</body>
</html>



