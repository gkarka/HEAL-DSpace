$.fn.helptip = function () {
    $(this).each(function () {
        if ($(this).hasClass('data-tip')) {
            if ($(this).attr('rel') != undefined) {
                $(this).cluetip({ width: '600px', closeText: '[X]', closePosition: 'title', sticky: true, mouseOutClose: 'cluetip' });
            }
            else {
                $(this).cluetip({ splitTitle: '|', showTitle: false, closeText: '[X]', closePosition: 'title', sticky: true, mouseOutClose: 'cluetip' });
            }
        }
        else {
            var id = $(this).attr('id');
            if (id != undefined) {
                var label = $(this).closest('li').find('label[for="' + id + '"]');
                var text = label.text();
                if (text.length > 0) {                    
                    var btn = $('<img src="themes/Heal/images/information.png" class="help-tip-button"/>');
                    btn.addClass('data-tip');
                    if ($(this).attr('rel') != undefined) {
                        btn.attr("title", $(this).attr('data-tip-title'));
                        btn.attr("rel", $(this).attr('rel'));
                        //btn.cluetip({ width: '600px' });
                    }
                    else {
                        btn.attr("title", $(this).attr('title'));
                        //btn.cluetip({ splitTitle: '|', showTitle: false });
                    }
                    label.append(btn);
                }
            }
        }
    });
}


$(document).ready(function () {
    $('span[help]').each(function () {
        var help = $(this).attr('help');
        if (help != undefined) {
            var parts = help.split('|');
            if (parts.length > 1) {
                var baseUrl = '';
                if (window.location.href.indexOf('/handle/') > 0)
                    baseUrl = window.location.href.substring(0, window.location.href.indexOf('/handle/') + 1);
                
                var imgUrl = baseUrl + 'themes/Heal/images/information.png';
                var helpUrl = baseUrl + parts[2];
                var span = $('<span>' + parts[0] + '</span>');
                var img = $('<img>').attr('rel', helpUrl).attr('title', parts[1]).attr('src', imgUrl).addClass('help-tip-button data-tip').text(' ');
                img.appendTo(span);
                $(this).html(span);
            }
        }
    });

    $(this).find('input[data-tip]').helptip();
    $(this).find('select[data-tip]').helptip();
    $(this).find('.data-tip').helptip();

});