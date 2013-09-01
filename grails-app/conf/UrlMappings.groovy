class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller:"home",action:"index")
		"/$produto-$id"(controller:"produto",action:"detalhes")
		"500"(view:'/error')
	}
}
