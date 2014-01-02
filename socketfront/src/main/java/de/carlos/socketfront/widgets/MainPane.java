package de.carlos.socketfront.widgets;

import org.json.JSONObject;

public class MainPane extends WidgetBase implements Parent {
    
    @Override
    public String getId() {
	return "mainpane";
    }

    @Override
    public void add(Widget child) {
	jsPipe.addStatement(String.format(
		"GuiInfo.idToWidget[%s].appendChild(GuiInfo.idToWidget[%s].mainDiv);",
		JSONObject.quote(this.getId()), JSONObject.quote(child.getId())));
    }

    @Override
    public void constructJSObject() {
	// Should already be created...
    }

}
