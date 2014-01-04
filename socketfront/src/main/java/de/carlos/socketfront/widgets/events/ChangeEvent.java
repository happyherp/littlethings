package de.carlos.socketfront.widgets.events;

import de.carlos.socketfront.widgets.InputSourceWidget;

public class ChangeEvent<T extends InputSourceWidget<?>> extends Event<T>{

    public ChangeEvent(T source) {
	super(source);
    }

}
