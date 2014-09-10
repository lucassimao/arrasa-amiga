package br.com.arrasaamiga

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(ProdutoController)
class ProdutoControllerUnitSpec extends Specification {

    def setupSpec() {
        ProdutoController.metaClass.getUploadDir(){
            return "/images/produtos"
        }
    }

    def cleanup() {
    }

    void "test adicionar produto"(){
        given:
            def mockTemplate = '<< MOCK TEMPLATE addNewUnidade >>'
            views['/produto/_addNewUnidade.gsp'] = mockTemplate
        when:
            params.unidade = 'UN'
            controller.addNewUnidade()
        then:
            response.text == mockTemplate


    }

    void "test upload de imagem de produto"(){
        given:
            def mockTemplate = '<< MOCK TEMPLATE addNewFoto >>'
            views['/produto/_addNewFoto.gsp'] = mockTemplate
            def file = new GrailsMockMultipartFile('foto','fotoqualquer.jpg',null, 'conteudo qualquer'.bytes)
        when:
            request.addFile(file)
            controller.asyncFotoUpload()
        then: 'O arquivo armazenado no servidor deve ter o nome modificado para img + numero aleatorio+nome original do arquivo'
            file.targetFileLocation.absolutePath.matches('.*(img\\d+fotoqualquer.jpg)$')
            response.text == mockTemplate
    }


    void "test invocar a action do controller para salvar imagem sem passar o arquivo"(){
        when:
            controller.asyncFotoUpload()
        then: 'O controlado nao encontra o arquivo na requisicao e retorna codigo de erro'
            response.status == 400
    }


    void "test upload de imagem de produto com nome de parametro incorreto"(){
        given:
            def file = new GrailsMockMultipartFile('parametroIncorreto','fotoqualquer.jpg',null, 'conteudo qualquer'.bytes)
        when:
            request.addFile(file)
            controller.asyncFotoUpload()
        then: 'O arquivo de foto é enviado, mas o nome do parametro é errado'
            response.status == 400
    }

}
