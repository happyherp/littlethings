package de.carlos.socketfront;

import org.json.JSONObject;

import de.carlos.observer.Observable;

public class Button extends WidgetBase {

    private String caption;
    
    public Observable<ClickEvent> onclick = new Observable<ClickEvent>();
        
    public Button(GuiContext context, String caption) {
	super(context);
	context.generateId(this);
	this.caption = caption;
	this.jsPipe.addStatement(
		String.format("new Button(%s, %s);\n", 
			JSONObject.quote(this.getId()), 
			JSONObject.quote(this.caption)));
    }

    public String getCaption() {
	return this.caption;
    }
        
    public Observable<ClickEvent> getOnClick(){
	return this.onclick;
    }
    
    @Override
    public void receiveEvent(JSONObject jsonevent){
	ClickEvent event = new ClickEvent();
	event.setSource(this);
	this.onclick.fire(event);
    }

}
