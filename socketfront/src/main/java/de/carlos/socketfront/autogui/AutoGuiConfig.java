package de.carlos.socketfront.autogui;

import java.util.HashMap;
import java.util.Map;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Checkbox;
import de.carlos.socketfront.widgets.InputSource;
import de.carlos.socketfront.widgets.NumberInput;
import de.carlos.socketfront.widgets.TextInput;
import de.carlos.util.Factory;
import de.carlos.util.FactoryImpl;

public class AutoGuiConfig {

    Map<Class, Factory<InputSource>> classmapping = new HashMap<Class, Factory<InputSource>>();

    public AutoGuiConfig() {
	this.classmapping.put(String.class, new FactoryImpl<InputSource>(TextInput.class));
	this.classmapping.put(Integer.class, new FactoryImpl<InputSource>(NumberInput.class));
	this.classmapping.put(Boolean.class, new FactoryImpl<InputSource>(Checkbox.class));
	
	//TODO: Handle primitive types.
    }


    public InputSource buildInput(GuiContext context, Class paramclass) {

	Factory<InputSource> factory = this.classmapping.get(paramclass);
	if (factory != null) {
	    return context.addWidget(factory.create());
	} else {
	    throw new RuntimeException("Cant handle type: " + paramclass);
	}

    }

}
