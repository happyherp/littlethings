package de.carlos.socketfront.widgets.table;

import java.util.ArrayList;
import java.util.List;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.JSWidget;

public class RowTable<T> implements Widget {

    GuiContext context;
    
    Grid grid;

    List<T> data = new ArrayList<>();
    
    RowTableDrawInstructions<T> instructions;
    
    public RowTable(RowTableDrawInstructions<T> instructions){
	this.instructions = instructions;
    }
    
    
    
    @Override
    public Grid createJSWidget(GuiContext context) {
	this.context = context;
	this.grid = new Grid(0, 0).createJSWidget(context);

	this.redrawData();
	return this.grid;
    }
    

    public void redrawData() {
	this.grid.clear();
	
	// Draw headline.
	int col = 0;
	grid.appendRow();
	for (String label : this.instructions.createHeader()) {
	    if (this.grid.getColumns() <= col) {
		this.grid.appendColumn();
	    }
	    this.grid.setCell(new Text(label).createJSWidget(context), col, 0);
	    col++;
	}
	
	
	int row = 1;
	for (T object : this.data) {
	    List<JSWidget> rowdata = this.instructions.createRow(context, object);
	    col = 0;
	    this.grid.appendRow();
	    for (JSWidget widget : rowdata) {
		if (this.grid.getColumns() < col) {
		    this.grid.appendColumn();
		}
		this.grid.setCell(widget, col, row);
		col++;
	    }
	    row++;
	}
    }

    public List<T> getData() {
	return data;
    }

    public void setData(List<T> data) {
	this.data = data;
    }

    @Override
    public Grid getMainJSWidget() {
	return this.grid;
    }



    @Override
    public void addInfo(Widget info) {
	this.grid.addInfo(info);
    }



    @Override
    public void remove() {
	this.grid.remove();
    }




}
