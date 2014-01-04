package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class Radiobutton<T> extends InputSourceWidgetBase<Boolean> {
    
    T object;
    
    RadioGroup<T> group;

    public Radiobutton(RadioGroup<T> group, T value) {
	this.group = group;
	this.object = value;
	
    }

    @Override
    public void constructJSObject() {
	this.jsPipe.addCall("new RadioButton", this.getId(), this.group.getName());
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
	
    }

    @Override
    public boolean hasValidInput() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public Observable<? extends ChangeEvent> getOnChange() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected void fireOnChangeEvent(JSONObject jsonobject) {
	// TODO Auto-generated method stub
	
    }

}
