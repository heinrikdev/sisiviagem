$(document).ready(() => {
    $('#tipoTransporte').change((event) => {

        const selected = $(event.target).val();

        if (selected === 'Veículo próprio') {
            $('#modal_dados_veiculo_proprio').modal('show');
        }
    });

    $('#btn_modal_veiculo_proprio_').on('click', () => {

        const form = $('#modal_dados_veiculo_proprio_form');

        if (form[0].reportValidity()) {
            
            var veiculoForm = form.serializeArray();
            var veiculoProprio = {};

            $.each(veiculoForm, (i, input) => {
                veiculoProprio[input.name] = input.value;
            });

            console.log(veiculoProprio);

            $.ajax({
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                url: '/dashboard/proposto/requisicoes/veiculo_proprio',
                data: JSON.stringify(veiculoProprio),
                success: function(result) {

                    $('#modal_dados_veiculo_proprio').modal('hide');
                    alert('Informações salvar com sucesso!');
                },
                error: function(e) {
                    
                    alert('Não foi possível realizar esta ação :(');
                },
                done: function(e) {

                    $('#modal_dados_veiculo_proprio').modal('hide');
                    alert('Informações salvar com sucesso!');
                }
            });
        }
    });
});