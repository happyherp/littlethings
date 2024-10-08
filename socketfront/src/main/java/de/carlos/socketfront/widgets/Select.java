package de.carlos.socketfront.widgets;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.events.ChangeEvent;
import de.carlos.util.Utils;

public class Select<T> extends JSInputSourceWidget<T> {

    List<Option> options = new ArrayList<Option>();

    Option selectedOption = null;
    
    int idcount = 0;

    @Override
    public Select<T> createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	this.jsPipe.addCall("new Select", this.getId());
	return this;
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
	    if (Utils.equals(obj, option.object)) {
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
	this.getOnChange().fire(new ChangeEvent<Select<T>>(this, this.context));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<ChangeEvent<Select<T>>> getOnChange() {
	return (Observable<ChangeEvent<Select<T>>>) this.onchange;
    }




}
