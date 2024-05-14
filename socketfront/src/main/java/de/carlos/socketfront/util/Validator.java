package de.carlos.socketfront.util;

import java.util.ArrayList;
import java.util.List;

import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.Widget;

public class Validator<T> {
    
    private InputSourceWidget<T> widget;
    
    List<Check<T>> checks = new ArrayList<>();

    public Validator(InputSourceWidget<T> widget){
	this.widget = widget;
    }
    
    public boolean isValid(T val){
		
	boolean allvalid = true;
	
	for (Check<T> check : this.checks){
	    allvalid = allvalid && check.isValid(val);
	    
	    Widget info = check.createInfoWidget(val);
	    if (info != null){
		//this.widget; 
	    }
	    
	}
	
	return allvalid;
    }
    
    public static interface Check<T>{
	
	boolean isValid(T obj);
	
	Widget createInfoWidget(T obj);
	
    }

}
