package de.carlos.socketfront;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

import de.carlos.observer.Observer;



@ServerEndpoint("/guiEndpoint")
public class GuiEndpoint {
    
    
    
    private static Logger LOGGER = Logger.getLogger(GuiEndpoint.class);
    
    MainPane mainPane;
    
    JSPipe jsPipe;
    
    int buttoncount = 1;

    
    
    public GuiEndpoint(){
	super();
	LOGGER.debug("Class instantiated.");
    }


    @OnOpen
    public void createStuff(Session session, EndpointConfig config) {
	LOGGER.debug( "Connection open!.");
	session.getAsyncRemote().sendText("alert(\"here we go\");");
	
	jsPipe = new JSPipe(session);
	
	mainPane = new MainPane(jsPipe);
	
	Button first = new Button(jsPipe, "More buttons");
	
	mainPane.add(first);

	
	first.getOnClick().getObservers().add(new Observer<ClickEvent>() {
	    
	    @Override
	    public void update(ClickEvent event) {
		Button newbutton = new Button(jsPipe, "Button number "+ buttoncount);
		buttoncount++;
		mainPane.add(newbutton);
		
		newbutton.getOnClick().getObservers().add(new Observer<ClickEvent>() {		    
		    @Override
		    public void update(ClickEvent event) {
			jsPipe.addStatement("alert(\"Button clicked!\");");			
		    }
		});		
	    }
	});
	
	
	
	
    }

    @OnMessage
    public void receiveEvent(Session session, String msg, boolean last) {
	LOGGER.debug( "Got an event! Message: "+ msg);
    }

}
