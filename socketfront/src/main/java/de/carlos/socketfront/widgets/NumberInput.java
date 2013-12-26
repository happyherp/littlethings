package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class NumberInput extends FilterInput<Integer> {

    private Observable<ChangeEvent<NumberInput>> onchange = new Observable<ChangeEvent<NumberInput>>();

    @Override
    public Integer getValue() {
	return Integer.parseInt(this.getStringValue());
    }

    @Override
    public boolean hasValidInput() {
	return this.getStringValue().matches("^ *-?\\d+ *$");
    }

    @Override
    public void setValue(Integer value) {
	this.setStringValue(value + "");
    }

    @Override
    public Observable<ChangeEvent<NumberInput>> getOnChange() {
	return this.onchange;
    }

    @Override
    protected void fireOnChangeEvent(JSONObject jsonobject) {
	this.getOnChange().fire(new ChangeEvent<NumberInput>(this));
    }

}
