package de.carlos.socketfront.widgets.table;

import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Widget;

public class RowSelectTable<T> extends RowTable<T> {

    public RowSelectTable(RowTableDrawInstructions<T> instructions) {
	super(instructions);
    }
    
    @Override
    public void constructJSObject(){
	
	
	//Modfiy instructions to create radiobuttons in first column.
	
	
	
	super.constructJSObject();
	
    }
    

}
