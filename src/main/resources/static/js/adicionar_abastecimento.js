$(document).ready(() => {

    $('#valor').mask('#.##0,00', {reverse: true});
    $('#litros').mask('#.##0,00', {reverse: true});
    $('#odometro').mask('#.##0', {reverse: true});

    const fetchAbastecimento = (abastecimento_id, callback) => {

        $('#loaderModal').modal('show');
        const url = `/dashboard/api/viagens/abastecimentos/byId/${abastecimento_id}`;

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

    $('#cancelar_adicionar_abastecimento_modal').on('click', () => {
        $('#formAbastecimentoModal').trigger("reset");
        $('#adicionar_abastecimento_modal').modal('hide');
    });

    $("#editar_abastecimento_").on('click', (event) => {

        const abastecimento_id = $(event.target).data('id');

        fetchAbastecimento(abastecimento_id,(abastecimento) => {

            const modal = $('#adicionar_abastecimento_modal');

            $('#abastecimento_id').val(abastecimento.id);
            $('#abastecimento_veiculo_id').val(abastecimento.veiculo.id);
            $('#litros').val(abastecimento.litros.toLocaleString('pt-BR'));
            $('#valor').val(abastecimento.valor.toLocaleString('pt-BR'));
            $('#tipoCombustivel').val(abastecimento.tipoCombustivelId);
            $('#tipoServico').val(abastecimento.tipoServicoId);
            $('#odometro').val(abastecimento.odometro.toLocaleString('pt-BR'));
            $('#observacao').val(abastecimento.observacao);

            modal.modal('show');
        });
    });
})