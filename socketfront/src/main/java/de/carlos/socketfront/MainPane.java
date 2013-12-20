package de.carlos.socketfront;

import org.json.JSONObject;

public class MainPane extends WidgetBase implements Parent {

    protected MainPane(GuiContext context) {
	super(context);
	this.setId("mainpane");
    }

    @Override
    public void add(Widget child) {
	jsPipe.addStatement(String.format("idToWidget[%s].appendChild(idToWidget[%s].mainDiv);", 
		JSONObject.quote(this.getId()), JSONObject.quote(child.getId())));
    }

}
