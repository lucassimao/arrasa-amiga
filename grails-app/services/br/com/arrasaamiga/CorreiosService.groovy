package br.com.arrasaamiga

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.GET
import static groovyx.net.http.ContentType.XML
import groovy.xml.*
import java.text.NumberFormat

class CorreiosService {

    boolean transactional = false

    public Double calcularFrete(String cepDestino,ServicoCorreio servico){

    	def http = new HTTPBuilder( "http://ws.correios.com.br" )

    	cepDestino = cepDestino.replace('-','')

    	try{

			http.request(GET,XML) { req ->
				uri.path = '/calculador/CalcPrecoPrazo.aspx'

				uri.query = [ nCdServico: servico.codigoServico,
							sCepOrigem:'64023620',sCepDestino: cepDestino,
				       	nVlPeso:'1',nCdFormato:1,
				       	nVlComprimento:30, nVlAltura:30, nVlLargura:30,
				       	sCdMaoPropria:'N', nVlValorDeclarado:0, sCdAvisoRecebimento:'N',StrRetorno:'xml' ]

				response.success = { resp, xml ->
					assert resp.status == 200
					//def respostaXML = new XmlParser().parseText(xml)
					//println XmlUtil.serialize(xml)
					if (!xml.cServico.Erro.text()?.equals('0'))
						throw new Exception(xml.cServico.MsgErro.text())

					return new BigDecimal(xml.cServico.Valor.toString().replace(',','.')).doubleValue()
				}
			 
			}

		}catch(Exception e){
			e.printStackTrace()

			// caso haja algum erro durante a comunicaçao com os correios, devolve um valor padrao 
			if ( servico.equals(ServicoCorreio.PAC) )
				return 20.15
			else
				return 35.55
		}


    }

}
