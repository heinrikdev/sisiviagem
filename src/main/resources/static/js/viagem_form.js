$(document).ready(() => {

    const isFormularioInformacoesValido = () => {
        return $('#formNovaViagem')[0].checkValidity();
    }

    const tables = $('table');

    const configuracaoForm = () => {

        for (var i = 0; i < tables.length; i++) {
            $(tables[i]).DataTable({
                scrollY: '200px',
                scrollCollapse: true,
                paging: true,
                searching: true
            });
        }
    
        let pathUrl = window.location.href.trim();
    
        if (pathUrl.includes('motorista')) {
    
            $('.nav-tabs a[href="#motoristas"]').tab('show');
    
        } else if (pathUrl.includes('veiculo')) {
    
            $('.nav-tabs a[href="#veiculos"]').tab('show');
        }

        $('#d-infoModal').on('hidden.bs.modal', () => {
            $('.nav-tabs a[href="#viagem"]').tab('show');
        })

        $('ul.nav.nav-tabs li:not(:first)').on('click', () => {
            if (!isFormularioInformacoesValido()) {

                $('#d-infoModalTexto').empty().append("Há campos não preenchidos na aba daddos da viagem, preenha-os antes por favor.");
                $('#d-infoModal').modal('show');
    
            }
        });
    };

    configuracaoForm();
});