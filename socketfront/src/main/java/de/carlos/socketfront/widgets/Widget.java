package de.carlos.socketfront.widgets;

import de.carlos.socketfront.GuiContext;

public interface Widget {
        
    /**
     * Build this Widget by creating the Client-Side representation.
     * 
     * Return the Main-Widget that is used to append this Widget to other Widgets.
     * 
     * @param context
     * @return
     */
    JSWidget createJSWidget(GuiContext context);
    
    /**
     * Return the JSWidget that was returned be the create-Method.
     * 
     * @return
     */
    JSWidget getMainJSWidget();

}
