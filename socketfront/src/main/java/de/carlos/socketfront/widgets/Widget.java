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
    
    /**
     * Add Information about this Widgets state.
     * 
     * 
     * @param info
     */
    void addInfo(Widget info);
    
    /**
     * Remove this Widget, and all Widgets created by it from the Client and the Context.
     * Must be called to free Memory.
     * 
     */
    void remove();

}
