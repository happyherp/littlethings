package de.carlos.socketfront.widgets.events;

import de.carlos.socketfront.GuiContext;


public class Event<T> {

    private T source;
    
    private GuiContext context;

    public Event(T source) {
	this.source = source;
    }

    public T getSource() {
	return source;
    }

    public void setSource(T source) {
	this.source = source;
    }

    public GuiContext getContext() {
        return context;
    }

    public void setContext(GuiContext context) {
        this.context = context;
    }

}
