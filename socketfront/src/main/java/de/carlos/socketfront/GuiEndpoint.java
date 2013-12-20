package de.carlos.socketfront;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.carlos.observer.Observer;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.ClickEvent;
import de.carlos.socketfront.widgets.TextInput;
import de.carlos.socketfront.widgets.Widget;

@ServerEndpoint("/guiEndpoint")
public class GuiEndpoint {
           
    private static Logger LOGGER = Logger.getLogger(GuiEndpoint.class);
    
    MainPane mainPane;    
    GuiContext context;   
    int buttoncount = 1;    
    TextInput textinput;

    
    public GuiEndpoint(){
	super();
	LOGGER.debug("Class instantiated.");
    }


    @OnOpen
    public void createStuff(Session session, EndpointConfig config) {
	LOGGER.debug( "Connection open!.");
	
	//Setup
	context = new GuiContext();
	context.setJsPipe(new JSPipe(session));
	mainPane = new MainPane(context);
	
	//Create a button that makes more buttons.
	Button first = new Button(context, "More buttons");
	
	mainPane.add(first);

	
	first.getOnClick().getObservers().add(new Observer<ClickEvent>() {
	    
	    @Override
	    public void update(ClickEvent event) {
		Button newbutton = new Button(context, "Button number "+ buttoncount);
		buttoncount++;
		mainPane.add(newbutton);
		
		newbutton.getOnClick().getObservers().add(new Observer<ClickEvent>() {		    
		    @Override
		    public void update(ClickEvent event) {
			context.getJsPipe().addStatement("alert(\"Button clicked!\");");			
		    }
		});		
	    }
	});	
	
	textinput = new TextInput(context);
	textinput.setValue("Edit me!");
	mainPane.add(textinput);
	
	Button submit = new Button(context, "Eingabe");
	submit.getOnClick().getObservers().add(new Observer<ClickEvent>() {
	    @Override
	    public void update(ClickEvent event) {
		context.getJsPipe().addStatement("alert(\""+textinput.getValue()+"\");\n");
	    }
	});
	mainPane.add(submit);
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
