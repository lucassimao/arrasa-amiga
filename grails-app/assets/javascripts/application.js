//= require jquery
//= require jquery-ui
//= require bootstrap.js
//= require masonry.pkgd.min.js
//= require imagesloaded.pkgd.min.js
//= require jquery.raty.js
//= require_self


(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','//www.google-analytics.com/analytics.js','ga');

ga('create', 'UA-43736713-1', 'arrasaamiga.com.br');
ga('send', 'pageview');

$(document).ready(function() {

    var path = (location.hostname == 'localhost')?'/arrasa-amiga/':'/' ;
    var isDevelopmentEnviroment = (location.hostname == 'localhost');

    $.ajaxSetup({ cache: true });

    $.getScript('//connect.facebook.net/pt_BR/sdk.js', function(){
        FB.init({
            appId: (isDevelopmentEnviroment)?'538200826283779':'592257150816024',
            xfbml      : true,
            version    : 'v2.0'
        });
    });




    $(".star-rating").raty({path: path+'assets',readOnly:true,
        score: function() { return $(this).attr('data-score'); }});

    $('.thumbnail img').hover(function() {
        $(this).addClass('transition');

    }, function() {
        $(this).removeClass('transition');
    });

    $('.carousel').carousel();
    $("[rel=tooltip]").tooltip();

    var $container = $('.product-container');
    $container.imagesLoaded( function(){
        $container.masonry();
    });


    $(".btnComoComprar").click(function(){

        var modal = $('.modal');

        if (modal.length > 0){
            $(modal).modal();
        }else{

            $.ajax({
                url: $(this).data('target'),
                settings: {'cache':true}
            }).success(function( data, textStatus, jqXHR ) {
                var modal = $(data);
                $('body').append(modal);
                $(modal).filter('.modal').modal();

            }).fail(function(){
                window.location = 'http://www.arrasaamiga.com.br';
            });

        }

    })


});