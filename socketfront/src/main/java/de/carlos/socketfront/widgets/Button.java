package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.observer.Observable;

public class Button extends ControlWidget {

    private String caption;

    public Observable<ClickEvent> onclick = new Observable<ClickEvent>();

    public Button(String caption) {
	this.caption = caption;
    }    

    @Override
    public void constructJSObject() {
	this.jsPipe.addCall("new Button", this.getId(), this.caption);
    }
    

    public String getCaption() {
	return this.caption;
    }

    public Observable<ClickEvent> getOnClick() {
	return this.onclick;
    }

    @Override
    public void receiveEvent(JSONObject jsonevent) {

	String type = jsonevent.getString("type");

	if (type.equals("click") && !this.isDisabled()) {

	    ClickEvent event = new ClickEvent();
	    event.setSource(this);
	    this.onclick.fire(event);
	} else {
	    throw new RuntimeException("Unknown event type: " + type);
	}

    }

}
