package de.carlos.socketfront.widgets.events;

import de.carlos.socketfront.widgets.Widget;

public class Event<T extends Widget> {

    private T source;

    public Event(T source) {
	this.source = source;
    }

    public T getSource() {
	return source;
    }

    public void setSource(T source) {
	this.source = source;
    }

}
