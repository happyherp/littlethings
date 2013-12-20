package de.carlos.socketfront;

import org.json.JSONObject;

public interface Widget {
    
    public String getId();
    
    /**
     * Set the initial id of this widget.
     * 
     * @param id
     */
    public void setId(String id);

    public void receiveEvent(JSONObject event);
        

}
