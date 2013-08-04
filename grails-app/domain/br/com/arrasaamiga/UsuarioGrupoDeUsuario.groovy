package br.com.arrasaamiga

import org.apache.commons.lang.builder.HashCodeBuilder

class UsuarioGrupoDeUsuario implements Serializable {

	Usuario usuario
	GrupoDeUsuario grupoDeUsuario

	boolean equals(other) {
		if (!(other instanceof UsuarioGrupoDeUsuario)) {
			return false
		}

		other.usuario?.id == usuario?.id &&
			other.grupoDeUsuario?.id == grupoDeUsuario?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (usuario) builder.append(usuario.id)
		if (grupoDeUsuario) builder.append(grupoDeUsuario.id)
		builder.toHashCode()
	}

	static UsuarioGrupoDeUsuario get(long usuarioId, long grupoDeUsuarioId) {
		find 'from UsuarioGrupoDeUsuario where usuario.id=:usuarioId and grupoDeUsuario.id=:grupoDeUsuarioId',
			[usuarioId: usuarioId, grupoDeUsuarioId: grupoDeUsuarioId]
	}

	static UsuarioGrupoDeUsuario create(Usuario usuario, GrupoDeUsuario grupoDeUsuario, boolean flush = false) {
		new UsuarioGrupoDeUsuario(usuario: usuario, grupoDeUsuario: grupoDeUsuario).save(flush: flush, insert: true)
	}

	static boolean remove(Usuario usuario, GrupoDeUsuario grupoDeUsuario, boolean flush = false) {
		UsuarioGrupoDeUsuario instance = UsuarioGrupoDeUsuario.findByUsuarioAndGrupoDeUsuario(usuario, grupoDeUsuario)
		if (!instance) {
			return false
		}

		instance.delete(flush: flush)
		true
	}

	static void removeAll(Usuario usuario) {
		executeUpdate 'DELETE FROM UsuarioGrupoDeUsuario WHERE usuario=:usuario', [usuario: usuario]
	}

	static void removeAll(GrupoDeUsuario grupoDeUsuario) {
		executeUpdate 'DELETE FROM UsuarioGrupoDeUsuario WHERE grupoDeUsuario=:grupoDeUsuario', [grupoDeUsuario: grupoDeUsuario]
	}

	static mapping = {
		id composite: ['grupoDeUsuario', 'usuario']
		version false
	}
}
