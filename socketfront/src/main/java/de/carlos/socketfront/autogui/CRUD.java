package de.carlos.socketfront.autogui;

import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.Group;
import de.carlos.socketfront.widgets.JSWidget;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.Window;
import de.carlos.socketfront.widgets.events.ChangeEvent;
import de.carlos.socketfront.widgets.events.ClickEvent;
import de.carlos.socketfront.widgets.table.RowSelectTable;

public class CRUD<T> implements Widget {

    Group group;

    private Provider<T> provider;

    RowSelectTable<T> table;

    private GuiContext context;

    public CRUD(Provider<T> provider) {
	this.provider = provider;
    }

    @Override
    public JSWidget getMainJSWidget() {
	return this.group;
    }

    @Override
    public Group createJSWidget(GuiContext context) {
	this.context = context;

	this.group = new Group().createJSWidget(context);

	table = new RowSelectTable<T>();
	table.setData(this.provider.getAll());
	table.setDrawInstructions(new EntityTableDrawInstuctions<T>(
		this.provider.getEntityClass()));
	table.createJSWidget(context);
	this.group.add(table.getMainJSWidget());

	Button deleteButton = new Button("Delete").createJSWidget(context);
	deleteButton.getOnClick().addObserver(
		new Observer<ClickEvent<Button>>() {
		    @Override
		    public void update(ClickEvent<Button> event) {
			CRUD.this.deleteSelected();
		    }
		});
	this.group.add(deleteButton);

	Button editButton = new Button("Edit").createJSWidget(context);
	editButton.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		CRUD.this.editSelected();
	    }
	});
	this.group.add(editButton);
	
	Button createButton = new Button("New").createJSWidget(context);
	createButton.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		CRUD.this.createNew();
	    }
	});
	this.group.add(createButton);

	return this.group;
    }

    protected void deleteSelected() {
	if (this.table.getValue() != null) {
	    this.provider.remove(this.table.getValue());
	    this.refresh();
	}
    }

    protected void editSelected() {
	if (this.table.getValue() != null){
	    
	    final Window editWindow = new Window();
	    editWindow.createJSWidget(context);
	    
	    final EntityEdit<T> editor = new EntityEdit<T>(this.table.getValue());
	    editor.createJSWidget(context);
	    editor.getOnChange().addObserver(new Observer<ChangeEvent<EntityEdit<T>>>() {
		@Override
		public void update(ChangeEvent<EntityEdit<T>> event){
		    CRUD.this.provider.save(editor.getValue());
		    editWindow.remove();
		    CRUD.this.refresh();
		}
	    });
	    
	    
	    editWindow.add(editor);
	    context.getMainPane().add(editWindow);
	}
    }
    
    protected void createNew() {
	    final Window createWindow = new Window();
	    createWindow.createJSWidget(context);	    
	    
	    final EntityEdit<T> editor = new EntityEdit<T>(this.provider.newEntity());
	    editor.createJSWidget(context);
	    editor.getOnChange().addObserver(new Observer<ChangeEvent<EntityEdit<T>>>() {
		@Override
		public void update(ChangeEvent<EntityEdit<T>> event){
		    CRUD.this.provider.create(editor.getValue());
		    createWindow.remove();
		    CRUD.this.refresh();
		}
	    });	    
	    
	    createWindow.add(editor);
	    context.getMainPane().add(createWindow);	
    }

    public void refresh() {
	this.table.setData(this.provider.getAll());
	this.table.refresh();
    }

}
