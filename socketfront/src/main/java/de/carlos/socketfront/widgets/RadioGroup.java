package de.carlos.socketfront.widgets;

import java.util.ArrayList;
import java.util.List;

import de.carlos.observer.Observable;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class RadioGroup<T> implements InputSource<T>  {
    
    Observable<ChangeEvent<Radiobutton<T>>> onchange = new Observable<>();
    
    String groupname;
    
    List<Radiobutton<T>> radios = new ArrayList<>();
    
    Radiobutton<T> activeRadio = null;
    
    GuiContext context;

    public RadioGroup(GuiContext context) {
	this.groupname = context.newRadioGroupName();
	this.context = context;
    }
    
    
    public Radiobutton<T> newRadio(T value){
	Radiobutton<T> radio = new Radiobutton<T>(this, value);
	this.context.addWidget(radio);
	radios.add(radio);
	return radio;
    }
    
    protected void onButtonChange(Radiobutton<T> button, Boolean status){
	if (status){
	    activeRadio = button;
	}
	this.onchange.fire(new ChangeEvent<Radiobutton<T>>(button));
    }



    @Override
    public T getValue() {
	if (activeRadio == null){
	    return null;
	}
	return activeRadio.getObject();
    }


    @Override
    public void setValue(T value) {
	
	for (Radiobutton<T> button : this.radios){
	    if (button.getObject().equals(value)){
		if (activeRadio != null){
		    activeRadio.setValue(false);
		}
		button.setValue(true);
		
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
    public Observable<ChangeEvent<Radiobutton<T>>> getOnChange() {
	return onchange;
    }


    public String getName() {
	return this.groupname;
    }


   

}
