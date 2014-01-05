package de.carlos.socketfront.widgets.table;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.WidgetBase;

public class Grid extends WidgetBase {

	protected int columns;
	protected int rows;

	public Grid(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
	}

	@Override
	public void constructJSObject(GuiContext context) {
		this.jsPipe.addCall("new Grid", this.getId(), this.columns, this.rows);
	}

	public void setCell(Widget widget, int col, int row) {
		// Repeated calls to setCell will cause memory-leaks....
		if (col < 0 || row < 0 || col >= this.columns || row >= this.rows) {
			throw new RuntimeException("Dimensions out of bounds. Col: " + col
					+ " Row: " + row);
		}
		this.callThisJS("setCell", this.getContext().getId(widget) , col, row);

	}

	public void addRow(int rowindex) {
		this.rows++;
		this.callThisJS("addRow", rowindex);
	}
	
	public void appendRow(){
	    this.addRow(this.rows);
	}
	
	public void removeRow(int rowindex){
		if (rowindex < 0 || rowindex >= this.rows){
			throw new RuntimeException("Index out of bounds.");
		}
		this.rows--;
		this.callThisJS("removeRow", rowindex);
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public void appendColumn() {
	    this.columns++;
	    this.callThisJS("appendColumn");
	}

}
