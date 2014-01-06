package de.carlos.socketfront.widgets.events;

import de.carlos.socketfront.GuiContext;


public class ChangeEvent<T> extends Event<T>{

    public ChangeEvent(T source, GuiContext context) {
	super(source, context);
    }


}
