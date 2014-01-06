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
import de.carlos.socketfront.widgets.InfoText;
import de.carlos.socketfront.widgets.InputSource;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.TextInput;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.events.ChangeEvent;
import de.carlos.socketfront.widgets.events.ClickEvent;
import de.carlos.socketfront.widgets.table.WidgetComposition;

public class Objectcreator<T> implements InputSource<T>, WidgetComposition {

    private static final Logger LOGGER = Logger
	    .getLogger(ObjectInputSourceFactory.class);

    InfoText exception = null;

    Class<T> clazz;

    T object = null;

    TextInput result;
    
    public Objectcreator(Class<T> clazz){
	this.clazz = clazz;
    }

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
	    this.exception = guiContext.addWidget(
		    new InfoText(e.getCause().getMessage()), this.group);
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

    protected Observable<ChangeEvent<Objectcreator<T>>> onchange = new Observable<>();

    private Group group;

    @Override
    public Observable<ChangeEvent<Objectcreator<T>>> getOnChange() {
	return onchange;
    }

    @Override
    public Widget getMainWidget() {
	return this.group;
    }

    @Override
    public void create(GuiContext context) {

	if (clazz.getConstructors().length == 0) {
	    throw new RuntimeException(clazz
		    + " does not have any constructors.");
	}
	
	this.group = new Group();
	context.addWidget(this.group);

	this.group.add(context.addWidget(new Text("Create new " + clazz.getName())));;
	

	for (final Constructor<?> constructor : clazz.getConstructors()) {

	    this.group.add(context.addWidget(new Text("Constructor")));

	    final List<InputSourceWidget<?>> parameterssources = new ArrayList<>();
	    for (Class<?> parameter : constructor.getParameterTypes()) {
		InputSourceWidget<?> parameterInput = AutoGuiConfig.getInstance()
			.buildInput(context, parameter);
		this.group.add(context.addWidget(parameterInput));
		parameterssources.add(parameterInput);
	    }

	    final Button createButton = context.addWidget(
		    new Button("create"), this.group);
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

	context.addWidget(new Text("Result: "), this.group);
	result = context.addWidget(new TextInput(), this.group);
	
    }

}