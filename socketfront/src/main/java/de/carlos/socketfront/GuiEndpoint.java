package de.carlos.socketfront;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.carlos.socketfront.widgets.MainPane;

@ServerEndpoint("/guiEndpoint/{gui-name}")
public class GuiEndpoint {

    private static Logger LOGGER = Logger.getLogger(GuiEndpoint.class);

    GuiContext context;

    public GuiEndpoint() {
	super();
	LOGGER.debug("Class instantiated.");
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config,
	    @PathParam("gui-name") String guiname) {
	LOGGER.debug("Connection open!.");

	SocketGUI gui = GuiMapping.getInstance().createGUI(guiname);
	// Setup
	context = new GuiContext();

	try {
	    gui.onCreate(context);
	} catch (RuntimeException e) {
	    LOGGER.error("Error while calling process Event",e);
	    context.showExceptionWindow(e);
	}

	String js =  context.getJsPipe().takeOutJS();
	session.getAsyncRemote().sendText(js);

    }

    @OnMessage
    public void receiveEvent(Session session, String msg, boolean last) {
	LOGGER.debug("Got an event! Message: " + msg);

	try {
	    JSONObject event = new JSONObject(msg);
	    
	    context.processEvent(event);
	    

	} catch (RuntimeException e) {
	    LOGGER.error("Error while calling process Event",e);
	    context.showExceptionWindow(e);
	}

	String js =  context.getJsPipe().takeOutJS();
	session.getAsyncRemote().sendText(js);
	
    }



}
