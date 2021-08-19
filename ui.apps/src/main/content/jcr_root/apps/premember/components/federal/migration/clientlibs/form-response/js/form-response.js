(function (window, document, $) {
    'use strict';
    let ui = $(window).adaptTo('foundation-ui');
    $(document).ready(function () {
        $("form.foundation-form").on('foundation-form-submitted', handleResponse);
    });

    function handleResponse(event, success, xhr) {
        let responseText = replaceAll(xhr.responseText,"%n","<br>");
        if (success === true) {
            ui.alert('Successful operation', responseText, 'success');
        }else{
            ui.alert('Failed operation', responseText, 'error');
        }
    }

    function replaceAll(string, search, replace) {
      return string.split(search).join(replace);
    }

})(window, document, Granite.$);