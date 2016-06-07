import br.com.arrasaamiga.VendaMarshaller
import br.com.arrasaamiga.auth.*
import grails.plugin.springsecurity.SpringSecurityUtils

// Place your Spring DSL code here
beans = {
    vendaMarchaller(VendaMarshaller)
    def conf = grailsApplication.config.grails.plugin.springsecurity

    restTokenValidationFilter(MyRestTokenValidationFilter) {
        headerName = conf.rest.token.validation.headerName
        validationEndpointUrl = conf.rest.token.validation.endpointUrl
        active = conf.rest.token.validation.active
        enableAnonymousAccess = conf.rest.token.validation.enableAnonymousAccess
        tokenReader = ref('tokenReader')
        authenticationSuccessHandler = ref('restAuthenticationSuccessHandler')
        authenticationFailureHandler = ref('restAuthenticationFailureHandler')
        restAuthenticationProvider = ref('restAuthenticationProvider')
        authenticationEventPublisher = ref('authenticationEventPublisher')
    }

    userDetailsService(MyUserDetailsService){
      grailsApplication = ref('grailsApplication')
    }
}
