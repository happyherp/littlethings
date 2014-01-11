package de.carlos.socketfront.autogui;

import java.util.HashMap;
import java.util.Map;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.sample.HobbyProvider;
import de.carlos.socketfront.sample.HobbyProvider.Hobby;
import de.carlos.socketfront.widgets.Checkbox;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.NumberInput;
import de.carlos.socketfront.widgets.TextInput;
import de.carlos.util.FactoryImpl;

public class AutoGuiConfig {
    
    static private AutoGuiConfig instance = new AutoGuiConfig();
    
    static public AutoGuiConfig getInstance(){
	return instance;
    }

    Map<Class<?>, InputSourceWidgetFactory<?>> classmapping = new HashMap<>();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public AutoGuiConfig() {
	//Basic Types
	this.classmapping.put(String.class, new InputSourceWidgetFactoryImpl(TextInput.class));
	this.classmapping.put(Integer.class, new InputSourceWidgetFactoryImpl(NumberInput.class));
	this.classmapping.put(int.class, new InputSourceWidgetFactoryImpl(NumberInput.class));	
	this.classmapping.put(Boolean.class, new InputSourceWidgetFactoryImpl(Checkbox.class));
	this.classmapping.put(boolean.class, new InputSourceWidgetFactoryImpl(Checkbox.class));
	
	//Composite general Types
	this.classmapping.put(Enum.class, new EnumSelectFactory());
	this.classmapping.put(Object.class, new ObjectCreatorFactory());
	
	//Custom Objects
	this.classmapping.put(Hobby.class, new EntitySelectFactory(HobbyProvider.getInstance()));
	
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <U> InputSourceWidget<U> buildInput(GuiContext context, Class<U> paramclass) {

		
	InputSourceWidgetFactory factory = this.classmapping.get(paramclass);
	Class<?> currentclass = paramclass;
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
    
    /**
     * Factory that creates the a Widget able of displaying and changing/creating an instance of the needed class.
     * 
     * @author Carlos
     *
     * @param <T>
     */
    public static interface InputSourceWidgetFactory<T> {

	    public InputSourceWidget<T> create(GuiContext context, Class<T> parameter);

	}
    
    /**
     * Always creates an instance of the given Widget.
     * 
     * @author Carlos
     *
     * @param <T>
     */
    public static class InputSourceWidgetFactoryImpl<T> extends FactoryImpl<InputSourceWidget<T>> implements InputSourceWidgetFactory<T> {

	
	public InputSourceWidgetFactoryImpl(Class<? extends InputSourceWidget<T>> widgetclazz){
	    super(widgetclazz);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public InputSourceWidget<T> create(GuiContext context,
		Class<T> parameter) {
	    return (InputSourceWidget<T>) this.create().createJSWidget(context);
	}
	
    }


}
