package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class InputSourceWidgetCombinator<T> implements InputSourceWidget<T> {
    
    private Widget widget;
    private InputSource<T> inputsource;

    public InputSourceWidgetCombinator(Widget widget , InputSource<T> inputsource){
	this.widget = widget;
	this.inputsource = inputsource;
    }

    public void setContext(GuiContext context) {
	widget.setContext(context);
    }

    public GuiContext getContext() {
	return widget.getContext();
    }

    public void constructJSObject(GuiContext context) {
	widget.constructJSObject(context);
    }

    public void receiveEvent(GuiContext context, JSONObject event) {
	widget.receiveEvent(context, event);
    }

    public T getValue() {
	return inputsource.getValue();
    }

    public void setValue(T value) {
	inputsource.setValue(value);
    }

    public boolean hasValidInput() {
	return inputsource.hasValidInput();
    }

    public Observable<? extends ChangeEvent> getOnChange() {
	return inputsource.getOnChange();
    }

}
