package br.com.arrasaamiga

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.GET
import static groovyx.net.http.ContentType.XML
import groovy.xml.*
import java.text.NumberFormat

class CorreiosService {

    boolean transactional = false


    public String getTrackingURL(String trackingCode) {
        return "http://websro.correios.com.br/sro_bin/txect01\$.Inexistente?P_LINGUA=001&P_TIPO=002&P_COD_LIS=${trackingCode}"
    }

    public boolean isCepValido(String cep) {
        try {
            Double frete = calcularFrete(cep, ServicoCorreio.PAC)
            return (frete > 0)
        } catch (Exception e) {
            e.printStackTrace()
            return false
        }
    }

    public Double calcularFrete(String cepDestino, ServicoCorreio servico) {

        def http = new HTTPBuilder("http://ws.correios.com.br")
        int valorEmbalagem = 3.5

        cepDestino = cepDestino?.replace('-', '')


        http.request(GET, XML) { req ->
            uri.path = '/calculador/CalcPrecoPrazo.aspx'

            uri.query = [nCdServico    : servico.codigoServico,
                         sCepOrigem    : '64023620', sCepDestino: cepDestino,
                         nVlPeso       : '1', nCdFormato: 1,
                         nVlComprimento: 30, nVlAltura: 30, nVlLargura: 30,
                         sCdMaoPropria : 'N', nVlValorDeclarado: 0, sCdAvisoRecebimento: 'N', StrRetorno: 'xml']

            response.success = { resp, xml ->
                assert resp.status == 200
                //def respostaXML = new XmlParser().parseText(xml)
                //println XmlUtil.serialize(xml)
                if (!xml.cServico.Erro.text()?.equals('0'))
                    throw new Exception(xml.cServico.MsgErro.text())


                def valorFrete = new BigDecimal(xml.cServico.Valor.toString().replace(',', '.')).doubleValue()
                return valorFrete + valorEmbalagem
            }

        }


    }

    public String getTranckingHistory(String codigoRastreio) {
        /*String trackingURL= getTrackingURL(codigoRastreio)
         def page = new XmlSlurper(new org.cyberneko.html.parsers.SAXParser()).parse(trackingURL)
         def tableData = page.'**'.findAll{ it.name().toLowerCase().equals("table")}
 */

        def http = new HTTPBuilder("http://websro.correios.com.br")
        http.request(GET, ContentType.TEXT) { req ->

            uri.path = '/sro_bin/txect01\$.Inexistente'
            uri.query = [P_LINGUA : '001', P_TIPO   : '002', P_COD_LIS: codigoRastreio]

            response.success = { resp, reader ->
                assert resp.status == 200

                String html = reader.text
                def group = (html =~ /(?msi)<table(.+)<\/table>/) // retirando apenas a tabela do conteudo HTML
                return group[0][0]
            }

        }
    }

}
