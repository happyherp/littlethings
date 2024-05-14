package de.carlos.socketfront.widgets;

import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;

public interface JSWidget extends Widget {
                
    void receiveEvent(GuiContext context, JSONObject event);
    
    JSWidgetID getId();
        

}
