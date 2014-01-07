package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.events.ClickEvent;

public class Button extends ControlWidget {

    private String caption;

    public Observable<ClickEvent<Button>> onclick = new Observable<ClickEvent<Button>>();

    public Button(String caption) {
	this.caption = caption;
    }    

    @Override
    public Button createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	this.jsPipe.addCall("new Button", this.getId(), this.caption);
	return this;
    }
    

    public String getCaption() {
	return this.caption;
    }

    public Observable<ClickEvent<Button>> getOnClick() {
	return this.onclick;
    }

    @Override
    public void receiveEvent(GuiContext context, JSONObject jsonevent) {

	String type = jsonevent.getString("type");

	if (type.equals("click") && !this.isDisabled()) {

	    ClickEvent<Button> event = new ClickEvent<Button>(this, context);
	    this.onclick.fire(event);
	} else {
	    throw new RuntimeException("Unknown event type: " + type);
	}

    }

}
