package de.carlos.socketfront.autogui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Grid;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.Widget;

public class EntityTable<T> implements Widget {
    
    private static Logger LOGGER = Logger.getLogger(EntityTable.class);

    private Grid grid;

    private GuiContext context;

    private List<T> data;

    private List<Method> getters = new ArrayList<>();

    /**
     * Needed to get type information, if the list is empty.
     */
    private Class<T> clazz;

    public EntityTable(List<T> data, Class<T> clazz) {
	this.data = data;
	this.clazz = clazz;
    }

    @Override
    public void constructJSObject() {

	for (Method m : clazz.getMethods()) {
	    if (m.getParameterTypes().length == 0
		    && (m.getName().startsWith("get") || m.getName()
			    .startsWith("is"))) {
		getters.add(m);
	    }
	}

	int rows = data.size() + 1 ;
	this.grid = context.addWidget(new Grid(getters.size(), rows));
	
	//Make header.
	int row = 0;
	int col = 0;
	for (Method m : this.getters){
	    String attrname;
	    if (m.getName().startsWith("is")){
		attrname = m.getName().substring(2);
	    }else{
		attrname = m.getName().substring(3);
	    }
	    Widget widget = context.addWidget(new Text(attrname));
		    this.grid.setCell(widget, col, row);
	    col++;
	}
	
	//Draw Rows.
	for (T entity : this.data){
	    row++;
	    col = 0;
	    for (Method getter: this.getters){
		String strResult;
		try {
		    Object result = getter.invoke(entity, new Object[]{});
		    if (result == null){
			strResult = "null";
		    }else{
			strResult = result.toString();
		    }
		} catch (IllegalAccessException | IllegalArgumentException
			| InvocationTargetException e) {

		    LOGGER.warn(e);
		    strResult = "Error while Calling";
		}
		this.grid.setCell(context.addWidget(new Text(strResult)), col, row);
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

}
