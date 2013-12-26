package de.carlos.socketfront.autogui;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.util.OnAllValid;
import de.carlos.socketfront.util.OnAllValidHandler;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.Group;
import de.carlos.socketfront.widgets.InputSource;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.Text;

public class ObjectInputSourceFactory implements InputSourceFactory {

    @Override
    public InputSource create(GuiContext context, Class parameter) {

	Objectcreator creator = context.addWidget(new Objectcreator());
	creator.createContent(parameter);
	return creator;
    }

    public class Objectcreator extends Group implements InputSource {


	Class clazz;

	Object object = null;

	public void createContent(Class clazz) {
	    this.clazz = clazz;

	    if (clazz.getConstructors().length == 0) {
		throw new RuntimeException(clazz
			+ " does not have any constructors.");
	    }

	    this.getContext().addWidget(
		    new Text("Create new " + clazz.getName()), this);

	    for (Constructor constructor : clazz.getConstructors()) {

		final List<InputSource> parameterssources = new ArrayList<>();
		for (Class parameter : constructor.getParameterTypes()) {
		    InputSource parameterInput = AutoGuiConfig.getInstance()
			    .buildInput(this.getContext(), parameter);
		    this.add(parameterInput);
		    parameterssources.add(parameterInput);
		}
		
		final Button createButton = this.getContext().addWidget(new Button("create"), this);
		new OnAllValid(new OnAllValidHandler() {
		    
		    @Override
		    public void onInvalid() {
			createButton.setDisabled(true);
		    }
		    
		    @Override
		    public void onAllValid() {
			createButton.setDisabled(false);
		    }
		}, (InputSourceWidget[]) parameterssources.toArray());
		
	    }

	    /*
	     * This is code for accessing setters. for (Method method :
	     * clazz.getMethods()){ if (method.getName().startsWith("set")){
	     * String name = method.getName().substring(3);
	     * this.getContext().addWidget(new Text(name), this);
	     * 
	     * for (Class paramClass : method.getParameterTypes()){
	     * 
	     * } } }
	     */
	}

	@Override
	public Object getValue() {
	    return this.object;
	}

	@Override
	public void setValue(Object value) {
	    this.object = object;
	}

	@Override
	public boolean hasValidInput() {
	    return this.object != null;
	}

    }

}
