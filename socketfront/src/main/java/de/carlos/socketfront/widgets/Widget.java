package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;

public interface Widget {
        
    void setContext(GuiContext context);
    
    GuiContext getContext();
    
    void constructJSObject(GuiContext context);

    void receiveEvent(JSONObject event);
        

}
