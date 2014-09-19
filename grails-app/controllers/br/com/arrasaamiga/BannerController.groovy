package br.com.arrasaamiga

import grails.util.BuildSettingsHolder
import grails.util.Environment

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured



@Transactional(readOnly = true)
@Secured(['isAuthenticated()'])
class BannerController {

    static allowedMethods = [save: "POST", update: ["PUT","POST"], delete: "DELETE"]

    def grailsApplication

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Banner.list(params), model:[bannerInstanceCount: Banner.count()]
    }

    def show(Banner bannerInstance) {
        respond bannerInstance
    }

    def create() {
        respond new Banner(params)
    }

    @Transactional
    def save(Banner bannerInstance) {
        if (bannerInstance == null) {
            notFound()
            return
        }

        def multipartFile = request.getFile('bannerFile')
        String originalFilename = null

        if(!multipartFile?.empty){
            originalFilename = "img${System.currentTimeMillis()}${multipartFile?.originalFilename}"
            bannerInstance.arquivo = originalFilename
            bannerInstance.clearErrors()
        }

        if (bannerInstance.hasErrors()) {
            respond bannerInstance.errors, view:'create'
            return
        }

        String uploadDir = getUploadDir()
        multipartFile?.transferTo(new File(uploadDir + File.separator + originalFilename))

        bannerInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'banner.label', default: 'Banner'), bannerInstance.id])
                redirect bannerInstance
            }
            '*' { respond bannerInstance, [status: CREATED] }
        }
    }

    def edit(Banner bannerInstance) {
        respond bannerInstance
    }

    @Transactional
    def update(Banner bannerInstance) {
        if (bannerInstance == null) {
            notFound()
            return
        }

        def multipartFile = request.getFile('bannerFile')
        String originalFilename = null

        if(!multipartFile?.empty){
            originalFilename = "img${System.currentTimeMillis()}${multipartFile?.originalFilename}"
            bannerInstance.arquivo = originalFilename
            bannerInstance.clearErrors()
        }

        if (bannerInstance.hasErrors()) {
            respond bannerInstance.errors, view:'edit'
            return
        }

        String uploadDir = getUploadDir()
        multipartFile?.transferTo(new File(uploadDir + File.separator + originalFilename))

        bannerInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Banner.label', default: 'Banner'), bannerInstance.id])
                redirect bannerInstance
            }
            '*'{ respond bannerInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Banner bannerInstance) {

        if (bannerInstance == null) {
            notFound()
            return
        }

        bannerInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Banner.label', default: 'Banner'), bannerInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'banner.label', default: 'Banner'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    private String getUploadDir(){
        return grailsApplication.mainContext.getResource('images/banners').file.absolutePath

    }
}
