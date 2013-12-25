package de.carlos.socketfront.sample;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.autogui.AutoGuiConfig;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.Group;
import de.carlos.socketfront.widgets.InfoText;
import de.carlos.socketfront.widgets.InputSource;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.TextInput;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.events.ClickEvent;

public class CallGui {
    
    InfoText info = null;
    
    GuiContext context;

    Group group;

    List<InputSource> parameterInputs = new ArrayList<InputSource>();

    Button button;

    TextInput output;

    private Method targetmethod = null;

    public void create(GuiContext context, AutoGuiConfig autoguiconfig,
	    Class clazz, String methodname) {
	
	this.context = context;

	group = context.addWidget(new Group());

	for (Method m : clazz.getMethods()) {
	    if (m.getName().equals(methodname)) {
		targetmethod = m;
	    }
	}

	if (targetmethod == null) {
	    throw new RuntimeException("Method with name " + methodname
		    + " could not be found in " + clazz);
	}

	for (Class paramclass : targetmethod.getParameterTypes()) {
	    InputSource inputsource = autoguiconfig.buildInput(context,
		    paramclass);
	    this.parameterInputs.add(inputsource);
	    this.group.add(inputsource);
	}

	this.button = context.addWidget(new Button(methodname), this.group);
	this.button.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		onButtonClick();
	    }
	});

	context.addWidget(new Text("Result:"), this.group);
	this.output = context.addWidget(new TextInput(), this.group);

    }

    protected void onButtonClick() {

	boolean allready = true;
	List arguments = new ArrayList();
	for (InputSource input : parameterInputs) {
	    allready = allready && input.hasValidInput();
	    if (!allready){
		break;
	    }
	    arguments.add(input.getValue());
	}

	if (allready) {
	    this.output.setValue("");
	    if (this.info != null){
		this.info.remove();
	    }

	    try {
		Object result = this.targetmethod.invoke(null,
			arguments.toArray());
		this.output.setValue(result.toString());
	    } catch (IllegalAccessException | IllegalArgumentException e) {
		throw new RuntimeException(e);
	    } catch (InvocationTargetException e) {
		StringWriter writer = new StringWriter();
		e.getCause().printStackTrace(new PrintWriter(writer));
		this.info = this.context.addWidget(new InfoText(writer.toString()));
		this.group.addInfoText(this.info);
	    }
	} else {
	    this.context.getJsPipe()
		    .addCall("alert", "Not all fields filled");
	}

    }

    public Widget getGroup() {
	return this.group;
    }

}
