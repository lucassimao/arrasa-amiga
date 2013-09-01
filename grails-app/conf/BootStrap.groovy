import br.com.arrasaamiga.*

import grails.util.Environment

class BootStrap {

    def init = { servletContext ->


    	if (Environment.current == Environment.DEVELOPMENT){

	        def adminRole = new GrupoDeUsuario(authority: 'ROLE_ADMIN').save(flush: true)
	        def userRole = new GrupoDeUsuario(authority: 'ROLE_CLIENTE').save(flush: true)

	        def testUser = new Usuario(username: 'me', enabled: true, password: '123')
	        testUser.save(flush: true)

	        UsuarioGrupoDeUsuario.create testUser, adminRole, true
    	}

    }

    def destroy = {
    }
}
