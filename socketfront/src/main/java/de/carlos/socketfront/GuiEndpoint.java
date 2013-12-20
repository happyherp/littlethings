package de.carlos.socketfront;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;



@ServerEndpoint("/guiEndpoint")
public class GuiEndpoint {
    
    
    
    private static Logger LOGGER = Logger.getLogger(GuiEndpoint.class);
    
    
    public GuiEndpoint(){
	super();
	LOGGER.debug("Class instantiated.");
    }


    @OnOpen
    public void createStuff(Session session, EndpointConfig config) {
	LOGGER.debug( "Connection open!.");
	session.getAsyncRemote().sendText("Hello from the server");
    }

    @OnMessage
    public void receiveEvent(Session session, String msg, boolean last) {
	LOGGER.debug( "Got an event! Message: "+ msg);
    }

}
