
idToWidget = {};


Widget = function(id){	
	this.id = id;
	idToWidget[id] = this;

	this.mainDiv = document.createElement("div");
	this.mainDiv.style.border = "1 px solid black";
	this.mainDiv.style.margin = "3px";	
	
	this.contentDiv = document.createElement("div");
	this.mainDiv.appendChild(this.contentDiv);
	
	this.infoDiv = document.createElement("div");
	this.mainDiv.appendChild(this.infoDiv);
};

Widget.prototype.addTo = function(widgetId){
	idToWidget[widgetId].appendChild(this);
};

Widget.prototype.setPositionAbsolute = function(x,y){
	this.mainDiv.style.position = "absolute";
	this.mainDiv.style.left = x;
	this.mainDiv.style.top = y;
};

Widget.prototype.remove = function(){
	idToWidget[this.id] = undefined;
	if (this.mainDiv.parentNode != null){
		this.mainDiv.parentNode.removeChild(this.mainDiv);
	}
}

Widget.prototype.addInfoText = function(widgetId){
	this.infoDiv.appendChild(idToWidget[widgetId].mainDiv);
}

Button = function(id, caption) {

	Widget.call(this, id);

	this.caption = caption;

	this.input = document.createElement("input");
	this.input.type="submit";
	this.input.value = this.caption;
	this.contentDiv.appendChild(this.input);

	this.input.onclick = this.clickHandler.bind(this);

};

extend(Widget, Button);

Button.prototype.clickHandler = function(e) {
	console.log("button clicked", e);
	var event = {
			id:this.id,
			type:"click",
			data:e
			};
	ws.send(JSON.stringify(event));
};

Button.prototype.setDisabled = function(disabled){
	this.input.disabled = disabled;
}

TextInput = function(id, value){
	Widget.call(this, id);
	
	this.input = document.createElement("input");
	this.input.type="text";
	this.input.value = value;
	this.contentDiv.appendChild(this.input);

	this.input.onchange = this.changeHandler.bind(this);	
}

extend(Widget, TextInput);

TextInput.prototype.setDisabled =  Button.prototype.setDisabled

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
	this.contentDiv.appendChild(this.input);

	this.input.onchange = this.changeHandler.bind(this);		
}

extend(Widget, Checkbox);

Checkbox.prototype.setDisabled =  Button.prototype.setDisabled

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
	this.contentDiv.appendChild(this.select);

	this.select.onchange = this.changeHandler.bind(this);		
}

extend(Widget, Select);

Select.prototype.setDisabled =  function(value){
	this.select.disabled = value;
}

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


Text = function(id, text){
	Widget.call(this, id);
	this.contentDiv.appendChild(document.createTextNode(text));
}

extend(Widget, Text);


Text.prototype.setText = function(text){
	while (this.contentDiv.hasChildNodes()) {
		this.contentDiv.removeChild(this.contentDiv.lastChild);
	}
	this.contentDiv.appendChild(document.createTextNode(text));
	
}

Table = function(id, cols, rows){
	Widget.call(this, id);
	
	this.table = document.createElement("table");
	
	
	this.posToTd=[];
	
	for (var row = 0; row < rows; row++){
		this.posToTd.push([]);
		var tr = document.createElement("tr");
		for (var col = 0; col<cols;col++){
			var td = document.createElement("td");
			this.posToTd[row].push(td);
			tr.appendChild(td);
		}
		this.table.appendChild(tr);
	}
	
	this.contentDiv.appendChild(this.table);
}

extend(Widget, Table);

Table.prototype.setCell = function(widgetId, col, row){
	var td = this.posToTd[row][col];
	while (td.hasChildNodes()) {
		td.removeChild(td.lastChild);
	}

	td.appendChild(idToWidget[widgetId].mainDiv);
	
}

Window = function(id){
	Widget.call(this, id);
	
	this.mainDiv.style.border = "3px solid #AAA";
	this.mainDiv.style.backgroundColor = "#777"
}
extend(Widget, Window);

Window.prototype.addChild = function(childId){
	this.mainDiv.appendChild(idToWidget[childId].mainDiv);
}





