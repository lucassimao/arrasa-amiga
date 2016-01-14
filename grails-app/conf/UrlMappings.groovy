class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: "home", action: "index")
        "/contato"(view: '/home/contato')
        "/pwdrecovery"(view: '/home/pwdRecovery')
        "/$produto-$id"(controller: "produto", action: "detalhes")
        "/produtos/$grupoDeProduto"(controller: "home", action: "index")
        "/api/vendas"(resources: 'venda',includes:['index','save','delete','update']){
            "/anexo"(controller:'anexo', action: 'save', method:'POST')
            "/anexo"(controller:'anexo', action: 'delete', method:'DELETE')
        }
        "/api/estoque"(resources: 'estoque',includes:['index','update'])
        "/api/caixa"(controller: 'caixa')
        "/api/enderecos"(controller:'cliente',action: 'enderecos')
        "500"(view: '/error')
    }
}
