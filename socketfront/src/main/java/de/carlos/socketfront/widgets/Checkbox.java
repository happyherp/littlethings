package de.carlos.socketfront.widgets;

import org.json.JSONObject;

public class Checkbox extends InputSourceWidget<Boolean, Checkbox> {

    Boolean value = false;
    
    @Override
    public void constructJSObject() {
	this.jsPipe.addCall("new Checkbox", this.getId());
    }
    

    public Boolean getValue() {
	return value;
    }

    public void setValue(Boolean value) {
	this.callThisJS("setValue", value);
	this.value = value;
    }


    @Override
    public boolean hasValidInput() {
	return true;
    }


    @Override
    Checkbox getThis() {
	return this;
    }


    @Override
    protected void reactToChange(JSONObject jsonevent) {
	this.value = jsonevent.getBoolean("value");	
    }


}
