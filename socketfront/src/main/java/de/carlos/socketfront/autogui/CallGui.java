package de.carlos.socketfront.autogui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.Group;
import de.carlos.socketfront.widgets.InfoText;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.JSWidget;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.TextInput;
import de.carlos.socketfront.widgets.events.ClickEvent;

public class CallGui {
    
    InfoText info = null;
    
    GuiContext context;

    Group group;

    List<InputSourceWidget<?>> parameterInputs = new ArrayList<InputSourceWidget<?>>();

    Button button;

    TextInput output;

    private Method targetmethod = null;
    
    private Object targetinstance = null;

    public void createStatic(GuiContext context, AutoGuiConfig autoguiconfig,
	    Class<?> clazz, String methodname) {


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

	for (Class<?> paramclass : targetmethod.getParameterTypes()) {   
	    InputSourceWidget<?> inputsource = autoguiconfig.buildInput(context,
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
    
    public void createMember(GuiContext context, AutoGuiConfig autoguiconfig,
	    Object object, String methodname) {
	
	this.targetinstance = object;
	createStatic(context, autoguiconfig, object.getClass(), methodname);
	
    }
    

    protected void onButtonClick() {

	boolean allready = true;
	List<Object> arguments = new ArrayList<Object>();
	for (InputSourceWidget<?> input : parameterInputs) {
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
		this.info = this.context.addWidget(new InfoText(writer.toString()));
		this.group.addInfoText(this.info);
	    }
	} else {
	    this.context.getJsPipe()
		    .addCall("alert", "Not all fields filled");
	}

    }

    public JSWidget getGroup() {
	return this.group;
    }

}
