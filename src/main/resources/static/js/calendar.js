$(document).ready(() => {

    let viagem_id;

    $('#eventDetalhes').on('click', () => {
        document.location.href = `/dashboard/transporte/viagem/${viagem_id}/detalhes`;
    });

    $("#calendar").fullCalendar({
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,basicWeek,listWeek'
		},
        lang: "pt-BR",
        allDayDefault: false,
        eventSources: [
            {
                url: "/dashboard/api/viagens", // use the `url` property
                color: "#337ab7", // an option!
                textColor: "white", // an option!
                cache: true
            }
        ],
        loading: function (isLoading) {
           
            if (isLoading) {

                $("#loaderModal").modal('show');

            } else {

                $("#loaderModal").modal('hide');
            }

        },
        eventClick: function(calEvent, jsEvent, view) {

            viagem_id = calEvent.id;

            console.log(calEvent);

            $("#titulo").text(calEvent.title);

            let motoristaNome = calEvent.motoristas.length > 0 ? calEvent.motoristas[0].pessoa.nome : 'Não informado';
            let infoVeiculo = calEvent.veiculos.length > 0 ? calEvent.veiculos[0].carro + ' - Placa: ' + calEvent.veiculos[0].placa : 'Não informado';

            let partida  = calEvent.data;
            let chegada  = calEvent.dataTermino;

            $("#partida").text(partida);
            $("#chegada").text(chegada);

            $("#motorista").text(motoristaNome);
            $("#veiculo").text(infoVeiculo);
            $("#descricao").text(calEvent.descricao);

            $("#eventDetalhesModal").modal('show');
        }
    });
});
