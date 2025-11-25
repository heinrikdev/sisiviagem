$(document).ready(() => {

    var mensagem_views = ['#transporte_msg', '#diaria_msg', '#passagem_msg'];

    let view_id = $('.mensagem_requisicao_btn:first').attr('id');
    $(mensagem_views[view_id]).removeClass('hide');
    
    $('.mensagem_requisicao_btn').on('click', (event) => {

        view_id = $(event.target).attr('id');

        $('.view_chat').addClass('hide');
        $(mensagem_views[view_id]).removeClass('hide');
    });
});