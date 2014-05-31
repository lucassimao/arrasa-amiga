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


		<script type="text/javascript">

			$(function(){

				$("#flash").fadeOut(3000);

			});

		</script>


	</head>


	<body>

		<g:form action="salvarContato" controller="home">

			<div class="well" style="background-color:white;margin-top:10px;">

				<legend> <i class="icon-envelope"></i> Envie por aqui sua sugestão ou dúvida </legend>

					<g:hasErrors bean="${cmd}">
						<div style="margin-left:30%;">
							<h4> Preencha os campos destacados </h4>
						</div>
					</g:hasErrors>

					<g:if test="${flash.message}">
						<div id="flash" style="text-align:center;border:1px solid blue;">
							<h4> ${flash.message} </h4>
						</div>					
					</g:if>

				<div class="row-fluid">

					<div class="span12">

						<fieldset>

							<div class="control-group ${hasErrors(bean: cmd, field:'nome','error')}">
								<label style="font-weight:bold;"> Nome: </label>
								<input type="text" value="${cmd?.nome}" name="nome" class="input-xxlarge">
							</div>


							<div style="float:left;" class="control-group ${hasErrors(bean: cmd, field:'email','error')}">
								<label style="font-weight:bold;"> Email: </label>
								<input type="text" value="${cmd?.email}" name="email" class="input-large" >

							</div>

							<div style="float:left;margin-left:10px;"  class="control-group ${hasErrors(bean: cmd, field:'dddCelular','error')}">
								<label style="font-weight:bold;"> Celular: </label> 
								<input type="text" value="${cmd?.dddCelular}" placeholder="DDD" maxlength="2" name="dddCelular" class="input-mini" >
							</div>

							<div  class="control-group ${hasErrors(bean: cmd, field:'celular','error')}">
								<input style="margin-top:25px;margin-left:5px;" type="text" value="${cmd?.celular}" placeholder="numero" maxlength="9" name="celular" class="input-small">
							</div>

							<div class="control-group ${hasErrors(bean: cmd, field:'mensagem','error')}">
								<label style="font-weight:bold;"> Mensagem: </label>
								<textarea rows="7"  value="${cmd?.mensagem}" name="mensagem" class="input-xxlarge">${cmd?.mensagem}</textarea>
							</div>	

						</fieldset>
					</div>
				</div>

				<div class="row-fluid">
	                <a href="${createLink(action:'index',controller:'home')}" class="btn btn-success">
	                    <i class="icon-backward icon-white"></i> Voltar
	                </a>	

	                <button type="submit" style="margin-left:44%" class="btn btn-success">
	                   <i class="icon-ok-circle icon-white"></i>  Enviar
	                </button>
				</div>
			</div>
		</g:form>
			
	</body>
</html>



