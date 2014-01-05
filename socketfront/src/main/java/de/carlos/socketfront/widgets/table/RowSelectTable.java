package de.carlos.socketfront.widgets.table;

import java.util.List;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Widget;

public class RowSelectTable<T> implements WidgetComposition{
    
    //TODO: Finish this class.
    
     RowTable<T> table;
     
    RowTableDrawInstructions<T> drawInstructions;
    
    //RadioGroup<T> radiogroup = new RadioGroup<T>();
    
    @Override
    public void create(GuiContext context){	
	
	
		
    }

    public void setDrawInstructions(final RowTableDrawInstructions<T> drawInstructions) {
	
	
	//Modfiy instructions to create radiobuttons in first column.
	RowTableDrawInstructions<T> decorator = new RowTableDrawInstructions<T>(){

	    @Override
	    public List<String> createHeader() {
		
		List<String> header = drawInstructions.createHeader();
		header.add(0, "Choice");		
		return header;
	    }

	    @Override
	    public List<Widget> createRow(T data) {
		List<Widget> widgets = drawInstructions.createRow(data);
		
		//widgets.add(0, )
		return widgets;
	    }
	    
	};
	
	
        this.drawInstructions = decorator;
    }

    @Override
    public Widget getMainWidget() {
	return this.getMainWidget();
    }
    

}
