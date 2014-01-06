package de.carlos.socketfront.widgets.events;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Widget;


public class ClickEvent<T extends Widget> extends Event<T>{

    public ClickEvent(T source, GuiContext context) {
	super(source, context);
    }

}
