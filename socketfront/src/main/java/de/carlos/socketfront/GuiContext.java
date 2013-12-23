package de.carlos.socketfront;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.carlos.socketfront.widgets.MainPane;
import de.carlos.socketfront.widgets.Widget;

public class GuiContext {
    
    private JSPipe jsPipe;
    
    private Map<String, Widget> idToWidget = new HashMap<String, Widget>();
    private Map<Widget, String> widgetToid = new HashMap<Widget, String>();
    
    private MainPane mainPane;

    public <T extends Widget> T addWidget(T widget){
	widget.setContext(this);
	widget.constructJSObject();
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
    
    public Widget getWidget(String id){
	return idToWidget.get(id);
    }
    
    public void setId(Widget widget, String id){
	idToWidget.put(id, widget);
	widgetToid.put(widget, id);
    }

    public void generateId(Widget widget) {
	String id = String.format("generated:%d", new Random().nextInt());
	setId(widget, id);	
    }
    

    public MainPane getMainPane() {
        return mainPane;
    }

    public void setMainPane(MainPane mainPane) {
        this.mainPane = mainPane;
    }

}
