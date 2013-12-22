package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;

public class Checkbox extends WidgetBase {

    Boolean value = false;

    public Checkbox(GuiContext context) {
	super(context);
	context.generateId(this);
	callConstructorWithId("Checkbox");

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
	String jsbool = value ? "true" : "false";//TODO: remove if not necessary.
	jsPipe.addStatement(String.format(getJSObject() + ".setValue(%s);\n",
		JSONObject.valueToString(value)));
	this.value = value;
    }
}
