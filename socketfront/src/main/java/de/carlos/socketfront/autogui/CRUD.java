package de.carlos.socketfront.autogui;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Group;
import de.carlos.socketfront.widgets.JSWidget;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.table.RowSelectTable;

public class CRUD<T> implements Widget {

    Group group;
    
    private Provider<T> provider;
    
    public CRUD(Provider<T> provider){
	this.provider = provider;
    }
    
    @Override
    public JSWidget getMainJSWidget() {
	return this.group;
    }

    @Override
    public Group createJSWidget(GuiContext context) {
	
	this.group = new Group().createJSWidget(context);
	
	RowSelectTable<T> table = new RowSelectTable<T>();
	table.setData(this.provider.getAll());
	table.setDrawInstructions(new EntityTableDrawInstuctions<T>(this.provider.getEntityClass()));
	table.createJSWidget(context);
	this.group.add(table.getMainJSWidget());
	
	
	return this.group;
    }

}
