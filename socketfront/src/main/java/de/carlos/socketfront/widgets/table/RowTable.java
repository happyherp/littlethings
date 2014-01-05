package de.carlos.socketfront.widgets.table;

import java.util.ArrayList;
import java.util.List;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.Widget;

public class RowTable<T> implements WidgetComposition {

    Grid grid;

    List<T> data = new ArrayList<>();
    
    RowTableDrawInstructions<T> instructions;
    
    public RowTable(RowTableDrawInstructions<T> instructions){
	this.instructions = instructions;
    }
    
    @Override
    public void create(GuiContext context) {
	this.grid = new Grid(0, 1);
	context.addWidget(grid);

	// Draw headline.
	int col = 0;
	for (String label : this.instructions.createHeader()) {
	    if (this.grid.getColumns() <= col) {
		this.grid.appendColumn();
	    }
	    this.grid.setCell(context.addWidget(new Text(label)), col, 0);
	    col++;
	}

	this.redrawData();
    }
    

    public void redrawData() {
	int row = 1;
	for (T object : this.data) {
	    List<Widget> rowdata = this.instructions.createRow(object);
	    int col = 0;
	    this.grid.appendRow();
	    for (Widget widget : rowdata) {
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
    public Widget getMainWidget() {
	return this.grid;
    }



}
