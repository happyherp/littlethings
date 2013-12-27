package de.carlos.socketfront.widgets;

public class Grid extends WidgetBase {
    
    protected int columns;
    protected int rows;
    
    
    public Grid(int columns, int rows){
	this.columns = columns;
	this.rows = rows;
    }

    @Override
    public void constructJSObject() {
	this.jsPipe.addCall("new Grid", this.getId(), this.columns, this.rows);
    }
    
    public void setCell(Widget widget,int col, int row){
	//Repeated calls to setCell will cause memory-leaks....
	if (col < 0 || row < 0 ||  col >= this.columns || row >= this.rows){
	    throw new RuntimeException("Dimensions out of bounds. Col: "+col +" Row: "+ row);
	}
	this.callThisJS("setCell", widget.getId(), col, row);
	
    }
    
    public void addRow(int rownum){
	this.rows++;
	this.callThisJS("addRow", rownum);
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }


}
