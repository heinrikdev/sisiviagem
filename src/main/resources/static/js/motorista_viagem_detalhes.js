$(document).ready(() => {

    const findTarefa = (tarefa_id, callback) => {

        $('#loaderModal').modal('show');
        const url = `/dashboard/api/tarefa/${tarefa_id}`;

        $.get(url, (data) => {
                callback(data);
            })
            .fail(() => {
                alert('woops\nNão foi possível realizar esta ação :(, verifique sua internet.');
            })
            .always(() => {
                $('#loaderModal').modal('hide');
            });
    };

    $('#btnIniciarViagem').on('click', function (evt) {

        const viagem_id = $(evt.target).data('viagem-id');
        const isKmInicialSetado = $(evt.target).data('km-inicial-inserido');

        if (!isKmInicialSetado) {

            $('#modal_adicionar_quilometragem').modal();
            
        } else {

            document.location.href = "/dashboard/motorista/viagem/detalhes/iniciar/" + viagem_id;
        }
    });

    $('#btnEncerrarViagem').on('click', function (evt) {
        
        const isKmFinalSetado = $(evt.target).data('km-final-inserido');
        console.log('KM FINAL: ', isKmFinalSetado);
        if (!isKmFinalSetado) {

            $('#modal_adicionar_quilometragem').modal();

        } else {

            $('#observacaoViagemModal').modal();
        }
    });

    $('.status_tarefa[data-status=0]').val('concluida')
    $('.status_tarefa[data-status=1]').val('nao_realizada');

    $('.status_tarefa').change((event) => {

        if ($('.status_tarefa').val() != '-1') {

            $('#loaderModal').modal('show');

            const target = $(event.target);

            const value = target.val();
            const tarefa_id = target.attr('data-tarefa');
            const label_status = (value === 'concluida') ? 'label label-success' : 'label label-danger';
            const label_status_text = (value === 'concluida') ? 'Concluída' : 'Não Realizada';
            const url = `/dashboard/api/tarefa/${value}/${tarefa_id}`;

            $.get(url, () => {

                    $(`#${tarefa_id}`).removeClass();
                    $(`#${tarefa_id}`).addClass(label_status);
                    $(`#${tarefa_id}`).empty().append(label_status_text);

                })
                .fail(() => {

                    alert('woops\nNão foi possível realizar esta ação :(');
                })
                .always(() => {

                    $('#loaderModal').modal('hide');
                });
        }
    });

    $('.btn_avaliar_tarefa_list').on('click', function(evt) {

        const tarefa_id = $(this).parents('tr').data().id;

        findTarefa(tarefa_id, (tarefa) => {

            $('#tarefa_id').val(tarefa.id);
            $('#avaliar_tarefa_titulo').text(tarefa.titulo);
            $('#avaliar_tarefa_descricao').text(tarefa.descricao);

            $('#avaliar_tarefa_modal').modal('show');
        });
    });

    $('#btn_ver_tarefa_list').on('click', () => {

        const tarefa_id = $('#btn_ver_tarefa_list').parents('tr').data().id;

        findTarefa(tarefa_id, (tarefa) => {

            $('#detalhes_tarefa_nome').text(tarefa.titulo);
            $('#detalhes_tarefa_status').text(tarefa.estadoTarefa);
            $('#detalhes_tarefa_status').removeClass();
            $('#detalhes_tarefa_status').addClass('label ' + tarefa.labelColorStatus)
            $('#detalhes_tarefa_descricao').text(tarefa.descricao);
            $('#detalhes_tarefa_observacao').text(tarefa.observacao != null ? tarefa.observacao : '-----');

            $('#detalhes_tarefa_modal').modal('show');
        });
    });

    $('.impressao_minima').on('click', (event) => {

        const viagem_id = $(event.target).attr('data-viagem');
        const rootUrl = window.location.href.replace(window.location.pathname, '');

        let url_impressao = rootUrl + `/dashboard/viagem/imprimir_lista_passageiros/${viagem_id}`;

        win = window.open(url_impressao, '_blank');

        win.onafterprint = () => {
            win.close();
        };
    });

    const tables = $('table:not(#veiculos_km)');

    for (var i = 0; i < tables.length; i++) {
        $(tables[i]).DataTable();
    }
});