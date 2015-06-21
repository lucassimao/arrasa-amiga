package br.com.arrasaamiga

import  grails.gsp.PageRenderer

class EmailService {

    boolean transactional = false

    def mailService
    def correiosService
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

    def notificarCodigoDeRastreio(Venda venda){
        mailService.sendMail {

            to venda.cliente.email
            async true
            subject "Arrasa Amiga - Seu pedido #${venda.id} foi enviado!"
            html '<body> ' +
                    '<p> ${venda.cliente.nome}, </p> ' +
                    "<p> seu pedido #${venda.id} foi enviado através dos correios. O código de rastreio do seu pedido é ${venda.codigoRastreio.toUpperCase()}. </p>" +
                    "<p>  <a href='${correiosService.getTrackingURL(venda.codigoRastreio)}' target='_blank'> Click aqui para acompanhar o andamento da sua entrega </a>  </p> " +
                    "</body>"
        }
    }

    def notificarCancelamento(Venda venda) {
        mailService.sendMail {

            to venda.cliente.email
            async true
            subject "Arrasa Amiga - Aviso de cancelamento de pedido"
            html '<body> ' +
                    '<p> ${venda.cliente.nome}, </p> ' +
                    '<p></p>'+
                    "<p> Seu pedido #${venda.id} foi cancelado devido a um problema com a forma de pagamento escolhida. " +
                    "Caso tenha utilizado cartão de crédito, tente novamente informando corretamente os dados do mesmo. " +
                    "Caso tenha escolhido boleto bancário, o mesmo deve ter vencimento e sua compra foi automaticamente cancelada. </p> " +
                    "</body>"
        }

        mailService.sendMail {

            to administradores
            async true
            subject "Arrasa Amiga - Aviso de cancelamento de pedido"
            html "<body> <p> Pedido #${venda.id} foi cancelado </p> </body>"
        }
    }

    def enviarRelatorioDePedidosEnviadosPorCorreios(List<TrackingHistory> histories) {

        mailService.sendMail {
            to administradores
            async true
            subject "Arrasa Amiga - Relatório de Pedidos Enviados pelos Correios"
            html groovyPageRenderer.render(template:'/venda/templates/trackingHistoryReport',
                   model:[histories: histories])
        }
    }

}
