package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.GuiContext;

public class Button extends WidgetBase {

    private String caption;

    public Observable<ClickEvent> onclick = new Observable<ClickEvent>();

    public Button(GuiContext context, String caption) {
	super(context);
	context.generateId(this);
	this.caption = caption;
	this.jsPipe
		.addStatement(String.format("new Button(%s, %s);\n",
			JSONObject.quote(this.getId()),
			JSONObject.quote(this.caption)));
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

	if (type.equals("click")) {

	    ClickEvent event = new ClickEvent();
	    event.setSource(this);
	    this.onclick.fire(event);
	} else {
	    throw new RuntimeException("Unknown event type: " + type);
	}

    }

}
