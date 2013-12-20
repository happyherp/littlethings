package de.carlos.socketfront;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import de.carlos.observer.Observer;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.ClickEvent;
import de.carlos.socketfront.widgets.TextInput;

@ServerEndpoint("/testGuiEndpoint")
public class TestGUI extends GuiEndpoint {

    GuiContext context;
    int buttoncount = 1;    
    TextInput textinput;

    @Override
    public void onStart(GuiContext ctx) {
	this.context = ctx;

	//Create a button that makes more buttons.
	Button first = new Button(context, "More buttons");
	
	this.context.getMainPane().add(first);

	
	first.getOnClick().getObservers().add(new Observer<ClickEvent>() {
	    
	    @Override
	    public void update(ClickEvent event) {
		Button newbutton = new Button(context, "Button number "+ buttoncount);
		buttoncount++;
		context.getMainPane().add(newbutton);
		
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
	context.getMainPane().add(textinput);
	
	Button submit = new Button(context, "Eingabe");
	submit.getOnClick().getObservers().add(new Observer<ClickEvent>() {
	    @Override
	    public void update(ClickEvent event) {
		context.getJsPipe().addStatement("alert(\""+textinput.getValue()+"\");\n");
	    }
	});
	context.getMainPane().add(submit);
	
    }
    
    
    //TODO: These two methods must be here so the annotations are found. 
    // Must find better way to do this.
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
	super.onOpen(session, config);
	
    }

    @OnMessage
    public void receiveEvent(Session session, String msg, boolean last) {
	super.receiveEvent(session, msg, last);	
    }
    

}
