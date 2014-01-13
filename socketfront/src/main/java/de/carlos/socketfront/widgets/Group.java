package de.carlos.socketfront.widgets;

import java.util.List;

import de.carlos.socketfront.GuiContext;

public class Group extends ParentWidget implements Parent {
    

    @Override
    public Group createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	jsPipe.addCall("new Group", this.getId());
	return this;
    }

    @Override
    public <T extends Widget>  T add(T child) {
	this.callThisJS("addChild", this.context.getId(child.getMainJSWidget()));
	this.children.add(child);
	return child;
    }
    
    public List<Widget> getChildren(){
	return this.children;
    }
    
}
