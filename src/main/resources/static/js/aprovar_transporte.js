$(document).ready(() => {

    let viagemSelecionadaID;
    
    const modalViagemDetalhes = {
        reference: $('#viagemDetalhes'),
        titulo: $('#viagemDetalhesTitulo'),
        viagem: {
            descricao: $('#viagemDescricao'),
            titulo: $('#viagemTitulo')
        },
        requisicao: {
            justificativa: $('#requisicaoJustificativo'),
            titulo: $('#requisicaoTitulo')
        }
    }

    $('.btn-descricao-viagem').on('click', (event) => {

        let id                  = $(event.target).data("id");
        let viagem              = viagens.find(el => el.id === id);
        let requisicaoTitulo    = 'Requisição ' + requisicao.id + ' - ' +  requisicao.proposto;
        let viagemTitulo        = 'Viagem ' + viagem.id + ' - ' + viagem.titulo;

        viagemSelecionadaID     = id;

        modalViagemDetalhes.viagem.descricao.html(viagem.descricao);
        modalViagemDetalhes.viagem.titulo.html(viagemTitulo);

        modalViagemDetalhes.requisicao.justificativa.html(requisicao.justificativa);
        modalViagemDetalhes.titulo.html(requisicaoTitulo);

        modalViagemDetalhes.reference.modal('show');
    });

    $('#aprovarRequisicao').on('click', (event) => {
        const URL_APROVAR = `/dashboard/requisicoes/avaliar/${requisicao.id}/aprovar/${viagemSelecionadaID}`
        window.location = URL_APROVAR;
    });

    const tables = $('table');

    for (var i = 0; i < tables.length; i++) {
        $(tables[i]).DataTable({
            scrollY: '200px',
            scrollCollapse: true,
            searching: true,
            paging: true,
        });
    }
});