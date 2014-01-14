package de.carlos.socketfront.autogui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
import de.carlos.socketfront.widgets.events.ClickEvent;

public class CallGui extends GroupComposition {

    InfoText info = null;

    List<InputSourceWidget<?>> parameterInputs = new ArrayList<InputSourceWidget<?>>();

    Button button;

    TextInput output;

    private Method targetmethod = null;

    private Object targetinstance = null;

    public CallGui(Object object, String methodname) {
	this(object, object.getClass(), methodname);
    }

    public CallGui(Class<?> clazz, String methodname) {
	this(null, clazz, methodname);
    }

    private CallGui(Object object, Class<?> clazz, String methodname) {
	for (Method m : clazz.getMethods()) {
	    if (m.getName().equals(methodname)) {
		targetmethod = m;
	    }
	}

	if (targetmethod == null) {
	    throw new RuntimeException("Method with name " + methodname
		    + " could not be found in " + clazz);
	}

	this.targetinstance = object;
    }

    @Override
    public JSWidget createJSWidget(GuiContext context) {
	super.createJSWidget(context);

	for (Class<?> paramclass : targetmethod.getParameterTypes()) {
	    InputSourceWidget<?> inputsource = AutoGuiConfig.getInstance()
		    .buildInput(context, paramclass);
	    this.parameterInputs.add(inputsource);
	    this.group.add(inputsource);
	}

	this.button = this.group.add(new Button(targetmethod.getName())
		.createJSWidget(context));
	this.button.getOnClick().addObserver(
		new Observer<ClickEvent<Button>>() {
		    @Override
		    public void update(ClickEvent<Button> event) {
			onButtonClick();
		    }
		});

	OnAllValid.enableButton(this.button,
		this.parameterInputs.toArray(new InputSourceWidget[] {}));

	this.group.add((new Text("Result:").createJSWidget(context)));
	this.output = new TextInput();
	this.group.add(this.output.createJSWidget(context));

	return this.group;
    }

    protected void onButtonClick() {

	List<Object> arguments = new ArrayList<Object>();
	for (InputSourceWidget<?> input : parameterInputs) {
	    arguments.add(input.getValue());
	}

	this.output.setValue("");
	if (this.info != null) {
	    this.info.remove();
	    this.info = null;
	}

	try {
	    Object result = this.targetmethod.invoke(this.targetinstance,
		    arguments.toArray());
	    this.output.setValue(result.toString());
	} catch (IllegalAccessException | IllegalArgumentException e) {
	    throw new RuntimeException(e);
	} catch (InvocationTargetException e) {
	    StringWriter writer = new StringWriter();
	    e.getCause().printStackTrace(new PrintWriter(writer));
	    this.info = new InfoText(writer.toString()).createJSWidget(context);
	    this.group.addInfo(this.info);
	}

    }

}
