function createSelect(acElement, serviceURI, healpUrl) {
	// create select scheme/qualifier
	var selectElementID = acElement.attr("id") + "_qualifier";
	var labelElement = '<label class="ds-composite-component">'
			+ '<select class="ds-select-field ds-select-scheme"' + 'id="'
			+ selectElementID + '">' + '</label>';

	acElement.parent().after(labelElement);
	var selectElement = $('#' + selectElementID);

	selectElement.append("<option value=''> All schemes </option>");

	$.getJSON(serviceURI, {
		getSchemes : "1"
	}, function(data) {
		$.each(data, function(key, val) {
			selectElement.append("<option value='" + val.value + "'>"
					+ val.label + "</option>");
		});
	});

	selectElement.css({'width': 200});
	
	acElement.autocomplete().data("autocomplete")._renderItem = function(ul,
			item) {
		return $("<li></li>").data("item.autocomplete", item).append(
				"<a><strong>" + item.label + "</strong> / " + item.scheme
						+ "</a>").appendTo(ul);
	};
	
	var redirectElementID = acElement.attr("id") + "_button";
	
	var redirectString = "<label class='ds-composite-component'><button id='" + redirectElementID + "'> ? </button></label>";
	
	acElement.parent().after(redirectString);
	
	redirectElement = $('#' + redirectElementID);
	
	redirectElement.click(function (event) {
		var filter=acElement.val();
		window.open(healpUrl + "?tab=subjects&filter=" + encodeURIComponent(filter));
		event.preventDefault();
	});

	return selectElement;
}

function getSelect(acElement) {
	var selectElement = acElement.attr("id") + "_qualifier";
	return selectElement;
}

function updateLabel(oldLabel) {
	var newLabel = oldLabel;
	if (oldLabel.indexOf("http://") >= 0) {
		var index=oldLabel.indexOf("http://");
		var uri = oldLabel.substring(oldLabel.indexOf("http://"), oldLabel.indexOf(")", index));
		var brk = oldLabel.substring(oldLabel.indexOf("("), oldLabel.indexOf(")") + 1);
		var label = oldLabel.replace(brk, "");
		newLabel = "<a + target='_blank' href='" + uri + "' >" + label + "</a>";
	}
	return newLabel;
}