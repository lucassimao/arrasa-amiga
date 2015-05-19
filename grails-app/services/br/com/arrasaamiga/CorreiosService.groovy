package br.com.arrasaamiga

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.GET
import static groovyx.net.http.ContentType.XML
import groovy.xml.*
import java.text.NumberFormat

class CorreiosService {

    boolean transactional = false


    public String getTrackingURL(String trackingCode){
    	return "http://websro.correios.com.br/sro_bin/txect01\$.Inexistente?P_LINGUA=001&P_TIPO=002&P_COD_LIS=${trackingCode}"
    }

    public Double calcularFrete(String cepDestino,ServicoCorreio servico){

    	def http = new HTTPBuilder( "http://ws.correios.com.br" )
		int valorEmbalagem = 3.5

    	cepDestino = cepDestino?.replace('-','')

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


                    def valorFrete = new BigDecimal(xml.cServico.Valor.toString().replace(',', '.')).doubleValue()
                    return valorFrete+valorEmbalagem
				}
			 
			}

		}catch(Exception e){
			e.printStackTrace()

			// caso haja algum erro durante a comunicaçao com os correios, devolve um valor padrao 
			if ( servico.equals(ServicoCorreio.PAC) )
				return 21.15 + valorEmbalagem
			else
				return 33.55 + valorEmbalagem
		}


    }

}
