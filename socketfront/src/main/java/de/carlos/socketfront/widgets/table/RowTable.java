package de.carlos.socketfront.widgets.table;

import java.util.ArrayList;
import java.util.List;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.JSWidget;

public class RowTable<T> implements Widget {

    Grid grid;

    List<T> data = new ArrayList<>();
    
    RowTableDrawInstructions<T> instructions;
    
    public RowTable(RowTableDrawInstructions<T> instructions){
	this.instructions = instructions;
    }
    
    @Override
    public Grid createJSWidget(GuiContext context) {
	this.grid = new Grid(0, 1).createJSWidget(context);

	// Draw headline.
	int col = 0;
	for (String label : this.instructions.createHeader()) {
	    if (this.grid.getColumns() <= col) {
		this.grid.appendColumn();
	    }
	    this.grid.setCell(new Text(label).createJSWidget(context), col, 0);
	    col++;
	}

	this.redrawData(context);
	return this.grid;
    }
    

    public void redrawData(GuiContext context) {
	int row = 1;
	for (T object : this.data) {
	    List<JSWidget> rowdata = this.instructions.createRow(context, object);
	    int col = 0;
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
    public JSWidget getMainJSWidget() {
	return this.grid;
    }




}
