package de.carlos.socketfront.widgets;


import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public abstract class InputSourceWidget<T> extends ControlWidget implements InputSource<T> {
    
    private static Logger LOGGER = Logger.getLogger(InputSourceWidget.class);  
    
    protected Observable<? extends ChangeEvent> onchange = new Observable<>();
        
    abstract protected void fireOnChangeEvent(JSONObject jsonobject);
        
    /**
     * 
     * Will be called for onChange events that happened on the browser. Updates the state of the widget with 
     * the information from the object.
     * 
     * @param object
     */
    abstract protected void reactToChange(JSONObject jsonevent);

    public void receiveEvent(JSONObject jsonevent) {
	if (jsonevent.getString("type").equals("change")) {
	    if (this.isDisabled()){
		LOGGER.info("Got on change-Event but Widget was Disabled. Id: "+this.getId());
	    }else{
		this.reactToChange(jsonevent);
		this.fireOnChangeEvent(jsonevent);
	    }
	    
	} else {
	    throw new RuntimeException("Unhandled event type "
		    + jsonevent.getString("type"));
	}
    }
    

}
