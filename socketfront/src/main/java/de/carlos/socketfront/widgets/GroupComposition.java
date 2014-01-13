package de.carlos.socketfront.widgets;

import de.carlos.socketfront.GuiContext;

public class GroupComposition implements Widget {

    protected Group group;

    private boolean vertical;

    protected GuiContext context;

    public GroupComposition() {
	this(false);
    }

    public GroupComposition(boolean vertical) {
	this.vertical = vertical;
    }

    @Override
    public JSWidget createJSWidget(GuiContext context) {
	this.context = context;
	if (vertical) {
	    this.group = new VGroup();
	} else {
	    this.group = new Group();
	}
	this.group.createJSWidget(context);
	return this.group;
    }

    @Override
    public JSWidget getMainJSWidget() {
	return this.group;
    }

    @Override
    public void addInfo(Widget info) {
	this.group.addInfo(info);
    }

    @Override
    public void remove() {
	this.group.remove();
    }

}
