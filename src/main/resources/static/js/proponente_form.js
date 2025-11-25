$(document).ready(() => {

    const isUpdateLoginUnico = $('#loginUnico').val();

    const inputLoginUnicoEditar = () => {
        let isEditarLoginUnico = !(($('#proponente_id').val() != '') &&
            $('#loginUnico').val() != '' &&
            $('#loginUnico').val() === isUpdateLoginUnico);
        return isEditarLoginUnico;
    };

    const isFormularioInformacoesValido = () => {
        return $('#formProponente')[0].checkValidity();
    };

    // ---- Novo listener para MULTI‐SELECT de perfis ----
    $('input[name="permissao"]').on('change', function() {
        // verifica se algum AUDITOR está marcado
        const auditorChecked =
            $('input[name="permissao"][value="AUDITOR"]:checked').length > 0;

        // mostra/esconde o bloco e ajusta required
        $('#auditorPermissao').toggleClass('hide', !auditorChecked);
        $('#unidadeDepartamento').prop('required', auditorChecked);
    });
    // dispara uma vez no load para inicializar corretamente
    $('input[name="permissao"]').first().trigger('change');
    // --------------------------------------------------

    $('#loginUnico').focusout(() => {
        if (inputLoginUnicoEditar()) {
            const url =
                '/dashboard/api/usuario/login_unico/em_uso/' +
                $('#loginUnico').val();

            $.get(url, (isLoginUnicoEmUso) => {
                if (isLoginUnicoEmUso) {
                    $('#d-infoModalTexto')
                        .empty()
                        .append(
                            "Este login único não está disponível, insira outro."
                        );
                    $('#d-infoModal').modal('show');
                }
            });
        }
    });

    $('#formProponenteBtnSubmit').on('click', () => {
        if (isFormularioInformacoesValido()) {
            if (inputLoginUnicoEditar()) {
                const url =
                    '/dashboard/api/usuario/login_unico/em_uso/' +
                    $('#loginUnico').val();

                $.get(url, (isLoginUnicoEmUso) => {
                    if (isLoginUnicoEmUso) {
                        $('#d-infoModalTexto')
                            .empty()
                            .append(
                                "Este login único não está disponível, insira outro."
                            );
                        $('#d-infoModal').modal('show');
                    } else {
                        $('#formProponente').submit();
                    }
                });
            } else {
                $('#formProponente').submit();
            }
        } else {
            $('#d-infoModalTexto')
                .empty()
                .append(
                    "Há campos não preenchidos, preencha-os antes por favor."
                );
            $('#d-infoModal').modal('show');
        }
    });
});