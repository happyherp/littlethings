package de.carlos.socketfront.widgets;

import java.util.ArrayList;
import java.util.List;

import de.carlos.observer.Observable;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class RadioGroup<T> implements InputSource<T>  {
    
    Observable<ChangeEvent<RadioButton<T>>> onchange = new Observable<>();
    
    String groupname;
    
    List<RadioButton<T>> radios = new ArrayList<>();
    
    RadioButton<T> activeRadio = null;
    
    GuiContext context;

    public RadioGroup(GuiContext context) {
	this.groupname = context.newRadioGroupName();
	this.context = context;
    }
    
    
    public RadioButton<T> newRadio(T value){
	RadioButton<T> radio = new RadioButton<T>(this, value);
	this.context.addWidget(radio);
	radios.add(radio);
	return radio;
    }
    
    protected void onButtonChange(RadioButton<T> button, Boolean status){
	
	RadioButton<T> oldRadio = activeRadio;
	
	if (status){
	    activeRadio = button;
	}
	
	if (oldRadio != null && oldRadio != activeRadio){
	    oldRadio.getOnChange().fire(new ChangeEvent<RadioButton<T>>(oldRadio, this.context));
	}
	activeRadio.getOnChange().fire(new ChangeEvent<RadioButton<T>>(activeRadio, this.context));
	
	
	this.onchange.fire(new ChangeEvent<RadioButton<T>>(button, this.context));
    }



    @Override
    public T getValue() {
	if (activeRadio == null){
	    return null;
	}
	return activeRadio.getObject();
    }
    
    /**
     * Checks if the given value is associated with a Radiobutton of this group.
     * If this returns true, setValue can safely be called.
     * 
     * @param value
     * @return
     */
    public boolean canBeUsedAsValue(T value){
	for (RadioButton<T> button : this.radios){
	    if (button.getObject().equals(value)){		
		return true;
	    }
	}	
	return false;
    }


    @Override
    public void setValue(T value) {
	
	for (RadioButton<T> button : this.radios){
	    if (button.getObject().equals(value)){
		activeRadio = button;
		return;
	    }
	}
	
	throw new RuntimeException("Value could not be found: " + value);

    }


    @Override
    public boolean hasValidInput() {
	return this.activeRadio != null;
    }


    @Override
    public Observable<ChangeEvent<RadioButton<T>>> getOnChange() {
	return onchange;
    }


    public String getName() {
	return this.groupname;
    }


   

}
