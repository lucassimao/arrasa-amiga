package br.com.arrasaamiga

class EmailService {

    boolean transactional = false

    def asyncMailService
    def grailsApplication

    

    public void notificarAdministradores(Venda venda){

      	def administradores = ['lsimaocosta@gmail.com'] //,'fisio.adnadantas@gmail.com','mariaclaravn26@gmail.com']
        def g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib') 
          
        asyncMailService.sendMail {

            to administradores
            subject "Nova venda - #${venda.id} - ${venda.cliente.nome}"

            html g.render(template:'/venda/templates/novaVenda',model:[venda: venda, cliente: venda.cliente])
        }

    } 


    public void notificarCliente(Venda venda){

        def g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib') 
          
        asyncMailService.sendMail {

            to venda.cliente.email
            subject " A Arrasa Amiga recebeu seu pedido ! "

            html g.render(template:'/venda/templates/confirmacaoVenda',model:[venda: venda, cliente: venda.cliente])
        }
    }
}
