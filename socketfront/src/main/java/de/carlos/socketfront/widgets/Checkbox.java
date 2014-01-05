package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class Checkbox extends InputSourceWidgetBase<Boolean> {

    Boolean value = false;
    private Observable<ChangeEvent<Checkbox>> onchange = new Observable<>();
    
    @Override
    public void constructJSObject(GuiContext context) {
	this.jsPipe.addCall("new Checkbox", this.getId());
    }
    

    public Boolean getValue() {
	return value;
    }

    public void setValue(Boolean value) {
	this.callThisJS("setValue", value);
	this.value = value;
    }


    @Override
    public boolean hasValidInput() {
	return true;
    }



    @Override
    protected void reactToChange(JSONObject jsonevent) {
	this.value = jsonevent.getBoolean("value");
    }


    @Override
    public Observable<ChangeEvent<Checkbox>> getOnChange() {
	return this.onchange ;
    }


    @Override
    protected void fireOnChangeEvent(JSONObject jsonobject) {
	this.getOnChange().fire(new ChangeEvent<Checkbox>(this));
    }


}
