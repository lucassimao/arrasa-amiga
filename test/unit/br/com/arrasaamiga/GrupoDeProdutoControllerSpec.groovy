package br.com.arrasaamiga



import grails.test.mixin.*
import spock.lang.*

@TestFor(GrupoDeProdutoController)
@Mock(GrupoDeProduto)
class GrupoDeProdutoControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.grupoDeProdutoInstanceList
            model.grupoDeProdutoInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.grupoDeProdutoInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def grupoDeProduto = new GrupoDeProduto()
            grupoDeProduto.validate()
            controller.save(grupoDeProduto)

        then:"The create view is rendered again with the correct model"
            model.grupoDeProdutoInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            grupoDeProduto = new GrupoDeProduto(params)

            controller.save(grupoDeProduto)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/grupoDeProduto/show/1'
            controller.flash.message != null
            GrupoDeProduto.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def grupoDeProduto = new GrupoDeProduto(params)
            controller.show(grupoDeProduto)

        then:"A model is populated containing the domain instance"
            model.grupoDeProdutoInstance == grupoDeProduto
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def grupoDeProduto = new GrupoDeProduto(params)
            controller.edit(grupoDeProduto)

        then:"A model is populated containing the domain instance"
            model.grupoDeProdutoInstance == grupoDeProduto
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/grupoDeProduto/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def grupoDeProduto = new GrupoDeProduto()
            grupoDeProduto.validate()
            controller.update(grupoDeProduto)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.grupoDeProdutoInstance == grupoDeProduto

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            grupoDeProduto = new GrupoDeProduto(params).save(flush: true)
            controller.update(grupoDeProduto)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/grupoDeProduto/show/$grupoDeProduto.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/grupoDeProduto/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def grupoDeProduto = new GrupoDeProduto(params).save(flush: true)

        then:"It exists"
            GrupoDeProduto.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(grupoDeProduto)

        then:"The instance is deleted"
            GrupoDeProduto.count() == 0
            response.redirectedUrl == '/grupoDeProduto/index'
            flash.message != null
    }
}
