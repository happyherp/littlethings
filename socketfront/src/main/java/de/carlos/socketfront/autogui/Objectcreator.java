package de.carlos.socketfront.autogui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.carlos.observer.Observable;
import de.carlos.observer.Observer;
import de.carlos.socketfront.util.OnAllValid;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.Group;
import de.carlos.socketfront.widgets.InfoText;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.TextInput;
import de.carlos.socketfront.widgets.events.ChangeEvent;
import de.carlos.socketfront.widgets.events.ClickEvent;

public class Objectcreator<T> extends Group implements InputSourceWidget<T> {

    private static final Logger LOGGER = Logger
	    .getLogger(ObjectInputSourceFactory.class);

    InfoText exception = null;

    Class<T> clazz;

    T object = null;

    TextInput result;

    public void createContent(Class<T> clazz) {
	this.clazz = clazz;

	if (clazz.getConstructors().length == 0) {
	    throw new RuntimeException(clazz
		    + " does not have any constructors.");
	}

	this.getContext().addWidget(new Text("Create new " + clazz.getName()),
		this);

	for (final Constructor<?> constructor : clazz.getConstructors()) {

	    this.getContext().addWidget(new Text("Constructor"), this);

	    final List<InputSourceWidget<?>> parameterssources = new ArrayList<>();
	    for (Class<?> parameter : constructor.getParameterTypes()) {
		InputSourceWidget<?> parameterInput = AutoGuiConfig.getInstance()
			.buildInput(this.getContext(), parameter);
		this.add(parameterInput);
		parameterssources.add(parameterInput);
	    }

	    final Button createButton = this.getContext().addWidget(
		    new Button("create"), this);
	    InputSourceWidget<?>[] inputsource_array = parameterssources
		    .toArray(new InputSourceWidget[] {});
	    OnAllValid.enableButton(createButton, inputsource_array);

	    createButton.getOnClick().addObserver(
		    new Observer<ClickEvent<Button>>() {

			@Override
			public void update(ClickEvent<Button> event) {
			    callConstructor(constructor, parameterssources);
			}
		    });
	}

	this.getContext().addWidget(new Text("Result: "), this);
	result = this.getContext().addWidget(new TextInput(), this);
    }

    protected void callConstructor(Constructor<?> constructor,
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
	    this.getOnChange().fire(new ChangeEvent<Objectcreator<T>>(this));
	} catch (InstantiationException | IllegalAccessException
		| IllegalArgumentException e) {
	    LOGGER.warn(e);
	} catch (InvocationTargetException e) {
	    this.exception = this.getContext().addWidget(
		    new InfoText(e.getCause().getMessage()), this);
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

    @Override
    public Observable<ChangeEvent<Objectcreator<T>>> getOnChange() {
	return onchange;
    }

}