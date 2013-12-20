var ws = null;

function connect(endpoint) {
	var target = 'ws://' + window.location.host + '/socketfront/'+endpoint;
	console.log("connecting");
	if ('WebSocket' in window) {
		ws = new WebSocket(target);
	} else if ('MozWebSocket' in window) {
		ws = new MozWebSocket(target);
	} else {
		alert('WebSocket is not supported by this browser.');
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
}

function disconnect() {
	if (ws != null) {
		ws.close();
		ws = null;
	}
	setConnected(false);
}

function copyObj(obj){
	var copy = {};
	for (var key in obj){
		copy[key] = obj[key];
	}
	return copy;
}

/**
 * Extends all attributes of source to dest.
 * @param source
 * @param dest
 */
function extend(source, dest){
	for (var key in source){
		dest[key] = source[key];
	}	
}

