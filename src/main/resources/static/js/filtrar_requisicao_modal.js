$(document).ready(() => {
    $("#tipoFiltro").change((event) => {

        let filtroSelecionado = $(event.target).val();

        $('.filtro_form').addClass('hide');
        $(filtroSelecionado).removeClass('hide');
    });
});
