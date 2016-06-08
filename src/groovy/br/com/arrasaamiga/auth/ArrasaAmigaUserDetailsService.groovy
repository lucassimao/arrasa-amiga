package br.com.arrasaamiga.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.*
import grails.plugin.springsecurity.userdetails.*
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.transaction.Transactional

/**
 * Created by lsimaocosta on 21/06/15.
 */
class ArrasaAmigaUserDetailsService extends  GormUserDetailsService {

  protected UserDetails createUserDetails(user, Collection<GrantedAuthority> authorities) {
    def grailsUser = super.createUserDetails(user,authorities)

    return new ArrasaAmigaUserDetails(grailsUser.username, grailsUser.password,
        grailsUser.enabled, grailsUser.accountNonExpired,
        grailsUser.credentialsNonExpired,grailsUser.accountNonLocked,
        grailsUser.authorities,grailsUser.id)
    }
}
