package br.com.arrasaamiga

class Usuario {

	transient springSecurityService

	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static transients = ['admin']
	static hasMany = ['usuarioGrupoDeUsuarios':UsuarioGrupoDeUsuario]

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
	}

	static namedQueries = {
		vendedores {
			usuarioGrupoDeUsuarios{
				grupoDeUsuario{
					'in'('authority',['ROLE_ADMIN','ROLE_VENDEDOR'])
				}
			}
		}
	}

	Set<GrupoDeUsuario> getAuthorities() {
		UsuarioGrupoDeUsuario.findAllByUsuario(this).collect { it.grupoDeUsuario } as Set
	}

	boolean isAdmin(){
		return this.authorities.any{ GrupoDeUsuario grupo-> grupo.authority.equals('ROLE_ADMIN') }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
