package br.com.arrasaamiga.auth

import grails.plugin.springsecurity.rest.*
import grails.plugin.springsecurity.rest.authentication.RestAuthenticationEventPublisher
import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.reader.TokenReader
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
/**
 * Created by lsimaocosta on 21/06/15.
 */
class MyRestTokenValidationFilter extends  RestTokenValidationFilter {

  def userDetailsService

  @Override
   void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
       HttpServletRequest httpRequest = request as HttpServletRequest
       HttpServletResponse httpResponse = response as HttpServletResponse
       AccessToken accessToken

       String clienteId = httpRequest.getParameter('cliente_id')

       try {
           accessToken = tokenReader.findToken(httpRequest)
           if (accessToken) {
               log.debug "Token found: ${accessToken.accessToken}"

               log.debug "Trying to authenticate the token"
               accessToken = restAuthenticationProvider.authenticate(accessToken) as AccessToken
               if (clienteId){
                  MyUserDetails principal = accessToken.principal
                  principal.clienteId = clienteId
               }

               if (accessToken.authenticated) {
                   log.debug "Token authenticated. Storing the authentication result in the security context"
                   log.debug "Authentication result: ${accessToken}"
                   SecurityContextHolder.context.setAuthentication(accessToken)

                   authenticationEventPublisher.publishAuthenticationSuccess(accessToken)
                   processFilterChain(request, response, chain, accessToken)
               }

           } else {
               log.debug "Token not found"
               processFilterChain(request, response, chain, accessToken)
           }
       } catch (AuthenticationException ae) {
           log.debug "Authentication failed: ${ae.message}"
           authenticationEventPublisher.publishAuthenticationFailure(ae, accessToken)
           authenticationFailureHandler.onAuthenticationFailure(httpRequest, httpResponse, ae)
       }
   }
}
