class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller:"home",action:"index")
		"/contato"(view:'/home/contato')
		"/pwdrecovery"(view:'/home/pwdRecovery')
		"/$produto-$id"(controller:"produto",action:"detalhes")
		"/produtos/$grupoDeProduto"(controller:"home",action:"index")
		"500"(view:'/error')
	}
}
