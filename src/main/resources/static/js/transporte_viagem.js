$(document).ready(() => {
    
    var maxLength = 254;

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

    $('#tarefa_descricao').keyup(() => {

        var length = $('#tarefa_descricao').val().length;
        var length = maxLength - length;

        $('#qtd_caracteres').text(length);
    });

    $(document).ready(() => {

        const tables = $('table');

        for (var i = 0; i < tables.length; i++) {
            $(tables[i]).DataTable();
        }
    });

    $('.btn_ver_tarefa_list').on('click', function(evt) {

        const tarefa_id = $(this).parents('tr').data().id;

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

    $('.btn_editar_tarefa_list').on('click', function(evt) {

        const tarefa_id = $(this).parents('tr').data().id;

        findTarefa(tarefa_id, (tarefa) => {

            $('#tarefa_id').val(tarefa.id);
            $('#tarefa_titulo').val(tarefa.titulo);
            $('#tarefa_descricao').val(tarefa.descricao);

            $('#qtd_caracteres').text(maxLength - tarefa.descricao.length);
            $('#adicionar_tarefa_modal').modal('show');
        });
    });
});
