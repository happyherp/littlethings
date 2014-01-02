
var GuiInfo = {
	ws : null, // Running websocket, if used
	guiId : null,// Used so the server knows to which currently open
					// gui-instance we are
	// talking to. Will be set from the response to connectAjax();
	idToWidget : {}

}

function connect(guiname) {

	GuiInfo.idToWidget["mainpane"] = document.getElementById("maincontainer");

	if ('WebSocket' in window || 'MozWebSocket' in window) {
		connectSocket(guiname);
	} else {
		connectAjax(guiname);
	}
}

function connectSocket(guiname) {

	var ws = null;
	var endpoint = "guiEndpoint";
	var target = 'ws://' + window.location.host + '/socketfront/' + endpoint
			+ "/" + guiname;
	console.log("connecting");
	if ('WebSocket' in window) {
		ws = new WebSocket(target);
	} else if ('MozWebSocket' in window) {
		ws = new MozWebSocket(target);
	} else {
		window
				.alert('WebSocket is not supported by this browser. Ajax implentation should have been used.');
		return;
	}
	ws.onopen = function() {
		console.log('Info: WebSocket connection opened.');
	};
	ws.onmessage = function(event) {
		console.log('Received: ' + event.data);
		eval(event.data);
	};
	ws.onclose = function() {
		console.log('Info: WebSocket connection closed.');
	};

	GuiInfo.ws = ws;
}

function connectAjax(guiname) {
	var data = {
		openGui : guiname
	};
	post("/socketfront/guiservlet", JSON.stringify(data), processResponse);

}

function sendEvent(event) {
	event.guiId = GuiInfo.guiId;
	var asText = JSON.stringify(event)
	if (GuiInfo.ws != null) {
		GuiInfo.ws.send(asText);
	} else {
		post("/socketfront/guiservlet",asText, processResponse);
	}
}

function processResponse(text) {
	eval(text);
}

function disconnect() {
	if (ws != null) {
		ws.close();
		ws = null;
	}
	setConnected(false);
}

function copyObj(obj) {
	var copy = {};
	for ( var key in obj) {
		copy[key] = obj[key];
	}
	return copy;
}

function newAjax() {
	var xmlhttp;
	if (window.XMLHttpRequest) {
		// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xmlhttp;
}

function post(url, value, callback) {
	var xmlhttp = newAjax();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			callback(xmlhttp.responseText);
		}
	};
	xmlhttp.open("POST", url, true);
	xmlhttp.send(value);
}

/**
 * Extends the destinations prototype with all methods from the sources
 * prototype.
 * 
 * @param source
 * @param dest
 */
function extend(source, dest) {
	for ( var key in source.prototype) {
		dest.prototype[key] = source.prototype[key];
	}
}
