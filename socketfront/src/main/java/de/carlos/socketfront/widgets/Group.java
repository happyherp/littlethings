package de.carlos.socketfront.widgets;

import de.carlos.socketfront.GuiContext;

public class Group extends WidgetBase implements Parent {

    @Override
    public void constructJSObject(GuiContext context) {
	jsPipe.addCall("new Group", this.getId());
    }

    @Override
    public void add(Widget child) {
	this.callThisJS("addChild", this.getContext().getId(child));
    }

}
