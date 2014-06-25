import br.com.arrasaamiga.*

import grails.util.Environment
import grails.util.BuildSettingsHolder

class BootStrap {

    def grailsApplication

    def init = { servletContext ->


        Environment.executeForCurrentEnvironment {

            production {
                File uploadDir = grailsApplication.mainContext.getResource('images/produtos').file
                if (!uploadDir.exists()){
                    
                    boolean dirCreated = uploadDir.mkdirs()
                    if (!dirCreated)
                        throw new Error('Diretorio de imagens de produtos não foi criado')
                }
            }

            development {
                def grailsSettings = BuildSettingsHolder.settings
                String separator = File.separator
                String baseDir = grailsSettings.baseDir.absolutePath
                String assetsFolder = "${baseDir}${separator}grails-app${separator}assets${separator}" 
                String uploadDir = "${assetsFolder}images${separator}produtos"
                def dir = new File(uploadDir) 

                if (!dir.exists()){
                    
                    boolean dirCreated = dir.mkdirs()
                    if (!dirCreated)
                        throw new Error('Diretorio de imagens de produtos não foi criado')
                }


                def adminRole = new GrupoDeUsuario(authority: 'ROLE_ADMIN').save(flush: true)
                def userRole = new GrupoDeUsuario(authority: 'ROLE_CLIENTE').save(flush: true)

                def testUser = new Usuario(username: 'me', enabled: true, password: '123')
                testUser.save(flush: true)

                UsuarioGrupoDeUsuario.create testUser, adminRole, true


            }
        }


    }

    def destroy = {
    }
}
