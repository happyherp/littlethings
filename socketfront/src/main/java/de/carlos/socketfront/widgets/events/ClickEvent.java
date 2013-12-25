package de.carlos.socketfront.widgets.events;

import de.carlos.socketfront.widgets.Widget;


public class ClickEvent<T extends Widget> extends Event<T>{

    public ClickEvent(T source) {
	super(source);
    }
}
