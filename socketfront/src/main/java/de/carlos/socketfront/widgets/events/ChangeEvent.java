package de.carlos.socketfront.widgets.events;

import de.carlos.socketfront.widgets.InputSource;

public class ChangeEvent<T extends InputSource<?>> extends Event<T>{

    public ChangeEvent(T source) {
	super(source);
    }

}
