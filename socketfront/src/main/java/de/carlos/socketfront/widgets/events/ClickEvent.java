package de.carlos.socketfront.widgets.events;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.JSWidget;


public class ClickEvent<T extends JSWidget> extends Event<T>{

    public ClickEvent(T source, GuiContext context) {
	super(source, context);
    }

}
