package de.carlos.socketfront.widgets;

import java.util.ArrayList;
import java.util.List;

import de.carlos.socketfront.GuiContext;

public class Window extends ParentWidget implements Parent  {

    private List<Widget> children = new ArrayList<>();

    @Override
    public Window createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	this.jsPipe.addCall("new Window", getId());
	this.setPositionAbsolute(10, 10);
	return this;
    }

    @Override
    public <T extends Widget>  T add(T child) {
	this.callThisJS("addChild", this.getContext().getId(child.getMainJSWidget()));
	this.children.add(child);
	return child;
	
    }

    public void close() {
	this.remove();
    }
    

}
