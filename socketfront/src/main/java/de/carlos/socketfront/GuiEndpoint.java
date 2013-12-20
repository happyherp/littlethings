package de.carlos.socketfront;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.carlos.socketfront.widgets.Widget;

public abstract class GuiEndpoint {
           
    private static Logger LOGGER = Logger.getLogger(GuiEndpoint.class);
    
    GuiContext context;   

    
    public GuiEndpoint(){
	super();
	LOGGER.debug("Class instantiated.");
    }
    
    abstract public void onStart(GuiContext context);

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
	LOGGER.debug( "Connection open!.");
	
	//Setup
	context = new GuiContext();
	context.setJsPipe(new JSPipe(session));
	context.setMainPane(new MainPane(context));
	
	onStart(context);
	
    }

    @OnMessage
    public void receiveEvent(Session session, String msg, boolean last) {
	LOGGER.debug( "Got an event! Message: "+ msg);
	
	JSONObject event = new JSONObject(msg);	
	String id = event.getString("id");
	
	Widget widget = context.getWidget(id);
	if (widget == null){
	    LOGGER.warn("Could not find widget with id: "+ id);
	}
	widget.receiveEvent(event);	
    }

}
