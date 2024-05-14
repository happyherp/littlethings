package de.carlos.socketfront.widgets;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.JSPipe;

public abstract class JSWidgetBase implements JSWidget {

    private static Logger LOGGER = Logger.getLogger(JSWidgetBase.class);

    protected GuiContext context;
    
    protected JSPipe jsPipe;
    
    @Override
    public JSWidget createJSWidget(GuiContext context) {
	this.context = context;
	this.jsPipe = context.getJsPipe();
	
	if (context.hasId(this)){
	    throw new RuntimeException("Widget already had an id."+context.getId(this));
	}
	
	this.registerToContext(context);
	
	return this;
    }
    
    /**
     * Tell the context under which id this widget should be saved.
     * 
     * @param context
     */
    protected void registerToContext(GuiContext context) {
	context.generateId(this);
	
    }

    @Override
    public JSWidget getMainJSWidget(){
	return this;
    }
	

    public JSWidgetID getId() {
	return this.context.getId(this);
    }

    @Override
    public void receiveEvent(GuiContext context, JSONObject event) {
	LOGGER.info("Unhandled Event received: " + event.toString(2));
    }

    protected String getJSObject() {
	return String.format("GuiInfo.idToWidget[%s]", JSONObject.quote(this.getId().getString()));
    }

    protected void callThisJS(String method, Object... args) {
	if (context == null){
	    throw new RuntimeException("Context was not set. Probable cause is that this Object was not added to a Context.");
	}
	context.getJsPipe().addCall(getJSObject() + "." + method, args);
    }
    
    public void setPositionAbsolute(int x, int y){
	this.callThisJS("setPositionAbsolute", x,y);
    }
    
    /**
     * Call this to remove references to this widget from the GUIContext as well as
     * its javascript representation in the browser.
     * 
     */
    @Override
    public void remove(){
	this.callThisJS("remove");
	this.context.removeWidget(this);
    }
    
    @Override
    public void addInfo(Widget info){
	this.callThisJS("addInfo", info.getMainJSWidget().getId().getString());
    }
    
    protected GuiContext getContext(){
	return this.context;
    }
    

}
