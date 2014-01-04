package de.carlos.socketfront.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import de.carlos.observer.Observer;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.events.ChangeEvent;

/**
 * Class that will enable a group of Widgets, when all sources have a valid
 * input and disable it, when one of them has an invalid input.
 * 
 * @author Carlos
 * 
 */
public class OnAllValid implements Observer<ChangeEvent<?>> {

    private Collection<InputSourceWidget> sources = new ArrayList<InputSourceWidget>();
    
    private OnAllValidHandler handler;

    public OnAllValid(OnAllValidHandler handler,  InputSourceWidget... sources) {
	this.handler = handler;
	this.sources.addAll(Arrays.asList(sources));
	
	for (InputSourceWidget source : sources){
	    source.getOnChange().addObserver(this);
	}
	checkValid();
    }


    private void checkValid() {
	boolean allready = true;
	for (InputSourceWidget source : sources){
	    allready = allready && source.hasValidInput();
	}
	if (allready){
	    this.handler.onAllValid();
	}else{
	    this.handler.onInvalid();
	}
    }

    


    @Override
    public void update(ChangeEvent<?> event) {
	checkValid();
    } 
    
    public static void enableButton(final Button button, InputSourceWidget... sources ){
	new OnAllValid(new OnAllValidHandler() {
	    
	    @Override
	    public void onInvalid() {
		button.setDisabled(true);
	    }
	    
	    @Override
	    public void onAllValid() {
		button.setDisabled(false);
	    }
	}, sources);
    }
    
}
