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
    
    static private Map<String,Factory<SocketGUI>> nameToGui = new HashMap<String, Factory<SocketGUI>>();
    static{
	nameToGui.put("Test", new FactoryImpl<SocketGUI>(TestGUI.class));
	nameToGui.put("TestAuto", new FactoryImpl<SocketGUI>(TestAutoGui.class));
	nameToGui.put("Person", new FactoryImpl<SocketGUI>(PersonGui.class));
    }

    
    public GuiEndpoint(){
	super();
	LOGGER.debug("Class instantiated.");
    }
    
    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("gui-name") String guiname) {
	LOGGER.debug( "Connection open!.");
	
	 Factory<SocketGUI> factory = nameToGui.get(guiname);
	 if (factory == null){
	     LOGGER.warn("GUI with name: "+ guiname + " not found.");
	 }else{
	//Setup
	context = new GuiContext();
	context.setJsPipe(new JSPipe(session));
	context.setMainPane(context.addWidget(new MainPane()));
	
	factory.create().onCreate(context);
	
	 }
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
