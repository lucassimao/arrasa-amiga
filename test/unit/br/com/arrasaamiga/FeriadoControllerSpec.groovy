package br.com.arrasaamiga



import grails.test.mixin.*
import spock.lang.*

@TestFor(FeriadoController)
@Mock(Feriado)
class FeriadoControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        params['descricao'] = 'Feriado XPTO'

        params['inicio'] = new Date()
        params['fim'] = new Date()+2

    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.feriadoInstanceList
            model.feriadoInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.feriadoInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def feriado = new Feriado()
            feriado.validate()
            controller.save(feriado)

        then:"The create view is rendered again with the correct model"
            model.feriadoInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            feriado = new Feriado(params)

            controller.save(feriado)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/feriado/show/1'
            controller.flash.message != null
            Feriado.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def feriado = new Feriado(params)
            controller.show(feriado)

        then:"A model is populated containing the domain instance"
            model.feriadoInstance == feriado
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def feriado = new Feriado(params)
            controller.edit(feriado)

        then:"A model is populated containing the domain instance"
            model.feriadoInstance == feriado
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/feriado/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def feriado = new Feriado()
            feriado.validate()
            controller.update(feriado)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.feriadoInstance == feriado

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            feriado = new Feriado(params).save(flush: true)
            controller.update(feriado)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/feriado/show/$feriado.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/feriado/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def feriado = new Feriado(params).save(flush: true)

        then:"It exists"
            Feriado.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(feriado)

        then:"The instance is deleted"
            Feriado.count() == 0
            response.redirectedUrl == '/feriado/index'
            flash.message != null
    }
}
