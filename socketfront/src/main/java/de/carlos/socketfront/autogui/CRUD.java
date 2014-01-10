package de.carlos.socketfront.autogui;

import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.Group;
import de.carlos.socketfront.widgets.JSWidget;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.events.ClickEvent;
import de.carlos.socketfront.widgets.table.RowSelectTable;

public class CRUD<T> implements Widget {

    Group group;
    
    private Provider<T> provider;
    
    RowSelectTable<T> table;
    
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
	
	table = new RowSelectTable<T>();
	table.setData(this.provider.getAll());
	table.setDrawInstructions(new EntityTableDrawInstuctions<T>(this.provider.getEntityClass()));
	table.createJSWidget(context);
	this.group.add(table.getMainJSWidget());
	
	
	Button deleteButton = new Button("Delete").createJSWidget(context);
	deleteButton.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    
	    @Override
	    public void update(ClickEvent<Button> event) {
		CRUD.this.deleteSelected();
	    }
	});
	this.group.add(deleteButton);
	
	
	return this.group;
    }

    protected void deleteSelected() {
	this.provider.remove(this.table.getValue());
	this.refresh();
    }

    public void refresh() {
	this.table.setData(this.provider.getAll());
	this.table.refresh();
    }

}
