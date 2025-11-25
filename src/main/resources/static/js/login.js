/**
 * LOADER MODAL
 */
$(window).on('load', () => {
    const loaderModal = $('#loaderModal');
    loaderModal.modal('hide');

    $('button:submit').click(() => {
        $('#loaderModal').modal('show');
    });
});

$(document).ready(() => {

    document.getElementById("btnLogar").disabled = false;

    var url_string = window.location.href
    var url = new URL(url_string);
    var redirecionar = url.searchParams.get("redirecionar");

    if (redirecionar != null) {
        var form = document.getElementById('loginForm');
        form.action += '?redirecionar=' + redirecionar + window.location.hash;
    }
});

function recaptchaDataCallback(){
    document.getElementById("btnLogar").disabled = false;
}