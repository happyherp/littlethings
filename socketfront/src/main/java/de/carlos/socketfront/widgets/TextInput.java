package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;

public class TextInput extends WidgetBase {

    String value = "";

    public TextInput(GuiContext context) {
	super(context);
	context.generateId(this);
	this.jsPipe.addStatement(String.format("new TextInput(%s, %s);\n",
		JSONObject.quote(this.getId()),
		JSONObject.quote(this.getValue())));
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.jsPipe.addStatement(String.format(this.getJSObject()
		+ ".setValue(%s)", JSONObject.quote(value)));
	this.value = value;
    }    
    
    public void receiveEvent(JSONObject jsonevent){
	String type = jsonevent.getString("type");
	if (type.equals("change")){
	    this.value = jsonevent.getString("value");
	}else{
	   throw new RuntimeException("Unknown event type: "+type);
	}
    }
    
}
