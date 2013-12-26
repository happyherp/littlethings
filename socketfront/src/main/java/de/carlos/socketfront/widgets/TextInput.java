package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class TextInput extends Input<String> implements InputSource<String> {

    @Override
    public String getValue() {
	return this.getStringValue();
    }

    @Override
    public void setValue(String value) {
	this.setStringValue(value);
    }

    @Override
    public boolean hasValidInput() {
	return true;
    }


    @Override
    protected void fireOnChangeEvent(JSONObject jsonobject) {
	this.getOnChange().fire(new ChangeEvent<TextInput>(this));
    }

    @Override
    public Observable<ChangeEvent<TextInput>> getOnChange() {
	return (Observable<ChangeEvent<TextInput>>) onchange;
    }

}
