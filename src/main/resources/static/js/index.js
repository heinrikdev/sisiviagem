
$(document).ready(() => {

    const bytesToSize = function (bytes) {

        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];

        if (bytes === 0) return 'n/a';

        const i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)), 10);
        if (i === 0) return `${bytes} ${sizes[i]})`;
        return `${(bytes / (1024 ** i)).toFixed(7)} ${sizes[i]}`;
    }

    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.extend(true, $.fn.dataTable.defaults, {
        fixedHeader: true,
        scrollX: true,
        fixedColumns: true,
        scrollCollapse: true,
        responsive: false,
        paging: false,
        searching: false,
        info: false,
        ordering: false,
        language: {
            sEmptyTable: "Nenhum registro encontrado",
            sInfo: "Mostrando de _START_ até _END_ de _TOTAL_ registros",
            sInfoEmpty: "Mostrando 0 até 0 de 0 registros",
            sInfoFiltered: "(Filtrados de _MAX_ registros)",
            sInfoPostFix: "",
            sInfoThousands: ".",
            sLengthMenu: "_MENU_ resultados por tabela",
            sLoadingRecords: "Carregando...",
            sProcessing: "Processando...",
            sZeroRecords: "Nenhum registro encontrado",
            sSearch: "Pesquisar na tabela: ",
            oPaginate: {
                sNext: "Próximo",
                sPrevious: "Anterior",
                sFirst: "Primeiro",
                sLast: "Último"
            },
            oAria: {
                sSortAscending: ": Ordenar colunas de forma ascendente",
                sSortDescending: ": Ordenar colunas de forma descendente"
            }
        }
    });
    
    
    /**
     * Ajustar colunas datatables quando uma tab é clicked
     */
    $('a[data-toggle="tab"]').on("shown.bs.tab", function (e) {
        $($.fn.dataTable.tables(true)).DataTable().columns.adjust();
    });
    
    /**
     * Ajustar colunas datatables quando o sidebar é collapsed
     */
    $('body').on('expanded.pushMenu collapsed.pushMenu', () => {
    
        setTimeout(() => {
            $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();
        }, 245);
    });
    
    /**
     * SCROLL TO TOP
     */
    
    $(function () {
    
        var slideToTop = $("<div />");
    
        slideToTop.html('<i class="fa fa-chevron-up"></i>');
    
        slideToTop.css({
            position: 'fixed',
            bottom: '20px',
            right: '25px',
            width: '40px',
            height: '40px',
            color: '#eee',
            'font-size': '',
            'line-height': '40px',
            'text-align': 'center',
            'background-color': '#222d32',
            cursor: 'pointer',
            'border-radius': '5px',
            'z-index': '99999',
            opacity: '.7',
            'display': 'none'
        });
    
        slideToTop.on('mouseenter', function () {
            $(this).css('opacity', '1');
        });
    
        slideToTop.on('mouseout', function () {
            $(this).css('opacity', '.7');
        });
    
        $('.wrapper').append(slideToTop);
    
        $(window).scroll(function () {
            if ($(window).scrollTop() >= 150) {
                if (!$(slideToTop).is(':visible')) {
                    $(slideToTop).fadeIn(500);
                }
            } else {
                $(slideToTop).fadeOut(500);
            }
        });
    
        $(slideToTop).click(function () {
            $(window).scrollTop(0);
        });
    });
    
    $('#btn_logout_sis').on('click', () => {

        $('#logoutModal').modal('hide');
        $("#loaderModal").modal("show");
        window.location = '/logout';
    });

    $("#loaderModal").modal("hide");

    $('input[type=file]').on('change', (evt) => {

        const maxSize = 5242880;
        const input = $(evt.target);
        const sizeFile = input[0].files[0].size;
        
        if (sizeFile > maxSize) {

            input.val(null);

            const modal = $('#d-infoModal')
            const texto = $('#d-infoModalTexto');

            const html = 
            `
                O arquivo deve ter no máximo 5MB (Dois Megabytes), selecione outro.<br>
                Tamanho do arquivo selecionado: <strong class="text-red">${bytesToSize(sizeFile)}</strong>
            `
            texto.html(html);
            modal.modal('show');
        }
    });
});

/**
 * LOADER MODAL
 */
$(window).on("load", () => {

    window.onhashchange = () => {

        const loaderModal = $("#loaderModal");
        loaderModal.modal("hide");

        console.log('has change');
    };
    

	const loaderModal = $("#loaderModal");
    loaderModal.modal("hide");

	$('a[href^="/"]').not('a[target="_blank"]').click((event) => {
        
        let link_href = $(event.target).closest('a').attr('href');
        $("#loaderModal").modal("show");
	});

	$('button[type="submit"]').click((event) => {

		const form = $(event.target.form)[0];
		const isValid = form !== undefined ? form.checkValidity() : false;

		if (isValid) {
			$("#loaderModal").modal("show");
		}
    });
    
    $('.modal').on('hidden.bs.modal', () => {
        $("body").css("padding-right","0");
    });
});