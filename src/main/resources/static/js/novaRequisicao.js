$(document).ready(() => {

    var tiposRequisicao = ['unidadesAdministrativa', 'unidadesGraduacao', 'unidadesPos'];

    var ehParaMudarParaAbaInformacoes = false;

    var tiposTransportes = {
        select_id: '#tipoTransporte',
        possuiDiaria: {
            isChecked: false,
            options: [
                '<option value="Aéreo">Aéreo</option>',
                // '<option value="Aéreo Internacional">Aéreo Internacional</option>',
                '<option value="Veículo Rodoviário">Veículo Rodoviário</option>',
                '<option value="Veículo próprio">Veículo próprio</option>'
            ]
        },
        possuiPassagem: {
            $: false,
            options: []
        },
        possuiTransporte: {
            $: false,
            options: [
                '<option value="Veículo oficial">Veículo oficial</option>'
            ]
        }
    };

    const unidades = {
        unidadesAdministrativa: [],
        unidadesGraduacao: [],
        unidadesPos: []
    };

    var callbackAfterModalClose = undefined;

    const cidades = {};

    const filtrarTipoTrechos = () => {

        const solicitacoes =  $('.solicitacoes_options');

        Array.from(solicitacoes).forEach(e => {

            let selectTipoTransporte = $('#tipoTransporte');
            let checkbox = $(e);
            let id = checkbox.attr('id');
            let options = [];

            tiposTransportes[id].isChecked = checkbox.is(":checked");
            selectTipoTransporte.children('option:not(:first)').remove();

            if (tiposTransportes.possuiDiaria.isChecked) {

                options = tiposTransportes.possuiDiaria.options;
                fillTipoTransporteOptions(options);
            }

            if (tiposTransportes.possuiPassagem.isChecked) {

                options = tiposTransportes.possuiPassagem.options;
                fillTipoTransporteOptions(options);
            }

            if (tiposTransportes.possuiTransporte.isChecked) {

                options = tiposTransportes.possuiTransporte.options;
                fillTipoTransporteOptions(options);
            }
        });

    };

    const validarCPF = (strCPF) => {

        if (strCPF == null || strCPF == "" || strCPF == undefined) return false;
        
        var Soma;
        var Resto;
        Soma = 0;

        if (strCPF == "00000000000") return false;
         
        for (i=1; i<=9; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (11 - i);
             Resto = (Soma * 10) % 11;
       
        if ((Resto == 10) || (Resto == 11))  Resto = 0;
        if (Resto != parseInt(strCPF.substring(9, 10)) ) return false;
       
        Soma = 0;
        
        for (i = 1; i <= 10; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (12 - i);

        Resto = (Soma * 10) % 11;
       
        if ((Resto == 10) || (Resto == 11))  Resto = 0;
        if (Resto != parseInt(strCPF.substring(10, 11) ) ) return false;

        return true;
    }

    const isFormularioInformacoesValido = () => {
        return $('#formNovaRequisicao')[0].checkValidity();
    }

    const criarDatatables = () => {

        const tables = $('table');

        for (var i = 0; i < tables.length; i++) {
            //if(tables[i].id != 'tableListaPassageiros')
                $(tables[i]).DataTable();
        }
    };

    const exibirModalPrimeiroTrechoInvalido = () => {

        const dataSaida         = stringToDate($('#dataSaida').val());
        const dataRequisicao    = stringToDate($('#dataRequisicao').val());

        const msg =
        `
            Para inserir o primeiro trecho a data de origem deve ser igual a data da viagem.<br><br>
            Dados inseridos:<br><br>

            <b>Data de origem</b>: ${dataSaida.toLocaleDateString()}<br>
            <b>Data da viagem</b>: ${dataRequisicao.toLocaleDateString()}
        `;

        $('#d-infoModalTexto').empty().append(msg);
        $('#d-infoModal').modal('show');

        callbackAfterModalClose = () => {
            $('#dataSaida').focus();
            attributeRequiredFieldAdiconarTrecho(false);
        };
    };

    const attributeRequiredFieldAdiconarTrecho = (isRequired) => {

        $('#adicionar_trecho input:text').prop('required', isRequired);
        $('#adicionar_trecho input[type="date"]').prop('required', isRequired);
        $('#adicionar_trecho select').prop('required', isRequired);
    };

    const attributeRequiredFieldAdiconarAnexo = (isRequired) => {

        $('#adicionar_anexo input:text').prop('required', isRequired);
        $('#adicionar_anexo select').prop('required', isRequired);
        $('#adicionar_anexo input:file').prop('required', isRequired);
    };

    const attributeRequiredFieldAdiconarPassageiros = (isRequired) => {

        $('#adicionar_passageiro input:text[name!="searchPassageiro"]').prop('required', isRequired);
        $('#adicionar_passageiro select').prop('required', isRequired);
    };

    const removerAttributeRequiredAnexoTrechoPassageiro = () => {

        attributeRequiredFieldAdiconarAnexo(false);
        attributeRequiredFieldAdiconarPassageiros(false);
        attributeRequiredFieldAdiconarTrecho(false);
    };

    const isPrimeiroTrechoInvalido = () => {

        const dataSaida         = stringToDate($('#dataSaida').val());
        const dataRequisicao    = stringToDate($('#dataRequisicao').val());
        const possuiTrecho      = $('#adicionar_trecho tbody tr:first td').length > 1;
        const primeiroTrecho    = !possuiTrecho;
        const datasValidas      = dataSaida.getTime() !== dataRequisicao.getTime();

        return primeiroTrecho && datasValidas;
    };

    const activeTabAtual = () => {

        let pathUrl = window.location.href.trim();

        if (pathUrl.length > 0 && isFormularioInformacoesValido()) {

            if (pathUrl.includes('passageiro')) {

                $('.nav-tabs a[href="#adicionar_passageiro"]').tab('show');

            } else if (pathUrl.includes('trecho')) {

                $('.nav-tabs a[href="#adicionar_trecho"]').tab('show');

            } else if (pathUrl.includes('anexo')) {

                $('.nav-tabs a[href="#adicionar_anexo"]').tab('show');
            }
        }
    };

    const fillUnidadesOptions = (tipo) => {

        $('#unidade').find('option').remove();

        let option = '<option value selected>Selecione</option>';
        $('#unidade').append(option);

        $.each(unidades[tipo], (i, item) => {

            let option = '<option value="'+ item.id +'">' + item.nome + '</option>';
            $('#unidade').append(option);
        });
    };

    const fillTipoTransporteOptions = (options) => {
        $.each(options, (i, option) => {
            $(tiposTransportes.select_id).append(option);
        });
    };

    const stringToDate = (str) => {

        const date = str.split('-');
        return new Date(date[0], date[1]-1, date[2]);
    };

    const exibirModalCamposNaoPreenchidos = () => {

        $('#d-infoModalTexto').empty().append("Há campos obrigatórios não preenchidos, preencha-os.");
        $('#d-infoModal').modal('show');
    };

    const configForms = () => {

        $('#formNovaRequisicao').on('keyup keypress', function(e) {
            var keyCode = e.keyCode || e.which;
            
            if (keyCode === 13) { 
            
                e.preventDefault();
                return false;
            }
        });

        $('#d-infoModal').on('hidden.bs.modal', () => {

            if (ehParaMudarParaAbaInformacoes) {

                $('.nav-tabs a[href="#nova_requisicao"]').tab('show');
                ehParaMudarParaAbaInformacoes = false;
            }

            if (callbackAfterModalClose != undefined) {

                callbackAfterModalClose();
                callbackAfterModalClose = undefined;
            }
        })

        $('ul.nav.nav-tabs li:not(:first)').on('click', () => {

            removerAttributeRequiredAnexoTrechoPassageiro();

            if (!isFormularioInformacoesValido()) {

                ehParaMudarParaAbaInformacoes = true;

                $('#d-infoModalTexto').empty().append("Há campos não preenchidos na aba informações, preenha-os antes por favor.");
                $('#d-infoModal').modal('show');
            }
        });

        $('#justificativa').keyup(() => {

            const qtdCaracteres = $('#justificativa').val().length;
            $('#caracteresRestantes').text('Restam: ' + (1000 - qtdCaracteres) + ' caractere(s).');
        })

        $('#titulo').change(() => {

            const option = $('#titulo').val();

            if (option === 'Outro') {

                $('#outroMotivoRequisicao').removeClass('hide');

            } else {

                $('#outroMotivoRequisicao').addClass('hide');
            }

        });

        $('#tipoRequisicao').change(() => {

            const selected = $('#tipoRequisicao').val();

            if (selected != 'NULL') {

                const tipo = tiposRequisicao[selected];

                if (unidades[tipo].length === 0) {

                    $('#loaderModal').modal('show');
                    const url = '/dashboard/api/unidades/' + tipo;

                    $.get(url, (data) => {

                        unidades[tipo] = data;

                        fillUnidadesOptions(tipo);

                    }).always(() => {

                        $('#loaderModal').modal('hide');
                    });

                } else {

                    fillUnidadesOptions(tipo);
                }
            }
        });

        $('#rotulo_anexo').change((event) => {

            let selected = $('#rotulo_anexo').val();

            if (selected === 'Outro') {

                $('#rotulo_outro').removeClass('hide');
                $('#input_rotulo_outro').prop('required', true);
                $('#input_rotulo_outro')[0].type = "text";
            }
            else {

                if (!$('#rotulo_outro').hasClass('hide')) {

                    $('#rotulo_outro').addClass('hide');
                    $('#input_rotulo_outro').prop('required', false);
                    $('#input_rotulo_outro')[0].type = "hidden";
                }
            }
        });

        $('.solicitacoes_options').on('click', (event) => {

            let selectTipoTransporte = $('#tipoTransporte');
            let checkbox = $(event.target);
            let id = checkbox.attr('id');
            let options = [];

            tiposTransportes[id].isChecked = checkbox.is(":checked");
            selectTipoTransporte.children('option:not(:first)').remove();

            if (tiposTransportes.possuiDiaria.isChecked) {

                options = tiposTransportes.possuiDiaria.options;
                fillTipoTransporteOptions(options);
            }

            if (tiposTransportes.possuiPassagem.isChecked) {

                options = tiposTransportes.possuiPassagem.options;
                fillTipoTransporteOptions(options);
            }

            if (tiposTransportes.possuiTransporte.isChecked) {

                options = tiposTransportes.possuiTransporte.options;
                fillTipoTransporteOptions(options);
            }
        });

        $('#tipoTrecho').change(() => {

            const selected = $('#tipoTrecho').val();

            if (selected == 'Ida e Volta') {
                const msg = 'A opção Ida e Volta só funciona para viagens com duração de 1 (um) dia. Caso contrário adicione os trechos de ida ou volta separadamente.';

                $('#d-infoModalTexto').empty().append(msg);
                $('#d-infoModal').modal('show');
            }
        });

        $('#btn_adicionar_passageiro').on('click', (event) => {

            event.preventDefault();
            attributeRequiredFieldAdiconarPassageiros(true);

            let form      = $('#formNovaRequisicao');
            let cpf       = $('input[name="passageiro.Identificacao"]').val().match(/\d/g).join("");

            let cpf_is_valid = validarCPF(cpf);

            if (form[0].reportValidity() && cpf_is_valid) {

                $('#loaderModal').modal('show');
                const url   = "/dashboard/proposto/formulario/adicionar_passageiro";

                form.attr('action', url);
                form.submit();

            }  else {

                if (!cpf_is_valid) {

                    $('#cpf_form_group').addClass('has-error has-feedback');
                    $('input[name="passageiro.Identificacao"]').focus();
                    $('#cpf_invalido_message').removeClass('hide');
                }

                exibirModalCamposNaoPreenchidos();
                callbackAfterModalClose = () => {

                    form[0].reportValidity();

                    setTimeout(() => {

                        attributeRequiredFieldAdiconarPassageiros(false);

                    }, 500);
                };
            }
        });

        $('#btn_adicionar_anexo').on('click', (event) => {

            event.preventDefault();
            attributeRequiredFieldAdiconarAnexo(true);

            var form = $('#formNovaRequisicao');

            if (form[0].reportValidity()) {

                $('#loaderModal').modal('show');
                const url   = "/dashboard/proposto/formulario/adicionar_anexo";

                form.attr('action', url);
                form.submit();

            } else {

                exibirModalCamposNaoPreenchidos();
                callbackAfterModalClose = () => {

                    form[0].reportValidity();

                    setTimeout(() => {

                        attributeRequiredFieldAdiconarAnexo(false);

                    }, 500);
                };
            }
        });

        $('#btn_adicionar_trecho').on('click', (event) => {

            event.preventDefault();
            attributeRequiredFieldAdiconarTrecho(true);

            var form = $('#formNovaRequisicao');

            if (form[0].reportValidity()) {

                if (isPrimeiroTrechoInvalido()) {

                    exibirModalPrimeiroTrechoInvalido();
                }
                else {

                    const dataSaida     = stringToDate($('#dataSaida').val());
                    const dataChegada   = stringToDate($('#dataChegada').val());

                    if (dataChegada < dataSaida) {

                        $('#d-infoModalTexto').empty().append("A data de destino não pode ser anterior a data de origem.");
                        $('#d-infoModal').modal('show');

                        callbackAfterModalClose = () => {};
                    }
                    else {

                        $('#loaderModal').modal('show');
                        const url   = "/dashboard/proposto/formulario/adicionar_trecho";

                        form.attr('action', url);
                        form.submit();
                    }

                }

            } else {

                exibirModalCamposNaoPreenchidos();
                callbackAfterModalClose = () => {

                    form[0].reportValidity();

                    setTimeout(() => {

                        attributeRequiredFieldAdiconarTrecho(false);

                    }, 500);
                };
            }
        });
    };

    criarDatatables();
    activeTabAtual();
    filtrarTipoTrechos();
    configForms();
});