package de.carlos.socketfront.widgets;

import org.json.JSONObject;

public class Checkbox extends WidgetBase {

    Boolean value = false;
    
    @Override
    public void constructJSObject() {
	this.jsPipe.addCall("new Checkbox", this.getId());
    }
    

    public void receiveEvent(JSONObject jsonevent) {
	String type = jsonevent.getString("type");
	if (type.equals("change")) {
	    this.value = jsonevent.getBoolean("value");
	} else {
	    throw new RuntimeException("Unknown event type: " + type);
	}
    }

    public Boolean getValue() {
	return value;
    }

    public void setValue(Boolean value) {
	this.callThisJS("setValue", value);
	this.value = value;
    }


}
