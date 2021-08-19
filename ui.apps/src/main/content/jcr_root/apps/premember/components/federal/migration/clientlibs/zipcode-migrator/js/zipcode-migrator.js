(function (window, document, $) {
    'use strict';
    $(document).ready(function () {
        $('#zipCodesMigrationPath').change(function () {
            let path = $(this).val();
            if (path != null) {
                $('#regionsPath').val(path);
            }
        });
    });
})(window, document, Granite.$);