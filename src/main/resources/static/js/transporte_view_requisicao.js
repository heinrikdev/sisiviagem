$(document).ready(() => {

    const modalViagemDetalhes = {
        reference: $('#viagemDetalhes'),
        titulo: $('#viagemDetalhesTitulo'),
        viagem: {
            descricao: $('#viagemDescricao'),
            titulo: $('#viagemTitulo')
        }
    };

    $('.btn-descricao-viagem').on('click', (event) => {

        let id                  = $(event.target).data("id");
        let titulo              = $(event.target).data("titulo");
        let descricao           = $(event.target).data("descricao");
        let viagemTitulo        = 'Viagem ' + id + ' - ' + titulo;

        modalViagemDetalhes.viagem.descricao.html(descricao);
        modalViagemDetalhes.titulo.html(viagemTitulo);
        
        modalViagemDetalhes.reference.modal('show');
    });
});