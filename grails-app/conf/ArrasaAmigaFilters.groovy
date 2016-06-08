import br.com.arrasaamiga.auth.ArrasaAmigaUserDetails

class ArrasaAmigaFilters {

    def springSecurityService

    def filters = {
        /**
          * Esse filtro intercepta restful endpoints e procura
          * pelo header <code>clienteId</code> que deve ser enviado
          * pelos clientes para ser utilizado possivelmente adiante pelo GcmService
          */
        setClienteId(uri:'/api/**'){

            before = {
                String clienteId = request.getHeader('clienteId')

                if (clienteId){
                    ArrasaAmigaUserDetails principal = springSecurityService.principal
                    principal.clienteId = clienteId
                }else
                    log.warn "${request.requestURL} não encontem header clienteId"
            }

        }
    }
}
