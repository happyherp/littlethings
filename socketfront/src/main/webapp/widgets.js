
idToWidget = {};


Widget = function(id){	
	this.id = id;
	idToWidget[id] = this;

	this.mainDiv = document.createElement("div");
	this.mainDiv.style.border = "1 px solid black";
	this.mainDiv.style.margin = "3px";	
};

Widget.prototype.addTo = function(widgetId){
	idToWidget[widgetId].appendChild(this);
};

Button = function(id, caption) {

	Widget.call(this, id);

	this.caption = caption;

	this.input = document.createElement("input");
	this.input.type="submit";
	this.input.value = this.caption;
	this.mainDiv.appendChild(this.input);

	this.input.onclick = this.clickHandler.bind(this);

};

extend(Widget.prototype, Button.prototype)

Button.prototype.clickHandler = function(e) {
	console.log("button clicked", e);
	var event = {
			id:this.id,
			type:"click",
			data:e
			};
	ws.send(JSON.stringify(event));
};

TextInput = function(id, value){
	Widget.call(this, id);
	
	this.input = document.createElement("input");
	this.input.type="text";
	this.input.value = value;
	this.mainDiv.appendChild(this.input);

	this.input.onchange = this.changeHandler.bind(this);	
}

extend(Widget.prototype, TextInput.prototype)

TextInput.prototype.changeHandler = function(e) {
	console.log("input changed clicked", e);
	var event = {
			id:this.id,
			type:"change",
			value:this.input.value,
			data:e
			};
	ws.send(JSON.stringify(event));
};

TextInput.prototype.setValue = function(value){
	this.input.value = value;
};