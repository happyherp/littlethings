package de.carlos.socketfront.widgets;

import de.carlos.socketfront.GuiContext;

public class Group extends JSWidgetBase implements Parent {

    @Override
    public Group createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	jsPipe.addCall("new Group", this.getId());
	return this;
    }

    @Override
    public <T extends Widget>  T add(T child) {
	this.callThisJS("addChild", this.context.getId(child.getMainJSWidget()));
	return child;
    }

}
