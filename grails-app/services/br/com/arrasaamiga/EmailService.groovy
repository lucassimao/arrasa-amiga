package br.com.arrasaamiga

import  grails.gsp.PageRenderer

class EmailService {

    boolean transactional = false

    def mailService
    PageRenderer groovyPageRenderer
    def administradores = ['lsimaocosta@gmail.com','arrasaamiga@gmail.com'].toArray()

    

    public void notificarAdministradores(Venda venda){
          
        mailService.sendMail {

            to administradores
            async true
            subject "Nova venda - #${venda.id} - ${venda.cliente.nome}"

            html groovyPageRenderer.render(template:'/venda/templates/novaVenda',model:[venda: venda, cliente: venda.cliente])
        }

    } 


    public void enviarNovaSenha(Cliente cliente, String novaSenha){
        mailService.sendMail {

            to cliente.email
            async true
            subject "Arrasa Amiga: Nova Senha"

            html groovyPageRenderer.render(template:'/home/novaSenha',model:[cliente: cliente,novaSenha: novaSenha])
        }        
    }


    public void notificarCliente(Venda venda){
          
        mailService.sendMail {

            to venda.cliente.email
            async true
            subject " A Arrasa Amiga recebeu seu pedido ! "

            html groovyPageRenderer.render(template:'/venda/templates/confirmacaoVenda',model:[venda: venda, cliente: venda.cliente])
        }
    }


    public void notificarContato(String nome,String telefone,String email,String msg){

        mailService.sendMail {

            to email
            async true
            subject "A Arrasa Amiga recebeu sua mensagem ! "
            html '<body> <p> Este e-mail é automático</p> <p> Agradecemos seu contato e responderemos o mais breve possível amiga! </p> </body>'
        }

        mailService.sendMail {

            to administradores
            async true
            subject " Nova mensagem para Arrasa Amiga ! "
            html "<body> <p> De: ${nome} </p><p> Email: ${email}</p><p> Telefone: ${telefone}</p> <p>Mensagem: ${msg}</p> </body>"

        }
    }

}
