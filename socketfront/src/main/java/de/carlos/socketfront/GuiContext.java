package de.carlos.socketfront;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.carlos.socketfront.widgets.Widget;

public class GuiContext {
    
    private JSPipe jsPipe;
    
    private Map<String, Widget> idToWidget = new HashMap<String, Widget>();
    private Map<Widget, String> widgetToid = new HashMap<Widget, String>();

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

}
