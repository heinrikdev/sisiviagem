$(document).ready(() => {

    let passageiros = [];

    $('#btnNovoPassageiro').on('click', function() {
        
        $('#formPassageiro').removeClass('hide');
        $('#listaPassageiros').addClass('hide');
        $('#btnNovoPassageiro').addClass('hide');
    });

    $('#Identificacao').mask('000.000.000-00', {reverse: true});

    const fetchPassageiros = () => {

        if (document.getElementById('nomePassageiro').value.trim()  == "") return -1;

        $('#loaderModal').modal('show');
        const url = '/dashboard/api/passageiro/nome/' + document.getElementById('nomePassageiro').value;

        $.get(url, (data) => {
            
            $('#btnNovoPassageiro').addClass('hide');
            $('#formPassageiro').addClass('hide');

            let table = $('#tableListaPassageiros').DataTable();
            table.destroy();

            $('#tableListaPassageiros tbody').empty();

            if (data.length > 0) {

                $('#listaPassageiros').removeClass('hide');

                data.forEach((p, index)=> {
                    $('#tableListaPassageiros tbody').append
                    (`
                        <tr>
                            <td>${p.nome}</td>
                            <td class="text-right">${p.identificacao}</td>
                            <td class="text-right">${p.rg + ' - ' + p.orgao}</td>
                            <td class="text-center">
                                <button class='btn btn-info text-center btn-flat btn-xs selecionar_passageiro_btn' type='button' id=${index}>
                                    Selecionar
                                </button>
                            </td>
                        </tr>
                    `);
                });
                
                $('.selecionar_passageiro_btn').on('click', function(evt){

                    let index_passageiro = $(this)[0].id;
                    let passageiro = passageiros[index_passageiro];

                    showFormulario(passageiro);
                });

                passageiros = data;
                $('#btnNovoPassageiro').removeClass('hide');

            } else {

                $('#btnNovoPassageiro').removeClass('hide');
            }

        }).always(() => {

            $('#tableListaPassageiros').DataTable();
            $('#loaderModal').modal('hide');
            
        });
    };

    const showFormulario = (passageiro = undefined) => {

        $('#btnNovoPassageiro').addClass('hide');

        $('#Identificacao').val(passageiro.identificacao).trigger('input');
        
        $('input[name="passageiro.id"')[0].value                = passageiro.id;
        $('input[name="passageiro.nome"')[0].value              = passageiro.nome;
        $('input[name="passageiro.orgao"')[0].value             = passageiro.orgao;
        $('input[name="passageiro.rg"')[0].value                = passageiro.rg;
        $('input[name="passageiro.telefone"')[0].value          = passageiro.telefone;
        $('select[name="passageiro.tipoSanguineo"')[0].value    = passageiro.tipoSanguineo;

        $('#formPassageiro').removeClass('hide');
        $('#listaPassageiros').addClass('hide');
    };

    $('#nomePassageiro').on('keyup keypress', function(e) {
        var keyCode = e.keyCode || e.which;
        if (keyCode === 13) { 

            fetchPassageiros();

            e.preventDefault();
            return false;
        }
    });

    $('#buscarPassageiros').on('click', () => {
        fetchPassageiros();
    });
});