var ws = null;

function connect(guiname) {
	
	if ('WebSocket' in window || 'MozWebSocket' in window){
		connectSocket(guiname);
	}else{
		connectAjax(guiname);
	}
}

function connectSocket(guiname){
	
	var endpoint = "guiEndpoint";
	var target = 'ws://' + window.location.host + '/socketfront/'+endpoint+ "/"+guiname;
	console.log("connecting");
	if ('WebSocket' in window) {
		ws = new WebSocket(target);
	} else if ('MozWebSocket' in window) {
		ws = new MozWebSocket(target);
	} else {
		window.alert('WebSocket is not supported by this browser. Ajax implentation should have been used.');		
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

function connectAjax(guiname){
	xmlhttp = new window.XMLHttpRequest();
	xmlhttp.open("POST","/socketfront/guiservlet",true);
	xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlhttp.send("opengui="+guiname);	
	xmlhttp.onreadystatechange=function(){
	  if (xmlhttp.readyState==4 && xmlhttp.status==200){
	    processResponse(xmlhttp.responseText);
	  }
	} 
	
}


function processResponse(text){
	eval(text);
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
 * Extends the destinations prototype with all methods from the sources prototype.
 * @param source
 * @param dest
 */
function extend(source, dest){
	for (var key in source.prototype){
		dest.prototype[key] = source.prototype[key];
	}	
}


