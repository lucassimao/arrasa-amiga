class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller:"home",action:"index")
		"/api/vendas"(resources:'venda')
		"/contato"(view:'/home/contato')
		"/pwdrecovery"(view:'/home/pwdRecovery')
		"/$produto-$id"(controller:"produto",action:"detalhes")
		"500"(view:'/error')
	}
}
