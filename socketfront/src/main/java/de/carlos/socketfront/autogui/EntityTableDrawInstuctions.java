package de.carlos.socketfront.autogui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.autogui.EntityUtil.EntityField;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.table.RowTableDrawInstructions;

public class EntityTableDrawInstuctions<T> implements RowTableDrawInstructions<T> {

    private static Logger LOGGER = Logger.getLogger(EntityTableDrawInstuctions.class);

    List<String> methodexclusions = new ArrayList<>();

    List<EntityField> fields;

    private GuiContext context;

    public EntityTableDrawInstuctions(GuiContext context, Class<T> clazz) {
	this.context = context;
	methodexclusions.add("Class");
	this.fields = EntityUtil.findFields(clazz);

	// Remove accessors without getters and exclusions.
	for (EntityField field : new ArrayList<>(fields)) {
	    if (field.getter == null || methodexclusions.contains(field.name)) {
		fields.remove(field);
	    }
	}
    }

    public void addMethodExclusion(String methodname) {
	this.methodexclusions.add(methodname);
    }

    @Override
    public List<String> createHeader() {
	// Make header.
	List<String> headers = new ArrayList<>();
	for (EntityField field : fields) {
	    headers.add(field.name);
	}
	return headers;
    }

    @Override
    public  List<Widget> createRow(T entity) {

	List<Widget> widgets = new ArrayList<>();
	for (EntityField field : fields) {
	    String strResult;
	    try {
		Object result = field.getter.invoke(entity, new Object[] {});
		if (result == null) {
		    strResult = "null";
		} else {
		    strResult = result.toString();
		}
	    } catch (IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException e) {

		LOGGER.warn(e);
		strResult = "Error while Calling";
	    }
	    widgets.add(this.context.addWidget(new Text(strResult)));
	}
	return widgets;
    }
    
}
