package de.carlos.socketfront.autogui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.carlos.observer.Observable;
import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.util.OnAllValid;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.Group;
import de.carlos.socketfront.widgets.GroupComposition;
import de.carlos.socketfront.widgets.InfoText;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.JSWidget;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.TextInput;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.events.ChangeEvent;
import de.carlos.socketfront.widgets.events.ClickEvent;

/**
 * Creates a GUI that creates an Object by calling its constructor.
 * 
 * @author Carlos
 *
 * @param <T>
 */
public class Objectcreator<T> extends GroupComposition implements InputSourceWidget<T> {

    private static final Logger LOGGER = Logger
	    .getLogger(ObjectCreatorFactory.class);

    InfoText exception = null;

    Class<T> clazz;

    T object = null;

    TextInput result;
    
    protected Observable<ChangeEvent<Objectcreator<T>>> onchange = new Observable<>();

    
    public Objectcreator(Class<T> clazz){
	this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    protected void callConstructor(GuiContext guiContext, Constructor<?> constructor,
	    List<InputSourceWidget<?>> parameters) {
	List<Object> args = new ArrayList<>();

	for (InputSourceWidget<?> inputsource : parameters) {
	    args.add(inputsource.getValue());
	}

	if (this.exception != null) {
	    this.exception.remove();
	    this.exception = null;
	}

	try {
	    this.setValue((T) constructor.newInstance(args.toArray()));
	    this.getOnChange().fire(new ChangeEvent<Objectcreator<T>>(this, guiContext));
	} catch (InstantiationException | IllegalAccessException
		| IllegalArgumentException e) {
	    LOGGER.warn(e);
	} catch (InvocationTargetException e) {
	    this.exception =  this.group.add(new InfoText(e.getCause().getMessage()).createJSWidget(guiContext));
	}
    }

    @Override
    public T getValue() {
	return this.object;
    }

    @Override
    public void setValue(T value) {
	this.object = value;
	if (value == null) {
	    this.result.setValue("null");
	} else {
	    this.result.setValue(value.toString());
	}
    }

    @Override
    public boolean hasValidInput() {
	return this.object != null;
    }


    @Override
    public Observable<ChangeEvent<Objectcreator<T>>> getOnChange() {
	return onchange;
    }


    @Override
    public Group createJSWidget(GuiContext context) {
	super.createJSWidget(context);

	if (clazz.getConstructors().length == 0) {
	    throw new RuntimeException(clazz
		    + " does not have any constructors.");
	}
	
	this.group = new Group().createJSWidget(context);

	this.group.add(new Text("Create new " + clazz.getName()).createJSWidget(context));;
	

	for (final Constructor<?> constructor : clazz.getConstructors()) {

	    this.group.add(new Text("Constructor").createJSWidget(context));

	    final List<InputSourceWidget<?>> parameterssources = new ArrayList<>();
	    for (Class<?> parameter : constructor.getParameterTypes()) {
		InputSourceWidget<?> parameterInput = AutoGuiConfig.getInstance()
			.buildInput(context, parameter);
		this.group.add(parameterInput);
		parameterssources.add(parameterInput);
	    }

	    final Button createButton = this.group.add( new Button("create").createJSWidget(context));
	    InputSourceWidget<?>[] inputsource_array = parameterssources
		    .toArray(new InputSourceWidget[] {});
	    OnAllValid.enableButton(createButton, inputsource_array);

	    createButton.getOnClick().addObserver(
		    new Observer<ClickEvent<Button>>() {

			@Override
			public void update(ClickEvent<Button> event) {
			    callConstructor(event.getContext(), constructor, parameterssources);
			}
		    });
	}

	this.group.add(new Text("Result: ").createJSWidget(context));
	result = new TextInput();
	this.group.add(result.createJSWidget(context));
	return group;
    }
}