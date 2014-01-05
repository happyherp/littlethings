package de.carlos.socketfront.widgets.table;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Widget;


/**
 * Interface for classes that create new Functionality by using other widgets.
 * 
 * @author Carlos
 *
 */
public interface WidgetComposition {
    
    /**
     * Returns the Widget that is used to draw this composition.
     * 
     * @return
     */
    Widget getMainWidget();
    
   void create(GuiContext context);

        

}
