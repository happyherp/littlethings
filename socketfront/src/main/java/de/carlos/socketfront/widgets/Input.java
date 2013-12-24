package de.carlos.socketfront.widgets;

import org.json.JSONObject;

public class Input extends ControlWidget {

    String value = "";
    
    @Override
    public void constructJSObject() {
	this.jsPipe.addCall("new TextInput", this.getId(), this.getStringValue());
	this.setValueInner(this.getStringValue());//Needed so can react to invalid Input with messages in FilteredInput
    }
    

    public String getStringValue() {
	return value;
    }

    /**
     * For updating the state from the server side.
     * @param value
     */
    public void setStringValue(String value) {
	this.callThisJS("setValue", value);
	this.setValueInner(value);
    }   
    
    protected void setValueInner(String value){
	this.value = value;	
    }
    
    public void receiveEvent(JSONObject jsonevent){
	String type = jsonevent.getString("type");
	if (type.equals("change") && !this.isDisabled()){
	    this.setValueInner(jsonevent.getString("value"));
	}else{
	   throw new RuntimeException("Unknown event type: "+type);
	}
    }

    
}
