package de.carlos.socketfront;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/guiEndpoint")
public class GuiEndpoint {
    
    public GuiEndpoint(){
	super();
	LOGGER.fine("Class instantiated.");
    }

    private static Logger LOGGER = Logger.getLogger("de.carlos.socketfront.GuiEndpoint");

    @OnOpen
    public void createStuff(Session session, EndpointConfig config) {
	LOGGER.log(Level.FINE, "Connection open!.");
	session.getAsyncRemote().sendText("Hello from the server");
    }

    @OnMessage
    public void receiveEvent(Session session, String msg, boolean last) {
	LOGGER.log(Level.FINE, "Got an event! Message:", msg);
    }

}
