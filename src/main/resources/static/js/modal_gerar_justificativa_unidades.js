$(document).ready(() => {
    $("#btn_modal_justificativa_").on('click', event => {

        $('#modal_gerar_justificativa_unidades').modal('hide');
        $('#modal_gerar_justificativa_unidades_form').submit();
        $('#modal_gerar_justificativa_unidades_form').trigger("reset");
    });
});