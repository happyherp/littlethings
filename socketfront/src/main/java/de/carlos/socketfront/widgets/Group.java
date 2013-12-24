package de.carlos.socketfront.widgets;

public class Group extends WidgetBase implements Parent {

    @Override
    public void constructJSObject() {
	jsPipe.addCall("new Group", this.getId());
    }

    @Override
    public void add(Widget child) {
	this.callThisJS("addChild", child.getId());
    }

}
