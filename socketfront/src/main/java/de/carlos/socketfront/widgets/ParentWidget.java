package de.carlos.socketfront.widgets;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentWidget extends JSWidgetBase implements Parent {

    protected List<Widget> children = new ArrayList<>();

    @Override
    public void remove(){
	for (Widget child: this.children ){
	    child.remove();
	}
	super.remove();
    }

}
