package br.com.arrasaamiga

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional
import org.hibernate.ObjectNotFoundException
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.OK
import grails.util.Holders

@Secured(['ROLE_ADMIN','ROLE_VENDEDOR'])
class AnexoController {

	def grailsApplication
	def gcmService

  	static allowedMethods = [save: 'POST',delete:'DELETE']


	def handleException(Exception e){
		e.printStackTrace()
		render status: BAD_REQUEST,text: e.toString()
	}

	def save(){
		def venda = Venda.load(params.vendaId)
		def file = request.getFile('anexo')

        if (file && !file.empty){
            def uploadDir = grailsApplication.mainContext.getResource("images/anexos").getFile().absolutePath
            def originalFilename = file.originalFilename

            if (venda.anexos.contains(originalFilename))
            	throw new IllegalArgumentException("Já existe um anexo com o nome ${originalFilename}")

            file.transferTo(new File(uploadDir + File.separator + originalFilename))
            venda.addToAnexos(originalFilename)
            venda.save(flush:true)

			def config = Holders.config
			if (config.useGcmService)
				gcmService.notificarNovoAnexo(venda,originalFilename)

			render status: OK,text:'Anexo adicionado'
        }else{
            render status: BAD_REQUEST,text:'O anexo deve ser enviado'
        }

	}

	def delete(String anexo){
		def venda = Venda.load(params.vendaId)

		if (venda.anexos?.contains(anexo)){
			venda.removeFromAnexos(anexo)

            def uploadDir = grailsApplication.mainContext.getResource("images/anexos").getFile().absolutePath
            def file = new File(uploadDir + File.separator + anexo)
            file.delete()
            venda.save(flush:true)

	        render status: OK,text:'Anexo removido'
		}else{
			render status: BAD_REQUEST,text:"Anexo ${anexo} inexistente"
		}

	}

}
