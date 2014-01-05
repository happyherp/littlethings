package de.carlos.socketfront.widgets;

import de.carlos.socketfront.GuiContext;

public class Window extends WidgetBase implements Parent  {

    @Override
    public void constructJSObject(GuiContext context) {
	this.jsPipe.addCall("new Window", getId());
	this.setPositionAbsolute(10, 10);
    }

    @Override
    public void add(Widget child) {
	this.callThisJS("addChild", this.getContext().getId(child));
	
    }

    public void close() {
	this.remove();
    }

}
