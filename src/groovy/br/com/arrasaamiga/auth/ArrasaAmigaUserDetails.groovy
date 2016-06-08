package br.com.arrasaamiga.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.*
import grails.plugin.springsecurity.userdetails.*


/**
  * Classe que extende a classe GrailsUser
  * apenas para armazenar o clienteId enviado
  * no cabeçalho das requisições rest pelos
  * clientes
  */
class ArrasaAmigaUserDetails extends  GrailsUser {

    String clienteId

    ArrasaAmigaUserDetails(String username, String password, boolean enabled,
                  boolean accountNonExpired, boolean credentialsNonExpired,
                  boolean accountNonLocked, Collection<GrantedAuthority> authorities, id){

                    super(username, password, enabled, accountNonExpired,
                      credentialsNonExpired,accountNonLocked, authorities,id)
    }

}
