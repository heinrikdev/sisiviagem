$(document).ready(() => {

    const detalhesPassageirosVisualizados = {};

    const exibirModalPassageiroDetalhes = (passageiro) => {

        $('#passageiroNome').empty().append(passageiro.nome);
        $('#identificacao').empty().append(passageiro.identificacao);

        const rg_orgao = (passageiro.rg + ' - ' +passageiro.orgao).replace('null', '');

        $('#rg').empty().append(rg_orgao);

        $('#tipoSanguineo').empty().append(passageiro.tipoSanguineo);
        $('#telefone').empty().append(passageiro.telefone);
        $('#motivo').empty().append(passageiro.motivo);

        $('#passageiroDetalhes').modal('show');
    };


    $('#tipoTrajeto').change(() => {

        $('.status_passageiro').val('0');

        if ($('#tipoTrajeto').val() != '-1') {

            $('.status_passageiro').removeClass('hide');
            $('table').DataTable().columns.adjust();
            $('#status_passageiro_th').empty().append('Status');
            $('#btn_salvar_status').removeClass('hide');

            if ($('#tipoTrajeto').val() == 'ida') {

                $('.status_passageiro[data-status-ida="Ausente"]').val('1');
            } 
            else {

                $('.status_passageiro[data-status-volta="Ausente"]').val('1');
            }

            $('.status_passageiro:first').focus();
        } 
        else {

            $('.status_passageiro').addClass('hide');
            $('#status_passageiro_th').empty();
            $('#btn_salvar_status').addClass('hide');
        }
    });

    $('#btn_salvar_status').on('click', () => {

        const tipoTrajeto = $('#tipoTrajeto').val();
        const passagirosStatusUrl = [];

        $('#loaderModal').modal('show');

        Array.from($('.status_passageiro')).forEach(el => {

            const select_ = $(el);
            
            passageiro_id = select_.attr('name');
            estado = select_.val();
        
            url = `/dashboard/api/passageiro/status/${tipoTrajeto}/${estado}/${passageiro_id}`;
            passagirosStatusUrl.push(url);
        });


        let progress = passagirosStatusUrl.length;

        const finished = () => {

            progress--;
            if (progress == 0) {
                $('#loaderModal').modal('hide');
            }
        };

        passagirosStatusUrl.forEach((url) =>  {
            console.log(url);
            $.get(url).always(() => {
                finished();
            });
        });
    });

    $('.btn_detalhes').on('click', (event) => {

        const passageiro_id = $(event.target).attr('data-passageiro');

        if (detalhesPassageirosVisualizados[passageiro_id] == undefined) {

            $('#loaderModal').modal('show');
            const url = '/dashboard/api/passageiro/' + passageiro_id;
        
            $.get(url, (passageiro) => {

                detalhesPassageirosVisualizados[passageiro.id] = passageiro;
                exibirModalPassageiroDetalhes(passageiro);
                
            })
            .fail(() => {

                alert('O servidor se comportou de forma inesperada.');
                $('#loaderModal').modal('hide');
            })
            .always(() => {

                $('#loaderModal').modal('hide');
            });

        } else {

            const passageiro = detalhesPassageirosVisualizados[passageiro_id];
            exibirModalPassageiroDetalhes(passageiro);
        }
    });
});