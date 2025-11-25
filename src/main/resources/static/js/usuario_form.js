
$(document).ready(() => {

    const isUpdateLoginUnico = $('#loginUnico').val();

    const inputLoginUnicoEditar = () => {

        let isEditarLoginUnico = !(($('#usuario_id').val() != '') && $('#loginUnico').val() != '' && $('#loginUnico').val() === isUpdateLoginUnico);
        return isEditarLoginUnico;
    };

    const isFormularioInformacoesValido = () => {
        return $('#formUsuario')[0].checkValidity();
    }

    $('#loginUnico').focusout(() => {
        if (inputLoginUnicoEditar()) {
            
            const url = '/dashboard/api/usuario/login_unico/em_uso/' + $('#loginUnico').val();

            $.get(url, (isLoginUnicoEmUso) => {
                if (isLoginUnicoEmUso) {
                    $('#d-infoModalTexto').empty().append("Este login único não está disponível, insira outro.");
                    $('#d-infoModal').modal('show');
                }
            });
        }
    });

    $('#formUsuarioBtnSubmit').on('click', () => {

        if (isFormularioInformacoesValido()) { 
            if (inputLoginUnicoEditar()) {
            
                const url = '/dashboard/api/usuario/login_unico/em_uso/' + $('#loginUnico').val();
    
                $.get(url, (isLoginUnicoEmUso) => {
    
                    if (isLoginUnicoEmUso) {
    
                        $('#d-infoModalTexto').empty().append("Este login único não está disponível, insira outro.");
                        $('#d-infoModal').modal('show');
    
                    } else {
    
                        $('#formUsuario').submit();
                    }
                });
    
            } else {
                
                $('#formUsuario').submit();
            }
        }
        else {

            $('#d-infoModalTexto').empty().append("Há campos não preenchidos, preenha-os antes por favor.");
            $('#d-infoModal').modal('show');
        }
    });

});