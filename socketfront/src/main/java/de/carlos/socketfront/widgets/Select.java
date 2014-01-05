package de.carlos.socketfront.widgets;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class Select<T> extends InputSourceWidgetBase<T> {

    List<Option> options = new ArrayList<Option>();

    Option selectedOption = null;
    
    int idcount = 0;

    @Override
    public void constructJSObject(GuiContext context) {
	this.jsPipe.addCall("new Select", this.getId());
    }

    public void addOption(String label, T obj) {
	Option option = new Option();
	option.object = obj;
	option.id = idcount;
	idcount++;
	this.options.add(option);
	this.callThisJS("addOption", option.id, label);

	if (selectedOption == null) {
	    selectedOption = option;
	}

    }

    public T getValue() {
	if (selectedOption == null) {
	    return null;
	} else {
	    return selectedOption.object;
	}
    }

    public void setValue(T obj) {

	Option newSelectedOption = null;
	for (Option option : options) {
	    if (option.object.equals(obj)) {
		newSelectedOption = option;
	    }
	}
	if (newSelectedOption == null) {
	    throw new RuntimeException(
		    "That object is not part of the options: " + obj);
	} else {
	    this.callThisJS("setSelected", newSelectedOption.id);
	    this.selectedOption = newSelectedOption;
	}

    }

    private class Option {
	T object;
	int id;

    }

    @Override
    public boolean hasValidInput() {
	return selectedOption != null;
    }


    @Override
    protected void reactToChange(JSONObject jsonevent) {
	int id = jsonevent.getInt("optionid");
	for (Option option : this.options) {
	    if (option.id == id) {
		this.selectedOption = option;
	    }
	}
    }


    @Override
    protected void fireOnChangeEvent(JSONObject jsonobject) {
	this.getOnChange().fire(new ChangeEvent<Select<T>>(this));
    }

    @Override
    public Observable<ChangeEvent<Select<T>>> getOnChange() {
	return (Observable<ChangeEvent<Select<T>>>) this.onchange;
    }


}
