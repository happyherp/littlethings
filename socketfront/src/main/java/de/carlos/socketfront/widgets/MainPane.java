package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;

public class MainPane extends JSWidgetBase implements Parent {
    
    public static JSWidgetID MAINPANEID = new JSWidgetID("mainpane"); 

    @Override
    public <T extends Widget> T add(T child) {
	jsPipe.addStatement(String
		.format("GuiInfo.idToWidget[%s].appendChild(GuiInfo.idToWidget[%s].mainDiv);",
			JSONObject.quote(this.getId().toString()),
			JSONObject.quote(this.context.getId(
				child.getMainJSWidget()).toString())));
	return child;
    }

    @Override
    public MainPane createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	// This Widget should already have an ID and be present in the
	// javascript.
	return this;
    }
    
    @Override
    public void registerToContext(GuiContext context){
	context.setId(this, MAINPANEID);
    }

}
