package de.carlos.socketfront;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.carlos.socketfront.sample.PersonGui;
import de.carlos.socketfront.sample.TestAutoGui;
import de.carlos.socketfront.sample.TestGUI;
import de.carlos.socketfront.widgets.MainPane;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.util.Factory;
import de.carlos.util.FactoryImpl;

@ServerEndpoint("/guiEndpoint/{gui-name}")
public class GuiEndpoint {
           
    private static Logger LOGGER = Logger.getLogger(GuiEndpoint.class);
    
    GuiContext context;   
    

    
    public GuiEndpoint(){
	super();
	LOGGER.debug("Class instantiated.");
    }
    
    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("gui-name") String guiname) {
	LOGGER.debug( "Connection open!.");
	
	 SocketGUI gui = GuiMapping.getInstance().createGUI(guiname);
	//Setup
	context = new GuiContext();
	context.setJsPipe(new JSPipe());
	context.setMainPane(context.addWidget(new MainPane()));
	
	gui.onCreate(context);
	
	String js = context.getJsPipe().flushJS();
	
	 
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
