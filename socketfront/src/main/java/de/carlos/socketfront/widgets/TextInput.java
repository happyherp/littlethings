package de.carlos.socketfront.widgets;

import org.json.JSONObject;

public class TextInput extends WidgetBase {

    String value = "";
    
    @Override
    public void constructJSObject() {
	this.jsPipe.addCall("new TextInput", this.getId(), this.getValue());
    }
    

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.callThisJS("setValue", value);
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
