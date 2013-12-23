package de.carlos.socketfront.widgets;

public class Window extends WidgetBase implements Parent  {

    @Override
    public void constructJSObject() {
	this.jsPipe.addCall("new Window", getId());
	this.setPositionAbsolute(10, 10);
    }

    @Override
    public void add(Widget child) {
	this.callThisJS("addChild", child.getId());
	
    }

    public void close() {
	this.remove();
    }

}
