$(document).ready(function () {
    $('span[help]').each(function () {
        var help = $(this).attr('help');
        if (help != undefined) {
            var parts = help.split('|');
            var a = $('<a/>').attr('href', parts[1]).text(parts[0]);
            $(this).html(a);
        }
    });
});