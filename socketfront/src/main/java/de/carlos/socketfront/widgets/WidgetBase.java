package de.carlos.socketfront.widgets;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.JSPipe;

public abstract class WidgetBase implements Widget {

    private static Logger LOGGER = Logger.getLogger(WidgetBase.class);

    protected JSPipe jsPipe;
    private GuiContext context;
    
    public void setContext(GuiContext context){
	this.jsPipe = context.getJsPipe();
	this.context = context;	
    }

    @Override
    public String getId() {

	if (this.context.getId(this) == null) {
	    this.context.generateId(this);
	}

	return this.context.getId(this);
    }

    @Override
    public void receiveEvent(JSONObject event) {
	LOGGER.info("Unhandled Event received: " + event.toString(2));
    }

    protected String getJSObject() {
	return String.format("idToWidget[%s]", JSONObject.quote(this.getId()));
    }

    protected void callThisJS(String method, Object... args) {
	context.getJsPipe().addCall(getJSObject() + "." + method, args);
    }

}
