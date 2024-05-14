package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;

public abstract class Input<T> extends JSInputSourceWidget<T> {

    String stringvalue = "";

    @Override
    public Input<T> createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	this.jsPipe.addCall("new TextInput", this.getId(),
		this.getStringValue());
	this.setValueInner(this.getStringValue());// Needed so can react to
						  // invalid Input with messages
						  // in FilteredInput from the start
	return this;
    }

    public String getStringValue() {
	return stringvalue;
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
	this.stringvalue = value;
    }

    protected void reactToChange(JSONObject jsonevent) {
	this.setValueInner(jsonevent.getString("value"));
    }

}
