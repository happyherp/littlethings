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
import de.carlos.socketfront.widgets.Window;
import de.carlos.socketfront.widgets.events.ClickEvent;

public class GuiContext {

    private JSPipe jsPipe;

    private Map<String, Widget> idToWidget = new HashMap<String, Widget>();
    private Map<Widget, String> widgetToid = new HashMap<Widget, String>();

    private MainPane mainPane;

    int idCount = 0;

    public <T extends Widget> T addWidget(T widget) {
	if (widget.getContext() != null) {
	    throw new RuntimeException("Widget with id " + widget.getId()
		    + " already has a context.");
	}
	widget.setContext(this);
	widget.constructJSObject();
	return widget;
    }

    public <T extends Widget> T addWidget(T widget, Parent parent) {
	addWidget(widget);
	parent.add(widget);
	return widget;
    }

    public JSPipe getJsPipe() {
	return jsPipe;
    }

    public void setJsPipe(JSPipe jsPipe) {
	this.jsPipe = jsPipe;
    }

    public String getId(Widget widget) {
	return widgetToid.get(widget);
    }

    public Widget getWidget(String id) {
	return idToWidget.get(id);
    }

    public void setId(Widget widget, String id) {
	idToWidget.put(id, widget);
	widgetToid.put(widget, id);
    }

    public void generateId(Widget widget) {

	String id = String.format("generated:%d", this.idCount);
	while (this.idToWidget.containsKey(id)) {
	    this.idCount++;
	    id = String.format("generated:%d", this.idCount);
	}

	setId(widget, id);
    }

    public MainPane getMainPane() {
	return mainPane;
    }

    public void setMainPane(MainPane mainPane) {
	this.mainPane = mainPane;
    }

    public void removeWidget(Widget widgetBase) {
	this.idToWidget.remove(widgetBase.getId());
	this.widgetToid.remove(widgetBase);
    }

    public void showExceptionWindow(Exception e) {
	final Window window = this.addWidget(new Window(), this.getMainPane());

	StringWriter writer = new StringWriter();
	writer.write("An Excpetion occurred. ");
	e.printStackTrace(new PrintWriter(writer));

	window.add(this.addWidget(new Text(writer.toString())));
	Button closebutton = this.addWidget(new Button("OK"), window);
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

	Widget widget = this.getWidget(id);
	if (widget == null) {
	    throw new RuntimeException("Could not find widget with id: " + id);
	}
	widget.receiveEvent(event);
    }

}
