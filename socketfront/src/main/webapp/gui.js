var ws = null;

function connect() {
	var target = 'ws://' + window.location.host + '/socketfront/guiEndpoint';
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

idToWidget = {};

Button = function(id, caption) {

	this.id = id;
	idToWidget[id] = this;

	this.caption = caption;

	this.mainDiv = document.createElement("div");
	this.mainDiv.style.border = "1 px solid black";
	this.mainDiv.style.margin = "3px";
	this.captionDiv = document.createElement("div");
	this.captionDiv.appendChild(document.createTextNode(this.caption));
	this.mainDiv.appendChild(this.captionDiv);

	this.mainDiv.onclick = this.clickHandler.bind(this);

};

Button.prototype.clickHandler = function(e) {
	console.log("button clicked", e);
	var event = {
			id:this.id,
			type:"click",
			data:e
			};
	ws.send(JSON.stringify(event));
};

Button.prototype.addTo = function(widgetId){
	idToWidget[widgetId].appendChild(this);
};