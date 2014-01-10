


Widget = function(id){	
	this.id = id;
	GuiInfo.idToWidget[id] = this;

	this.mainDiv = document.createElement("div");
	this.mainDiv.style.border = "1 px solid black";
	this.mainDiv.style.margin = "3px";	
	
	this.contentDiv = document.createElement("div");
	this.mainDiv.appendChild(this.contentDiv);
	
	this.infoDiv = document.createElement("div");
	this.mainDiv.appendChild(this.infoDiv);
};

Widget.prototype.addTo = function(widgetId){
	GuiInfo.idToWidget[widgetId].appendChild(this);
};

Widget.prototype.setPositionAbsolute = function(x,y){
	this.mainDiv.style.position = "absolute";
	this.mainDiv.style.left = x;
	this.mainDiv.style.top = y;
};

Widget.prototype.remove = function(){
	GuiInfo.idToWidget[this.id] = undefined;
	if (this.mainDiv.parentNode != null){
		this.mainDiv.parentNode.removeChild(this.mainDiv);
	}
}

Widget.prototype.addInfoText = function(widgetId){
	this.infoDiv.appendChild(GuiInfo.idToWidget[widgetId].mainDiv);
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
	sendEvent(event);
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
	sendEvent(event);
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
	sendEvent(event);
};

Checkbox.prototype.setValue = function(value){
	this.input.checked = value;
};

RadioButton = function(id, name){
	Widget.call(this,id);
	this.input = document.createElement("input");
	this.input.type="radio";
	this.input.checked = false;
	this.input.name = name;
	this.contentDiv.appendChild(this.input);
	this.input.onchange = this.changeHandler.bind(this);		
	
}

extend(Widget, RadioButton);

RadioButton.prototype.setDisabled =  Button.prototype.setDisabled
RadioButton.prototype.setValue =  Checkbox.prototype.setValue
RadioButton.prototype.changeHandler = Checkbox.prototype.changeHandler;

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
	sendEvent(event);
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

Grid = function(id, cols, rows){
	Widget.call(this, id);

	
	this.table = document.createElement("table");
	this.table.border = "1px";
	
	this.cols = cols;
	this.rows = 0
	for (var row = 0; row < rows; row++){
		this.addRow(0);
	}
	
	this.contentDiv.appendChild(this.table);
}

extend(Widget, Grid);

Grid.prototype.setCell = function(widgetId, col, row){
	var td = this.table.childNodes[row].childNodes[col];
	while (td.hasChildNodes()) {
		td.removeChild(td.lastChild);
	}

	td.appendChild(GuiInfo.idToWidget[widgetId].mainDiv);
	
}

Grid.prototype.addRow = function(rowindex){
	
	var tr = document.createElement("tr");		
	for (var col = 0; col<this.cols;col++){
		var td = document.createElement("td");
		tr.appendChild(td);
	}		
	
	var rowToMove =this.table.childNodes[rowindex]; 
	if (rowToMove){
		this.table.insertBefore(tr, rowToMove)
	}else{
		this.table.appendChild(tr);
	}
	
	this.rows++;
}

Grid.prototype.removeRow = function(rowindex){
	this.table.removeChild(this.table.childNodes[rowindex]);
	this.rows--;
}

Grid.prototype.addColumn = function(colindex){
	for (var row = 0; row<this.rows;row++){
		var td = document.createElement("td");
		var tr = this.table.childNodes[row];
		
		var tdToMove = tr.childNodes[colindex];
		if (tdToMove){
			tr.insertBefore(td, tdToMove);
		}else{
			tr.appendChild(td);
		}
	}
	this.cols++;
}

Grid.prototype.clear = function(){
	while (this.table.childNodes.length > 0){
		this.table.removeChild(this.table.childNodes[0]);
	}
	this.cols = 0;
	this.rows = 0;	
}

Window = function(id){
	Widget.call(this, id);
	
	this.mainDiv.style.border = "3px solid #AAA";
	this.mainDiv.style.backgroundColor = "#777"
}
extend(Widget, Window);

Window.prototype.addChild = function(childId){
	this.contentDiv.appendChild(GuiInfo.idToWidget[childId].mainDiv);
}

Group = function(id){
	Widget.call(this, id);
	this.mainDiv.style.border = "1px solid grey";
}

extend(Widget, Group);

Group.prototype.addChild = function(childId){
	this.contentDiv.appendChild(GuiInfo.idToWidget[childId].mainDiv);
}



