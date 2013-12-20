package de.carlos.socketfront;

import org.json.JSONObject;

public class MainPane extends WidgetBase implements Parent {

    protected MainPane(JSPipe jsPipe) {
	super(jsPipe);
	this.id = "mainpane";
    }

    @Override
    public void add(Widget child) {
	jsPipe.addStatement(String.format("idToWidget[%s].appendChild(idToWidget[%s].mainDiv);", 
		JSONObject.quote(this.id), JSONObject.quote(child.getId())));
    }

}
