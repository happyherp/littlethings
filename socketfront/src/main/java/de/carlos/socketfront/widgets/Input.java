package de.carlos.socketfront.widgets;

import org.json.JSONObject;

public abstract class Input<T> extends InputSourceWidget<T, InputSource<T>> {

    String value = "";

    @Override
    public void constructJSObject() {
	this.jsPipe.addCall("new TextInput", this.getId(),
		this.getStringValue());
	this.setValueInner(this.getStringValue());// Needed so can react to
						  // invalid Input with messages
						  // in FilteredInput
    }

    public String getStringValue() {
	return value;
    }

    /**
     * For updating the state from the server side.
     * 
     * @param value
     */
    public void setStringValue(String value) {
	this.callThisJS("setValue", value);
	this.setValueInner(value);
    }

    protected void setValueInner(String value) {
	this.value = value;
    }

    protected void reactToChange(JSONObject jsonevent) {
	this.setValueInner(jsonevent.getString("value"));
    }

}
