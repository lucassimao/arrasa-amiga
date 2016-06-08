import br.com.arrasaamiga.VendaMarshaller
import br.com.arrasaamiga.auth.*
import grails.plugin.springsecurity.SpringSecurityUtils

// Place your Spring DSL code here
beans = {
    // vendaMarchaller(VendaMarshaller)

    userDetailsService(ArrasaAmigaUserDetailsService){
      grailsApplication = ref('grailsApplication')
    }
}
