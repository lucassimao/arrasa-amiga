package br.com.arrasaamiga

import org.springframework.dao.DataIntegrityViolationException
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.*

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import grails.converters.JSON
import org.apache.commons.codec.binary.Base64
import groovy.sql.Sql
import grails.util.Environment
import grails.util.BuildSettingsHolder
import groovy.json.JsonBuilder
import grails.util.Environment


@Secured(['permitAll'])
class ProdutoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    
    def grailsApplication
    def springSecurityService
    def dataSource

    def getTags(){
        def sql = new Sql(dataSource)

        
        String searchKey = "%${params.term}%"
        def rows = sql.rows("select distinct pw.keywords_string from produto_keywords pw where lower(pw.keywords_string) like lower(:search)",
                            [search:searchKey])

        def tags = [:]

        rows.eachWithIndex{row, index-> tags[index] = row.keywords_string }
        render tags as JSON
    }

    private String getGruposDeProdutos(){
        def grupos = []
        def builder = new JsonBuilder()

        
        GrupoDeProduto.list().each{grupo->
            grupos<< ['id': grupo.id,'label': grupo.nome]
        }

        builder(grupos)

        return builder.toString()
    }


    def addNewUnidade(String unidade){
        render(template:'addNewUnidade',model:[unidade:unidade])
    }

    def asyncFotoUpload(){

        String uploadDir = getUploadDir()

        def multipartFile = request.getFile('foto')
        if (multipartFile) {

            String originalFilename = "img${System.currentTimeMillis()}${multipartFile.originalFilename}"

            multipartFile.transferTo(new File(uploadDir + File.separator + originalFilename))
            def foto = new FotoProduto(arquivo: originalFilename)

            render(template: 'addNewFoto', model: [foto: foto])
        }else{
            render status: 400, text: 'A imagem deve ser enviada'
        }
    }


    @Secured(['ROLE_ADMIN'])
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(['ROLE_ADMIN'])
    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        
        [produtoInstanceList: Produto.executeQuery('from Produto p order by visivel desc,ordem asc',[],params), produtoInstanceTotal: Produto.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        def produtoInstance = new Produto(params)
        produtoInstance.visivel = true

        [produtoInstance: produtoInstance,gruposDeProdutos: getGruposDeProdutos()]
    }

    @Secured(['ROLE_ADMIN'])
    def save() {

        def unidades = (params.unidades)?JSON.parse(params.unidades):[]
        def fotos = (params.fotosUnidades)?JSON.parse(params.fotosUnidades):[]
        def comentarios = (params.fotoComentario)?JSON.parse(params.fotoComentario):[]


        // corrigindo os grupos
        def pattern = ~/_grupos\[\d+\-a\]/
        def paramsGroups = params.keySet().findAll{p->  return (pattern.matcher(p).matches()) }

        int count = 0
        def pattern2 = ~/\d+/
        paramsGroups.each{p->
            params["grupos[${count}].id"] = pattern2.matcher(p)[0]
            ++count
        }


        def produtoInstance = new Produto(params)
        produtoInstance.keywords = params.list('palavrasChave[]')
        produtoInstance.fotos = []
        produtoInstance.unidades = []

        unidades.each{ unidade->
            produtoInstance.addToUnidades(unidade.trim())

            fotos[unidade].eachWithIndex{ foto, index ->
                def fotoProduto = new FotoProduto();
                
                fotoProduto.comentario = comentarios[foto]?.trim();
                fotoProduto.posicao = index
                fotoProduto.unidade = unidade.trim()
                fotoProduto.arquivo = foto.trim()

                produtoInstance.addToFotos(fotoProduto)
            }
        }
        
        def multipartFileMiniatura = request.getFile('fotoMiniaturaFile')
        if (multipartFileMiniatura)
            produtoInstance.fotoMiniatura = "img${System.currentTimeMillis()}${multipartFileMiniatura.originalFilename}"

        if (!produtoInstance.save(flush: true)) {
            produtoInstance.fotoMiniatura = ''
            produtoInstance.fotos = []
            render(view: "create", model: [produtoInstance: produtoInstance])
            return
        }

        String uploadDir = getUploadDir()

        if (multipartFileMiniatura)
            multipartFileMiniatura.transferTo(new File(uploadDir + File.separator + produtoInstance.fotoMiniatura))


       produtoInstance.unidades.each{ un->

            def estoque = new Estoque()

            estoque.produto = produtoInstance
            estoque.unidade = un
            estoque.quantidade = 0 // qtde inicial

            estoque.save(flush:true)

        }


        flash.message = "Produto cadastrado"
        redirect(action: "show", id: produtoInstance.id)
        
    }

    //Apresenta detalhes do produto ao administrador
    @Secured(['ROLE_ADMIN'])
    def show(Long id) {
        def produtoInstance = Produto.get(id)
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
            return
        }

        [produtoInstance: produtoInstance]
    }



    @Secured(['ROLE_ADMIN'])
    def edit(Long id) {
        def produtoInstance = Produto.get(id)
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
            return
        }

        [produtoInstance: produtoInstance,gruposDeProdutos: getGruposDeProdutos()]
    }



    @Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
        def produtoInstance = Produto.get(id)

        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (produtoInstance.version > version) {
                produtoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'produto.label', default: 'Produto')] as Object[],
                          "Another user has updated this Produto while you were editing")
                render(view: "edit", model: [produtoInstance: produtoInstance])
                return
            }
        }


        produtoInstance.keywords = params.list('palavrasChave[]')
        
        // adicionando novas tags
        def patternKeywords = ~/palavrasChave\[\d+\-a\]/
        def paramsTags = params.keySet().findAll{p->  return (patternKeywords.matcher(p).matches()) }
        paramsTags.each{ key->
            produtoInstance.keywords << params[key]
        }


        // atualizando os grupos
        def patternGrupos = ~/_grupos\[\d+\-a\]/
        def paramsGroups = params.keySet().findAll{p->  return (patternGrupos.matcher(p).matches()) }

        int count = 0
        def pattern2 = ~/\d+/
        paramsGroups.each{p->
            params["grupos[${count}].id"] = pattern2.matcher(p)[0]
            ++count
        }

        produtoInstance.grupos = []
        bindData(produtoInstance, params, [include: ['grupos','visivel','nome','descricao','tipoUnitario','marca']])
        assert count == produtoInstance.grupos?.size()

        def miniaturaAnterior = produtoInstance.fotoMiniatura
        def multipartFileMiniatura = request.getFile('fotoMiniaturaFile')

        if (multipartFileMiniatura)
            produtoInstance.fotoMiniatura =  multipartFileMiniatura.originalFilename
        else
             produtoInstance.fotoMiniatura = miniaturaAnterior


        produtoInstance.fotos*.delete()
        produtoInstance.fotos.clear()
        produtoInstance.unidades.clear()

        def unidades = (params.unidades)?JSON.parse(params.unidades):[]
        def fotos = (params.fotosUnidades)?JSON.parse(params.fotosUnidades):[]
        def comentarios = (params.fotoComentario)?JSON.parse(params.fotoComentario):[]

        unidades.each{ unidade -> 


            if (!Estoque.findByProdutoAndUnidade( produtoInstance, unidade ) ){
                
                def estoque = new Estoque()

                estoque.produto = produtoInstance
                estoque.unidade = unidade
                estoque.quantidade = 0 // qtde inicial

                estoque.save(flush:true)
            }

            produtoInstance.addToUnidades(unidade)

            fotos[unidade].eachWithIndex{ arquivoFoto, index ->

                def fotoProduto = new FotoProduto()
                
                fotoProduto.comentario = comentarios[arquivoFoto]?.trim()
                fotoProduto.posicao = index
                fotoProduto.unidade = unidade
                fotoProduto.arquivo = arquivoFoto    

                produtoInstance.addToFotos(fotoProduto)

            }

        }

        if (!produtoInstance.save(flush: true)) {
            render(view: "edit", model: [produtoInstance: produtoInstance])
            return
        }

         
        if (multipartFileMiniatura){
            String uploadDir =  getUploadDir()
            multipartFileMiniatura.transferTo(new File(uploadDir + File.separator + produtoInstance.fotoMiniatura))
        }

        flash.message = "Produto atualizado"
        redirect(action: "show", id: produtoInstance.id)

    }

    @Secured(['ROLE_ADMIN'])
    def delete(Long id) {
        def produtoInstance = Produto.get(id)
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
            return
        }

        try {
            produtoInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(action: "show", id: id)
        }
    }



    //Apresenta detalhes do produto ao comprador
    def detalhes(Long id) {
        def produtoInstance = Produto.get(id)
        def user = springSecurityService.currentUser
        def cliente = Cliente.findByUsuario(user)
        
        if (!produtoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'produto.label', default: 'Produto'), id])
            redirect(uri: "/", absolute:true)
            return
        }

        def estoques = produtoInstance.getEstoques()
        // ordenando em ordem decrescente de quantidade
        List unidades = estoques.sort{ e1, e2-> e2.quantidade <=> e1.quantidade }.collect{ it.unidade } 

        [produtoInstance: produtoInstance,unidades: unidades,
            shoppingCart: session.shoppingCart,
            estoques: estoques,cliente:cliente, grupoRaiz: produtoInstance.grupoPadrao?.getGrupoRaiz()?.nome]
    }


    def salvarAviso(Long id){
        
        //Facebook App info
        String fbSecretKey;
        String fbAppId;


        switch (Environment.current) {
            case Environment.DEVELOPMENT:
                fbSecretKey = "43e210a33b692fd745392a8d4a6b92ec"
                fbAppId = "538200826283779"
                break
            case Environment.PRODUCTION:
                fbSecretKey = "d1392b8e735e984825ea27846a7ee107"
                fbAppId = "592257150816024"
        }



        //it is important to enable url-safe mode for Base64 encoder 
        Base64 base64 = new Base64(true);

        //split request into signature and data
        String[] signedRequest = params.signed_request.split("\\.", 2);

        //parse signature
        String sig = new String(base64.decode(signedRequest[0].getBytes("UTF-8")));

        //parse data and convert to json object
        def data = JSON.parse(new String(base64.decode(signedRequest[1].getBytes("UTF-8"))))

        //check signature algorithm
        if(!data.algorithm.equals("HMAC-SHA256")) {
            //unknown algorithm is used
            return null;
        }

        //check if data is signed correctly
        if(!hmacSHA256(signedRequest[1], fbSecretKey).equals(sig)) {
            //signature is not correct, possibly the data was tampered with
            return null;
        }

        //def oauth_token = data.oauth_token

        def aviso = new Aviso()
        aviso.nome = data.registration.name
        aviso.email = data.registration.email
        aviso.celular = data.registration.celular
        aviso.produto = Produto.load(data.registration.idProduto)
        aviso.unidade = data.registration.unidade
        aviso.facebookUserId = data.user_id

        aviso.save(flush:true)

        flash.info = 'Amiga, avisaremos você assim que novas unidades chegarem !!'
        redirect(uri:aviso.produto.nomeAsURL)

    }

    //HmacSHA256 implementation 
    private String hmacSHA256(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] hmacData = mac.doFinal(data.getBytes("UTF-8"));
        return new String(hmacData);
    }




    def quantidadeEmEstoque(Long produtoId,String unidade) {

        def produtoInstance = Produto.get(produtoId)
        
        if (!produtoInstance) {
            render "erro"
            throw new Exception("Erro ao carregar produto com id ${produtoId} em ProdutoController#quantidadeEmEstoque")
        }

        if (!unidade){
            render "erro"
            throw new Exception("Alguem tentanto carregar o produto ${produtoId} sem unidade ....")
        }

        if (!produtoInstance.unidades.contains(unidade)){
            render "erro"
            throw new Exception("Não existe a unidade ${params.unidade} no produto ${produtoId} ....")
        }

        try{
            def quantidade = produtoInstance.getQuantidadeEmEstoque(unidade)

            boolean marcadoParaAvisar = false

            if (quantidade == 0){
                def user = springSecurityService.currentUser
                
                if (user){
                    def cliente = Cliente.findByUsuario(user)
                    marcadoParaAvisar = ( Aviso.findByProdutoAndEmailAndUnidade(produtoInstance,cliente.email,unidade) != null )
                }
            }

            render (['quantidade': quantidade, 'marcadoParaAvisar': marcadoParaAvisar] as JSON)

        }catch(Exception e){
            render "erro"
            throw e
        }
       
         
    }


    private String getUploadDir(){
        return grailsApplication.mainContext.getResource('images/produtos').file.absolutePath
    }

}
