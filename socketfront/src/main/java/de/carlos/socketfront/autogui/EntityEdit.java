package de.carlos.socketfront.autogui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.carlos.observer.Observable;
import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.autogui.EntityUtil.EntityField;
import de.carlos.socketfront.util.OnAllValid;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.Group;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.events.ChangeEvent;
import de.carlos.socketfront.widgets.events.ClickEvent;

public class EntityEdit<T> implements InputSourceWidget<T> {
    
    private static final Logger LOGGER = Logger.getLogger(EntityEdit.class);

    Group group;

    T object;

    GuiContext context;

    List<EntityField> fields;

    List<InputSourceWidget> inputs = new ArrayList<InputSourceWidget>();;

    List<String> excludedMethods = new ArrayList<String>();
    
    Observable<ChangeEvent<EntityEdit<T>>> onchange = new Observable<ChangeEvent<EntityEdit<T>>>();

    public EntityEdit(T object) {
	this.object = object;
	this.excludedMethods.add("Class");
    }

    @Override
    public void constructJSObject(GuiContext context) {

	this.group = this.getContext().addWidget(new Group());

	fields = EntityUtil.findFields(object.getClass());

	for (EntityField field : fields) {
	    if (!excludedMethods.contains(field.name)) {
		this.getContext().addWidget(new Text(field.name), this.group);
		InputSourceWidget inputsource = AutoGuiConfig.getInstance()
			.buildInput(getContext(), field.type);
		
		if (field.getter != null){
		    try {
			inputsource.setValue(field.getter.invoke(this.object, new Object[]{}));
		    } catch (IllegalAccessException | IllegalArgumentException
			    | InvocationTargetException e) {
			LOGGER.warn("Could not call getter "+field.getter.getName(), e);
		    }
		}
		
		this.group.add(inputsource);
		this.inputs.add(inputsource);
	    }
	}

	Button savebutton = this.getContext().addWidget(new Button("save"),
		this.group);
	OnAllValid.enableButton(savebutton,
		this.inputs.toArray(new InputSourceWidget[] {}));
	savebutton.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {

	    @Override
	    public void update(ClickEvent<Button> event) {
		saveToObject();
	    }
	});

    }

    protected void saveToObject() {
	int i = 0;
	for (InputSourceWidget inputsource : this.inputs){
	    EntityField field = this.fields.get(i);
	    
	    if (field.setter != null){
		try {
		    field.setter.invoke(this.object, new Object[]{inputsource.getValue()});
		} catch (IllegalAccessException | IllegalArgumentException
			| InvocationTargetException e) {
		    LOGGER.warn("Could not invoke setter "+field.setter.getName(),e);
		}
	    }	    
	    
	    i++;
	}
	
	this.getOnChange().fire(new ChangeEvent(this));
	
    }

    @Override
    public void setContext(GuiContext context) {
	this.context = context;
    }

    @Override
    public GuiContext getContext() {
	return this.context;
    }

    @Override
    public void receiveEvent(JSONObject event) {
    }

    @Override
    public T getValue() {
	return this.object;
    }

    @Override
    public void setValue(T value) {
	this.object = value;
    }

    @Override
    public boolean hasValidInput() {
	return true;
    }

    @Override
    public Observable<ChangeEvent<EntityEdit<T>>> getOnChange() {
	return this.onchange;
    }

}
