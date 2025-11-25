$(document).ready(() => {

    const PONTO_PARTIDA_BRASIL = 1;

    const cidades = {};
    
    let estadoOrigemSelect = undefined;
    let estadoDestinoSelect = undefined;

    let cidadeOrigemSelect = document.createElement("select");

    cidadeOrigemSelect.setAttribute('id', 'cidadeOrigem');
    cidadeOrigemSelect.setAttribute('class', 'form-control');
    cidadeOrigemSelect.setAttribute('type', 'text');
    cidadeOrigemSelect.setAttribute('name', 'cidadeOrigem');

    let cidadeDestinoSelect = document.createElement("select");

    cidadeDestinoSelect.setAttribute('id', 'cidadeDestino');
    cidadeDestinoSelect.setAttribute('class', 'form-control');
    cidadeDestinoSelect.setAttribute('type', 'text');
    cidadeDestinoSelect.setAttribute('name', 'cidadeDestino');

    let option = document.createElement("option");
            
    option.value = '';
    option.text  = 'Selecione';

    cidadeDestinoSelect.add(option.cloneNode(true));
    cidadeOrigemSelect.add(option);

    const fillCidadesOptions = (uf, idSelect) => {

        $(idSelect).find('option').remove();

        let option = '<option value selected>Selecione</option>';
        $(idSelect).append(option);

        $.each(cidades[uf], (i, item) => {

            let option = '<option value="'+ item.nome +'">' + item.nome + '</option>';
            $(idSelect).append(option);
        });
    };

    const setEventEstados = () => {

        $('#estadoDestino').change(() => {
    
            const uf = $('#estadoDestino').val();
    
            if (uf.trim().length > 0) {
    
                if (cidades[uf] === undefined) {
    
                    $('#loaderModal').modal('show');
                    const url = '/dashboard/api/cidadeEstado/' + uf;
    
                    $.get(url, (data) => {
    
                        cidades[uf] = data;
    
                        fillCidadesOptions(uf, '#cidadeDestino');
    
                    }).always(() => {
    
                        $('#loaderModal').modal('hide');
                    });
    
                } else {
    
                    fillCidadesOptions(uf, '#cidadeDestino');
                }
            }
        });

        $('#estadoOrigem').change(() => {
    
            const uf = $('#estadoOrigem').val();
    
            if (uf.trim().length > 0) {
    
                if (cidades[uf] === undefined) {
    
                    $('#loaderModal').modal('show');
                    const url = '/dashboard/api/cidadeEstado/' + uf;
    
                    $.get(url, (data) => {
    
                        cidades[uf] = data;
                        fillCidadesOptions(uf, '#cidadeOrigem');
    
                    }).always(() => {
    
                        $('#loaderModal').modal('hide');
                    });
    
                } else {
    
                    fillCidadesOptions(uf, '#cidadeOrigem');
                }
            }
        });
    };

    const fetchEstados = (callback = undefined) => {

        $('#loaderModal').modal('show');
        const url = '/dashboard/api/cidadeEstado/estados';

        $.get(url, (data) => {

            estadoOrigemSelect = document.createElement("select");

            estadoOrigemSelect.setAttribute('id', 'estadoOrigem');
            estadoOrigemSelect.setAttribute('class', 'form-control');
            estadoOrigemSelect.setAttribute('type', 'text');
            estadoOrigemSelect.setAttribute('name', 'estadoOrigem');

            estadoDestinoSelect = document.createElement("select");

            estadoDestinoSelect.setAttribute('id', 'estadoDestino');
            estadoDestinoSelect.setAttribute('class', 'form-control');
            estadoDestinoSelect.setAttribute('type', 'text');
            estadoDestinoSelect.setAttribute('name', 'estadoDestino');

            let option = document.createElement("option");
            
            option.value = '';
            option.text  = 'Selecione';

            estadoOrigemSelect.add(option.cloneNode(true));
            estadoDestinoSelect.add(option);

            for (i = 0; i < data.length; i++) {

                let option = document.createElement("option");
                
                option.value = data[i].uf;
                option.text = data[i].nome + ' - ' + data[i].uf;

                estadoOrigemSelect.add(option.cloneNode(true));
                estadoDestinoSelect.add(option);
            }

            if (callback != undefined) callback();

        }).always(() => {

            $('#loaderModal').modal('hide');
        });
    };

    $('input[type=radio][name=isOrigemDoBrasil]').change(function (evt) {

        const element = evt.target;

        const parentEstadoOrigem = document.getElementById('partidaField');
        const parentCidadeOrigem = document.getElementById('partidaCidadeField');
        const labelEstadoPartida = document.getElementById('labelEstadoPartida');

        if (element.value == PONTO_PARTIDA_BRASIL) {

            labelEstadoPartida.innerText = "Estado: ";

            parentEstadoOrigem.removeChild(document.getElementById('estadoOrigem'));
            parentCidadeOrigem.removeChild(document.getElementById('cidadeOrigem'));

            parentEstadoOrigem.appendChild(estadoOrigemSelect);
            parentCidadeOrigem.appendChild(cidadeOrigemSelect);

            setEventEstados();

        } else {

            const inputPais = document.createElement("input");
            const inputCidade = document.createElement("input");
            
            inputPais.setAttribute('id', 'estadoOrigem');
            inputPais.setAttribute('class', 'form-control');
            inputPais.setAttribute('type', 'text');
            inputPais.setAttribute('name', 'estadoOrigem');

            inputCidade.setAttribute('id', 'cidadeOrigem');
            inputCidade.setAttribute('class', 'form-control');
            inputCidade.setAttribute('type', 'text');
            inputCidade.setAttribute('name', 'cidadeOrigem');
            
            labelEstadoPartida.innerText = "País: "; 

            const alterarFormulario = () => {

                parentEstadoOrigem.removeChild(document.getElementById('estadoOrigem'));
                parentCidadeOrigem.removeChild(document.getElementById('cidadeOrigem'));

                parentEstadoOrigem.appendChild(inputPais);
                parentCidadeOrigem.appendChild(inputCidade);
            };

            if (estadoDestinoSelect == undefined || estadoOrigemSelect == undefined) {
                
                fetchEstados(alterarFormulario);

            } else {

                alterarFormulario();
            }
        }
    });

    $('input[type=radio][name=isDestinoDoBrasil]').change(function (evt) {

        const element = evt.target;

        const parentEstadoOrigem = document.getElementById('destinoField');
        const parentCidadeOrigem = document.getElementById('destinoCidadeField');
        const labelEstadoPartida = document.getElementById('labelEstadoDestino');
        
        if (element.value == PONTO_PARTIDA_BRASIL) {

            labelEstadoPartida.innerText = "Estado: ";

            parentEstadoOrigem.removeChild(document.getElementById('estadoDestino'));
            parentCidadeOrigem.removeChild(document.getElementById('cidadeDestino'));

            parentEstadoOrigem.appendChild(estadoDestinoSelect);
            parentCidadeOrigem.appendChild(cidadeDestinoSelect);

            setEventEstados();

        } else {

            const inputPais = document.createElement("input");
            const inputCidade = document.createElement("input");
            
            inputPais.setAttribute('id', 'estadoDestino');
            inputPais.setAttribute('class', 'form-control');
            inputPais.setAttribute('type', 'text');
            inputPais.setAttribute('name', 'estadoDestino');

            inputCidade.setAttribute('id', 'cidadeDestino');
            inputCidade.setAttribute('class', 'form-control');
            inputCidade.setAttribute('type', 'text');
            inputCidade.setAttribute('name', 'cidadeDestino');
            
            labelEstadoPartida.innerText = "País: ";

            const alterarFormulario = () => {

                parentEstadoOrigem.removeChild(document.getElementById('estadoDestino'));
                parentEstadoOrigem.appendChild(inputPais);

                parentCidadeOrigem.removeChild(document.getElementById('cidadeDestino'));
                parentCidadeOrigem.appendChild(inputCidade);
            };

            if (estadoDestinoSelect == undefined || estadoOrigemSelect == undefined) {
                
                fetchEstados(alterarFormulario);

            } else {

                alterarFormulario();
            }
        }
    });

    fetchEstados();
    setEventEstados();
});