package de.carlos.socketfront.widgets.events;


public class ChangeEvent<T> extends Event<T>{

    public ChangeEvent(T source) {
	super(source);
    }

}
