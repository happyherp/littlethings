package de.carlos.socketfront;

import java.io.PrintWriter;
import java.io.StringWriter;
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

import de.carlos.observer.Observer;
import de.carlos.socketfront.sample.PersonGui;
import de.carlos.socketfront.sample.TestAutoGui;
import de.carlos.socketfront.sample.TestGUI;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.MainPane;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.Window;
import de.carlos.socketfront.widgets.events.ClickEvent;
import de.carlos.util.Factory;
import de.carlos.util.FactoryImpl;

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
	context.setJsPipe(new JSPipe());
	context.setMainPane(context.addWidget(new MainPane()));

	try {
	    gui.onCreate(context);
	} catch (RuntimeException e) {
	    LOGGER.error(e);
	    showExceptionWindow(context, e);
	}

	String js =  context.getJsPipe().takeOutJS();
	session.getAsyncRemote().sendText(js);

    }

    @OnMessage
    public void receiveEvent(Session session, String msg, boolean last) {
	LOGGER.debug("Got an event! Message: " + msg);

	try {
	    JSONObject event = new JSONObject(msg);
	    String id = event.getString("id");

	    Widget widget = context.getWidget(id);
	    if (widget == null) {
		throw new RuntimeException("Could not find widget with id: "
			+ id);
	    }
	    widget.receiveEvent(event);
	} catch (RuntimeException e) {
	    LOGGER.error(e);
	    showExceptionWindow(context, e);
	}

	String js =  context.getJsPipe().takeOutJS();
	session.getAsyncRemote().sendText(js);
	
    }

    protected void showExceptionWindow(GuiContext context2, RuntimeException e) {
	final Window window = context.addWidget(new Window(),
		context.getMainPane());

	StringWriter writer = new StringWriter();
	writer.write("An Excpetion occurred. ");
	e.printStackTrace(new PrintWriter(writer));

	window.add(context.addWidget(new Text(writer.toString())));
	Button closebutton = context.addWidget(new Button("OK"), window);
	closebutton.getOnClick().addObserver(
		new Observer<ClickEvent<Button>>() {

		    @Override
		    public void update(ClickEvent<Button> event) {
			window.close();
		    }
		});
    }

}
