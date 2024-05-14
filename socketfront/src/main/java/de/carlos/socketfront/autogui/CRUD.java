package de.carlos.socketfront.autogui;

import java.util.List;

import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.Group;
import de.carlos.socketfront.widgets.GroupComposition;
import de.carlos.socketfront.widgets.JSWidget;
import de.carlos.socketfront.widgets.TablePagination;
import de.carlos.socketfront.widgets.TablePagination.Range;
import de.carlos.socketfront.widgets.VGroup;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.Window;
import de.carlos.socketfront.widgets.events.ChangeEvent;
import de.carlos.socketfront.widgets.events.ClickEvent;
import de.carlos.socketfront.widgets.table.RowSelectTable;

public class CRUD<T> extends GroupComposition {

    private Provider<T> provider;

    RowSelectTable<T> table;

    private TablePagination pagination;

    public CRUD(Provider<T> provider) {
	super(true);
	this.provider = provider;
    }

    @Override
    public Group createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	
	Group tablegroup = new Group();
	tablegroup.createJSWidget(context);
	

	table = new RowSelectTable<T>();
	table.setDrawInstructions(new EntityTableDrawInstuctions<T>(
		this.provider.getEntityClass()));
	table.createJSWidget(context);
	tablegroup.add(table.getMainJSWidget());
	
	this.pagination = new TablePagination();
	this.pagination.setPagesize(3);
	this.pagination.getOnChange().addObserver(new Observer<ChangeEvent<TablePagination>>() {

	    @Override
	    public void update(ChangeEvent<TablePagination> event) {
		CRUD.this.refresh();
	    };
	});
	tablegroup.add(this.pagination.createJSWidget(context));
	
	this.group.add(tablegroup);
	
	
	Group buttongrGroup = new Group();
	this.group.add(buttongrGroup.createJSWidget(context));

	Button deleteButton = new Button("Delete").createJSWidget(context);
	deleteButton.getOnClick().addObserver(
		new Observer<ClickEvent<Button>>() {
		    @Override
		    public void update(ClickEvent<Button> event) {
			CRUD.this.deleteSelected();
		    }
		});
	buttongrGroup.add(deleteButton);

	Button editButton = new Button("Edit").createJSWidget(context);
	editButton.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		CRUD.this.editSelected();
	    }
	});
	buttongrGroup.add(editButton);
	
	Button createButton = new Button("New").createJSWidget(context);
	createButton.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		CRUD.this.createNew();
	    }
	});
	buttongrGroup.add(createButton);
	
	this.refresh();

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
		    editor.remove();
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
		    CRUD.this.provider.insert(editor.getValue());
		    editor.remove();
		    createWindow.remove();
		    CRUD.this.refresh();
		}
	    });	    
	    
	    createWindow.add(editor);
	    context.getMainPane().add(createWindow);	
    }

    public void refresh() {
	
	List<T> alldata = this.provider.getAll();
	
	this.pagination.setTotalrows(alldata.size());
	Range range = this.pagination.getCurrentRange();
	this.table.setData(alldata.subList(range.getFromIndex(), range.getToIndex()));
	this.table.refresh();
    }

}
