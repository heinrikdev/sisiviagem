$(document).ready(() => {

    var veiculo_id = undefined;

    $('#btn_abastecimento').on('click', (event) => {
        veiculo_id = $(event.target)[0].getAttribute('data-veiculo');
        $('#abastecimento_veiculo_id').val(veiculo_id);
    });
});