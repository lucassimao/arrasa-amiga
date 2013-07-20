import br.com.arrasaamiga.*

class BootStrap {

    def init = { servletContext ->
    	
    	def paleta180Cores = new Produto(nome:'Paleta de 180 Sombras',preco:12000)
    	paleta180Cores.descricao = 'Uma paleta completa para qualquer maquiagem: seja com look casual ou de festa... Traz cores neutras, matte e shimmer... Tudo para você arrasar!'
    	paleta180Cores.fotos = ['rsz_paleta180-1.png','paleta180-1.jpg','paleta180-2.jpg','paleta180-3.jpg','paleta180-4.jpg','paleta180-5.jpg']

    	paleta180Cores.save()



    	def lovealpha = new Produto(nome:'Máscara para Cílios Love Alpha',preco:2500)
    	lovealpha.descricao = 'Quer cílios GIGANTES??? Pois esta é a máscara para cílios que você precisa... Love Alpha: a união da máscara convencional e uma segunda máscara com fibras para dar um efeito de cílios postiços à sua makeup... '
    	lovealpha.fotos = ['rsz_lovealpha-5.png','lovealpha-2.png','lovealpha-5.jpg','lovealpha-1.png']

    	lovealpha.save()


    	def eyeliner = new Produto(nome:'Delineador em Gel',preco:1600)
    	eyeliner.descricao = 'Uma make de verdade tem que ter um belo delineado não é? E hoje temos o tão querido delineador em gel para nos ajudar nessa hora... Este já vem com o pincel específico e de ponta bem fina que permite fazer traços perfeitos... '
    	eyeliner.fotos = ['rsz_1eyeliner-1.png','eyeliner-1.jpg','eyeliner-2.jpg','eyeliner-3.jpg',
    	'eyeliner-5.jpg','eyeliner-7.jpg']

    	eyeliner.save()


    	def kitPinceisPreto = new Produto(nome:'Kit de 12 Pinceis + Estojo de Couro - Preto',preco:9500)
    	kitPinceisPreto.descricao = 'Para uma maquiagem bem feita são indispensáveis bons pincéis... Para isso temos o Kit de 12 pincéis que vem nesse lindo estojo... '
    	kitPinceisPreto.fotos = ['rsz_kitpinceispreto-1.png','kitPinceisPreto-1.jpg','kitPinceis-1.jpg','kitPinceis-2.jpg']

    	kitPinceisPreto.save()


    	def kitPinceisLilas = new Produto(nome:'Kit de 12 Pinceis + Estojo de Couro - Lilás',preco:9500)
    	kitPinceisLilas.descricao = 'Para uma maquiagem bem feita são indispensáveis bons pincéis... Para isso temos o Kit de 12 pincéis que vem nesse lindo estojo... '
    	kitPinceisLilas.fotos = ['rsz_kitpinceislilas-1.png','kitPinceisLilas-1.jpg','kitPinceis-1.jpg','kitPinceis-2.jpg']

    	kitPinceisLilas.save()


    	def kitPinceisRosa = new Produto(nome:'Kit de 12 Pinceis + Estojo de Couro - Rosa',preco:9500)
    	kitPinceisRosa.descricao = 'Para uma maquiagem bem feita são indispensáveis bons pincéis... Para isso temos o Kit de 12 pincéis que vem nesse lindo estojo... '
    	kitPinceisRosa.fotos = ['rsz_kitpinceisrosa-1.png','kitPinceisRosa-1.jpg','kitPinceis-1.jpg','kitPinceis-2.jpg']

    	kitPinceisRosa.save()

    	// mais


    	def paleta120Cores = new Produto(nome:'Paleta de 120 Sombras',preco:8000)
    	paleta120Cores.descricao = 'Para você que quer tudo de sombra em um só lugar, cores matte e shimmer... Super pigmentada, traz todos os tipos de cores que você precisa para elaborar aquela make...'
    	paleta120Cores.fotos = ['rsz_paleta120-1.png','paleta120-1.jpg','paleta120-2.jpg','paleta120-3.jpg','paleta120-4.jpg']

    	paleta120Cores.save()



    	def blushNyxCinnamon = new Produto(nome:'Blush Cinnamon da Nyx',preco:2800)
    	blushNyxCinnamon.descricao = 'Blush cor cinnamon da NYX'
    	blushNyxCinnamon.fotos = ['rsz_blushnyx.png','blushnyxcinnamon-1.png','blushnyxcinnamon-2.png',
    							'blushnyxcinnamon-3.png',
    							'blushnyxcinnamon-4.png','blushnyxcinnamon-5.png','blushnyxcinnamon-6.jpg',
    							'blushnyxcinnamon-7.png','blushnyxcinnamon-8.png','blushnyxcinnamon-9.jpg']

    	blushNyxCinnamon.save()


    	def blushNyxCooper = new Produto(nome:'Blush Cooper da Nyx',preco:2800)
    	blushNyxCooper.descricao = 'Blush cor Cooper da NYX'
    	blushNyxCooper.fotos = ['rsz_blushnyxcooper-3.png','blushnyxcooper-1.jpg','blushnyxcooper-2.jpg','blushnyxcooper-3.png',
    							'blushnyxcooper-4.png']

    	blushNyxCooper.save()


    	def blushNyxRoseGarden = new Produto(nome:'Blush Rose Garden da Nyx',preco:2800)
    	blushNyxRoseGarden.descricao = 'Blush cor Rose Garden da NYX'
    	blushNyxRoseGarden.fotos = ['rsz_blushnyxrosegarden-1.png','blushnyxrosegarden-1.png','blushnyxrosegarden-2.png',
    							'blushnyxrosegarden-3.png',
    							'blushnyxrosegarden-4.jpg','blushnyxrosegarden-5.jpg','blushnyxrosegarden-6.png','blushnyxrosegarden-7.jpg']

    	blushNyxRoseGarden.save()


    	// mais
    	
    	def blush = new Produto(nome:'Paleta Blush - 10 cores',preco:6000)
    	blush.descricao = 'Para você que é loooouca por blush... Temos a opção da paleta de 10 blushes que possui tonalidades em rosa, pêssego, laranja, bronze, pink... Com cores cintilantes e matte... Para compor sua make da forma que você desejar...'
    	blush.fotos = ['rsz_blush-1.png','blush-2.png','blush-3.png','blush-4.jpg','blush-1.png',
    							'blush-5.png','blush-6.jpg','blush-7.png']

    	blush.save()

    	
    	
    	def blushMosaicNyx = new Produto(nome:'Blush Mosaic Love - Nyx',preco:4500)
    	blushMosaicNyx.descricao = 'O que dizer dos blushes mosaico da NYX? São maravilhosos: pigmentação incrível e cintilância fascinante... Temos disponível a cor "Love", um tom alaranjado ideal para sua make...'
    	blushMosaicNyx.fotos = ['rsz_mosaiclove-1.png','mosaiclove-1.jpg','mosaiclove-2.jpg',
    					'mosaiclove-3.JPG','mosaiclove-4.png','mosaiclove-5.png']

    	blushMosaicNyx.save()


    	def esponja = new Produto(nome:'Esponja Gota',preco:1600)
    	esponja.descricao = 'Uma ótima opção para aplicação de bases e corretivos. Em formato anatômico e '+
    						'com uma textura macia permite espalhar o produto de forma uniforme, deixando ' +
    						'um acabamento mais bonito que o pincel... Sem falar que é compacta, dá para ' +
    						'levar sempre com você... E além disso eh uma fofura neh? Com cores para todos ' +
    						'os gostos... Não perde tempo amiga! Compre antes que acabe o estoque! '

    	esponja.fotos = ['rsz-esponja-1.png','esponja-1.png','esponja-2.png','esponja-3.png','esponja-4.png',
    					'esponja-5.png','esponja-6.png','esponja-7.png','esponja-8.png','esponja-9.png',
    					'esponja-10.png','esponja-11.png']

    	esponja.save()




    }

    def destroy = {
    }
}
