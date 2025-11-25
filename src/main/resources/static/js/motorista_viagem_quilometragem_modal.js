$(document).ready(() => {

    const removerOuAddInsercaoFinalizar = () => {
        const veiculoSelect = document.getElementById("veiculoSelect");
        
        if (veiculoSelect.children.length <= 1) {

            //$('#infoKmInsercao').addClass('hide');
            $('#btn_modal_adicionar_quilometragem_').removeClass('hide');

        } else {

            //$('#infoKmInsercao').removeClass('hide');
            $('#btn_modal_adicionar_quilometragem_').addClass('hide');
        }
    };

    const table = $('#veiculos_km').DataTable({
        paging: false,
        columns: [{
                data: 'id'
            },
            {
                data: 'descricaoVeiculo'
            },
            {
                data: 'quilometro'
            },
            {
                data: "",
                render: (data, type, row, meta) => {
                    return `<button data-row='${meta.row}' class='btn bg-green btn-xs'> <i class='fa fa-edit'></i> Editar</button>`;
                }
            }
        ],
        createdRow: function (row, data, index) {
            $('td', row).eq(2).addClass('text-right');
        },
        data: []
    });

    $('#veiculos_km tbody').on('click', 'td button', function (evt) {

        const index = $(this).data('row');
        const table = $('#veiculos_km').DataTable();
        const data = table.row(index).data();
        const veiculoSelect = document.getElementById("veiculoSelect");
        const option = document.createElement("option");

        table.row(index).remove().draw();
        
        option.text = data.descricaoVeiculo;
        option.value = data.veiculoId;
        option.dataset.id_quilometragem = data.id;

        veiculoSelect.add(option);
        veiculoSelect.value = data.veiculoId;

        $('#km_input').val(data.quilometro);
    });

    $('#btnInserirKm').on('click', function () {

        if ($('#veiculoSelect').val() != "" && $('#km_input').val() != "") {

            const veiculo_id = $('#veiculoSelect').val();
            const viagem_id = $('#veiculoSelect').data('viagem-id');

            const data = {
                id: $('#veiculoSelect option:selected').data('id_quilometragem'),
                quilometro: $('#km_input').val(),
                tipoQuilometragem: $('#veiculoSelect').data('tipo-quilometragem')
            };

            $('#loaderModal').modal('show');

            $.ajax({
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                url: `/dashboard/api/viagens/quilometragem/${viagem_id}/${veiculo_id}`,
                data: JSON.stringify(data),
                success: function (result) {

                    table.row.add(result.data).draw();

                    $('#veiculoSelect option:selected').remove();

                    $('#alertValidated').addClass('hide');
                    $('#km_input').val('');
                    $('#veiculoSelect').val('');

                    removerOuAddInsercaoFinalizar();
                },
                error: function (e) {

                    console.log(e);
                    alert('Não foi possível realizar esta ação :(');
                },
                done: function (e) {

                    console.info('end');
                },
                complete: function() {

                    $('#loaderModal').modal('hide');
                }
            });

        } else {

            $('#alertValidated').removeClass('hide');
        }
    });

    $('#btn_modal_adicionar_quilometragem_').on('click', function(evt) {

        const isEncerramento = $(this).data('is_encerramento');
        
        if(!isEncerramento) {

            document.location.href = "/dashboard/motorista/viagem/detalhes/iniciar/" + $('#veiculoSelect').data('viagem-id');

        } else {

            $('#modal_adicionar_quilometragem').modal('hide');
            $('#observacaoViagemModal').modal();
        }
    });

    $("#modal_adicionar_quilometragem").on('shown.bs.modal', function () {
        $.fn.dataTable.tables({
            visible: true,
            api: true
        }).columns.adjust();
    });
});