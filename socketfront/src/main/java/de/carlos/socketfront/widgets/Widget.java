package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;

public interface Widget {
    
    String getId();
    
    void setContext(GuiContext context);
    
    void constructJSObject();

    void receiveEvent(JSONObject event);
        

}
