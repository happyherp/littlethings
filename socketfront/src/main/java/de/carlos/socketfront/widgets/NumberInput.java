package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class NumberInput extends FilterInput<Integer> {

    private boolean allowNull;

    private Observable<ChangeEvent<NumberInput>> onchange = new Observable<ChangeEvent<NumberInput>>();

    public NumberInput() {
	this(false);
    }

    public NumberInput(boolean allowNull) {
	super();
	this.allowNull = allowNull;
    }

    @Override
    public Integer getValue() {

	if (allowNull && this.getStringValue().isEmpty()) {
	    return null;
	}

	return Integer.parseInt(this.getStringValue());
    }

    @Override
    public boolean hasValidInput() {
	return (this.allowNull && this.getStringValue().isEmpty())
		|| this.getStringValue().matches("^ *-?\\d+ *$");
    }

    @Override
    public void setValue(Integer value) {
	if (value != null) {
	    this.setStringValue(value + "");
	} else {
	    this.setStringValue("");
	}
    }

    @Override
    public Observable<ChangeEvent<NumberInput>> getOnChange() {
	return this.onchange;
    }

    @Override
    protected void fireOnChangeEvent(JSONObject jsonobject) {
	this.getOnChange().fire(
		new ChangeEvent<NumberInput>(this, this.context));
    }

}
