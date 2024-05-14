package de.carlos.socketfront.widgets.events;

import de.carlos.socketfront.GuiContext;


public class Event<T> {

    private T source;
    
    private GuiContext context;

    public Event(T source, GuiContext context) {
	this.source = source;
	this.context = context;
	assert(context != null);
	assert(source != null);
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
