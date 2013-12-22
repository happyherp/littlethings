
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
	console.log("input changed", e);
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


Checkbox = function(id){
	Widget.call(this, id);
	
	this.input = document.createElement("input");
	this.input.type="checkbox";
	this.input.checked = false;
	this.mainDiv.appendChild(this.input);

	this.input.onchange = this.changeHandler.bind(this);		
}

extend(Widget, Checkbox);

Checkbox.prototype.changeHandler = function(e) {
	console.log("input changed", e);
	var event = {
			id:this.id,
			type:"change",
			value:this.input.checked,
			data:e
			};
	ws.send(JSON.stringify(event));
};

Checkbox.prototype.setValue = function(value){
	this.input.checked = value;
};

Select = function(id){
	Widget.call(this, id);
	
	this.select = document.createElement("select");
	this.mainDiv.appendChild(this.select);

	this.select.onchange = this.changeHandler.bind(this);		
}

extend(Widget, Select);

Select.prototype.addOption = function(id, label){
	var option = document.createElement("option");
	option.id = id;
	option.appendChild(document.createTextNode(label));
	this.select.appendChild(option);
}

Select.prototype.changeHandler = function(e) {
	console.log("select changed", e);
	var event = {
			id:this.id,
			type:"change",
			optionid:this.select.options[this.select.selectedIndex].id,
			data:e
			};
	ws.send(JSON.stringify(event));
};

Select.prototype.setSelected = function(id){
	
	var newIndex = -1;
	for (var i = 0; i<this.select.options.length;i++){
		if (this.select.options[i].id == id){
			newIndex = i;
		}
	}
	if (newIndex == -1){
		throw "Could not find option with id "+ id;
	}
	
	this.select.selectedIndex = newIndex;
};


