package de.carlos.socketfront;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import de.carlos.observer.Observer;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.MainPane;
import de.carlos.socketfront.widgets.Parent;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.JSWidget;
import de.carlos.socketfront.widgets.JSWidgetID;
import de.carlos.socketfront.widgets.Window;
import de.carlos.socketfront.widgets.events.ClickEvent;


/**
 * 
 * Manages Ids between the client and the server. This Object is supposed to stay intact for the duration
 * of the session on a single GUI.
 * 
 * 
 * @author Carlos
 *
 */
public class GuiContext {

    private JSPipe jsPipe = new JSPipe();

    private Map<JSWidgetID, JSWidget> idToWidget = new HashMap<JSWidgetID, JSWidget>();
    private Map<JSWidget, JSWidgetID> widgetToid = new HashMap<JSWidget, JSWidgetID>();

    private MainPane mainPane;

    int idCount = 0;

    int radioCount = 0;
    
    public GuiContext(){
	this.mainPane = new MainPane().createJSWidget(this);
    }

    @Deprecated
    public <T extends Widget> T addWidget(T widget) {
	widget.createJSWidget(this);
	return widget;
    }

    @Deprecated
    public <T extends JSWidget> T addWidget(T widget, Parent parent) {
	addWidget(widget);
	parent.add(widget);
	return widget;
    }

    public JSPipe getJsPipe() {
	return jsPipe;
    }


    public boolean hasId(JSWidget widget) {
	return this.widgetToid.containsKey(widget);
    }

    public JSWidgetID getId(JSWidget widget) {
	JSWidgetID id = widgetToid.get(widget);
	if (id == null) {
	    throw new RuntimeException("Id for Widget " + widget
		    + " could not be found.");
	}
	return id;
    }

    public JSWidget getWidget(String id) {

	JSWidget widget = idToWidget.get(new JSWidgetID(id));
	if (widget == null) {
	    throw new RuntimeException("No widget with id " + id
		    + " could be found.");
	}
	return widget;
    }

    public void setId(JSWidget widget, JSWidgetID id) {
	idToWidget.put(id, widget);
	widgetToid.put(widget, id);
    }

    public void generateId(JSWidget widget) {

	String idstring = String.format("generated/%s/%d", widget.getClass().getName(), this.idCount);
	while (this.idToWidget.containsKey(new JSWidgetID(idstring))) {
	    this.idCount++;
	    idstring = String.format("generated/%s/%d", widget.getClass().getName(), this.idCount);
	}

	setId(widget, new JSWidgetID(idstring));
    }

    public MainPane getMainPane() {
	return mainPane;
    }


    public void removeWidget(JSWidget widget) {
	this.idToWidget.remove(this.getId(widget));
	this.widgetToid.remove(widget);
    }

    public void showExceptionWindow(Exception e) {
	final Window window = new Window().createJSWidget(this);
	this.getMainPane().add(window);

	StringWriter writer = new StringWriter();
	writer.write("An Excpetion occurred. ");
	e.printStackTrace(new PrintWriter(writer));

	window.add(new Text(writer.toString()).createJSWidget(this));
	Button closebutton = window.add(new Button("OK").createJSWidget(this));
	closebutton.getOnClick().addObserver(
		new Observer<ClickEvent<Button>>() {

		    @Override
		    public void update(ClickEvent<Button> event) {
			window.close();
		    }
		});
    }

    public void processEvent(JSONObject event) {
	String id = event.getString("id");

	JSWidget widget = this.getWidget(id);
	if (widget == null) {
	    throw new RuntimeException("Could not find widget with id: " + id);
	}
	widget.receiveEvent(this, event);
    }

    public String newRadioGroupName() {
	radioCount++;
	return "radiogroup#" + radioCount;
    }

    public void alert(String string) {
	this.jsPipe.addCall("alert", string);
	
    }

}
