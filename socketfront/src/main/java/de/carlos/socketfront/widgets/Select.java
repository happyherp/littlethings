package de.carlos.socketfront.widgets;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Select<T> extends WidgetBase {

    List<Option> options = new ArrayList<Option>();

    Option selectedOption = null;

    int idcount = 0;
    
    @Override
    public void constructJSObject() {
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

    public T getSelected() {
	if (selectedOption == null) {
	    return null;
	} else {
	    return selectedOption.object;
	}
    }

    public void setSelected(T obj) {

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

    public void receiveEvent(JSONObject jsonevent) {
	if (jsonevent.getString("type").equals("change")) {
	    int id = jsonevent.getInt("optionid");
	    for (Option option : this.options){
		if (option.id == id){
		    this.selectedOption = option;
		}
	    }
	} else {
	    throw new RuntimeException("Unhandled event type "
		    + jsonevent.getString("type"));
	}
    }

    private class Option {
	T object;
	int id;

    }



}
