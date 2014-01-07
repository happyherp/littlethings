package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class RadioButton<T> extends InputSourceWidgetBase<Boolean> {
    
    T object;
    
    RadioGroup<T> group;
    
    public RadioButton(RadioGroup<T> group, T value) {
	this.group = group;
	this.object = value;
	
    }

    @Override
    public RadioButton<T> createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	this.jsPipe.addCall("new RadioButton", this.getId(), this.group.getName());
	return this;
    }

    public T getObject() {
	return this.object;
    }
    
    @Override
    protected void reactToChange(JSONObject jsonevent) {
	this.group.onButtonChange(this, jsonevent.getBoolean("value"));
    }

    @Override
    public Boolean getValue() {
	return this.group.getValue().equals(this.object);
    }

    @Override
    public void setValue(Boolean value) {
	this.group.setValue(this.getObject());
    }

    @Override
    public boolean hasValidInput() {
	return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<ChangeEvent<RadioButton<T>>> getOnChange() {
	return (Observable<ChangeEvent<RadioButton<T>>>) this.onchange;
    }

    @Override
    protected void fireOnChangeEvent(JSONObject jsonobject) {
	
    }

}
