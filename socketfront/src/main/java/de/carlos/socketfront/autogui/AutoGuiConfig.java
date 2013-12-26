package de.carlos.socketfront.autogui;

import java.util.HashMap;
import java.util.Map;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Checkbox;
import de.carlos.socketfront.widgets.InputSource;
import de.carlos.socketfront.widgets.NumberInput;
import de.carlos.socketfront.widgets.TextInput;

public class AutoGuiConfig {
    
    static private AutoGuiConfig instance = new AutoGuiConfig();
    
    static public AutoGuiConfig getInstance(){
	return instance;
    }

    Map<Class<?>, InputSourceFactory> classmapping = new HashMap<>();

    public AutoGuiConfig() {
	this.classmapping.put(String.class, new InputSourceFactoryImpl(TextInput.class));
	this.classmapping.put(Integer.class, new InputSourceFactoryImpl(NumberInput.class));
	this.classmapping.put(int.class, new InputSourceFactoryImpl(NumberInput.class));	
	this.classmapping.put(Boolean.class, new InputSourceFactoryImpl(Checkbox.class));
	this.classmapping.put(boolean.class, new InputSourceFactoryImpl(Checkbox.class));
	this.classmapping.put(Enum.class, new EnumInputSourceFactory());
	this.classmapping.put(Object.class, new ObjectInputSourceFactory());
    }

    public InputSource<?> buildInput(GuiContext context, Class<?> paramclass) {

		
	InputSourceFactory factory = this.classmapping.get(paramclass);
	Class currentclass = paramclass;
	while (factory == null && currentclass.getSuperclass() != null){
	    currentclass = currentclass.getSuperclass();
	    factory = this.classmapping.get(currentclass);
	}
	
	if (factory != null) {
	    return factory.create(context, paramclass);
	} else {
	    throw new RuntimeException("Cant handle type: " + paramclass);
	}
    }      

}
