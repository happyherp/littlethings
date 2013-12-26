package de.carlos.socketfront.autogui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.autogui.EntityUtil.Accessor;
import de.carlos.socketfront.autogui.EntityUtil.EntityField;
import de.carlos.socketfront.widgets.Grid;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.Widget;

public class EntityTable<T> implements Widget {

    private static Logger LOGGER = Logger.getLogger(EntityTable.class);

    private Grid grid;

    private GuiContext context;

    private List<T> data;

    List<String> methodexclusions = new ArrayList<>();

    /**
     * Needed to get type information, if the list is empty.
     */
    private Class<T> clazz;
    
    List<EntityField> fields;

    public EntityTable(List<T> data, Class<T> clazz) {
	this.data = data;
	this.clazz = clazz;
	methodexclusions.add("Class");
    }

    public void addMethodExclusion(String methodname) {
	this.methodexclusions.add(methodname);
    }

    @Override
    public void constructJSObject() {

	fields = EntityUtil.findFields(clazz);

	// Remove accessors without getters and axclusions.
	for (EntityField field: new ArrayList<>(fields)){
	    if (field.getter == null || methodexclusions.contains(field.name)){
		fields.remove(field);
	    }
	}

	int rows = data.size() + 1;
	this.grid = context.addWidget(new Grid(fields.size(), rows));

	// Make header.
	int col = 0;
	for (EntityField field:fields) {
	    Widget widget = context.addWidget(new Text(field.name));
	    this.grid.setCell(widget, col, 0);
	    col++;
	}

	drawRows();

    }

    protected void drawRows() {
	
	
	int col;
	// Draw Rows.
	int row = 0;
	for (T entity : this.data) {
	    row++;
	    col = 0;
	    for (EntityField field:fields) {
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
		this.grid.setCell(context.addWidget(new Text(strResult)), col,
			row);
		col++;
	    }
	}
    }

    @Override
    public String getId() {
	return this.grid.getId();
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

    public void refresh() {
	this.drawRows();
    }

}
