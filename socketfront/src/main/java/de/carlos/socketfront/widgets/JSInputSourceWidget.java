package de.carlos.socketfront.widgets;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public abstract class JSInputSourceWidget<T> extends JSControlWidget implements
	InputSourceWidget<T> {

    private static Logger LOGGER = Logger.getLogger(JSInputSourceWidget.class);

    protected Observable<? extends ChangeEvent<?>> onchange = new Observable<>();

    abstract protected void fireOnChangeEvent(JSONObject jsonobject);

    /**
     * 
     * Will be called for onChange events that happened on the browser. Updates
     * the state of the widget with the information from the object.
     * 
     * @param object
     */
    abstract protected void reactToChange(JSONObject jsonevent);

    public void receiveEvent(GuiContext context, JSONObject jsonevent) {
	if (jsonevent.getString("type").equals("change")) {
	    if (this.isDisabled()) {
		LOGGER.info("Got on change-Event but Widget was Disabled. Id: "
			+ this.getId());
	    } else {
		this.reactToChange(jsonevent);
		if (this.hasValidInput()) {
		    this.fireOnChangeEvent(jsonevent);
		}
	    }

	} else {
	    throw new RuntimeException("Unhandled event type "
		    + jsonevent.getString("type"));
	}
    }

}
