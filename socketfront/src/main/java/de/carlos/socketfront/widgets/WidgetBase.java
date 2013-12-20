package de.carlos.socketfront.widgets;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.JSPipe;

public class WidgetBase implements Widget {

    private static Logger LOGGER = Logger.getLogger(WidgetBase.class);

    protected JSPipe jsPipe;
    private GuiContext context;

    protected WidgetBase(GuiContext context) {
	this.jsPipe = context.getJsPipe();
	this.context = context;
    }

    @Override
    public String getId() {
	return this.context.getId(this);
    }

    @Override
    public void setId(String id) {
	if (this.getId() == null) {
	    this.context.setId(this, id);
	} else {
	    throw new RuntimeException("Id was already set.");
	}

    }

    @Override
    public void receiveEvent(JSONObject event) {
	LOGGER.info("Unhandled Event received: " + event.toString(2));
    }
    
    protected String getJSObject(){
	return String.format("idToWidget[%s]", JSONObject.quote(this.getId()));
    }

}
