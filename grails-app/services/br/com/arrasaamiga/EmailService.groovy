package br.com.arrasaamiga

import  grails.gsp.PageRenderer

class EmailService {

    boolean transactional = false

    def asyncMailService
    PageRenderer groovyPageRenderer
    def administradores = ['lsimaocosta@gmail.com','arrasaamiga@gmail.com'] //,'fisio.adnadantas@gmail.com','mariaclaravn26@gmail.com']

    

    public void notificarAdministradores(Venda venda){
          
        asyncMailService.sendMail {

            to administradores
            subject "Nova venda - #${venda.id} - ${venda.cliente.nome}"

            html groovyPageRenderer.render(template:'/venda/templates/novaVenda',model:[venda: venda, cliente: venda.cliente])
        }

    } 


    public void notificarCliente(Venda venda){
          
        asyncMailService.sendMail {

            to venda.cliente.email
            subject " A Arrasa Amiga recebeu seu pedido ! "

            html groovyPageRenderer.render(template:'/venda/templates/confirmacaoVenda',model:[venda: venda, cliente: venda.cliente])
        }
    }


    public void notificarContato(String nome,String telefone,String email,String msg){

        asyncMailService.sendMail {

            to email
            subject "A Arrasa Amiga recebeu sua mensagem ! "
            html '<body> <p> Este e-mail é automático</p> <p> Agradecemos seu contato e responderemos o mais breve possível amiga! </p> </body>'
        }

        asyncMailService.sendMail {

            to administradores
            subject " Nova mensagem para Arrasa Amiga ! "
            html "<body> <p> De: ${nome} </p><p> Email: ${email}</p><p> Telefone: ${telefone}</p> <p>Mensagem: ${msg}</p> </body>"

        }
    }

}
