package br.com.arrasaamiga.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.*
import grails.plugin.springsecurity.userdetails.*
/**
 * Created by lsimaocosta on 21/06/15.
 */
class MyUserDetails extends  GrailsUser {

  String clienteId

  MyUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
  	           boolean credentialsNonExpired, boolean accountNonLocked,
  	           Collection<GrantedAuthority> authorities, id){
           super(username, password, enabled, accountNonExpired, credentialsNonExpired,
       				accountNonLocked, authorities,id)
  }
}
