package de.carlos.socketfront;

import org.json.JSONObject;

import de.carlos.observer.Observable;

public class Button extends WidgetBase {

    private String caption;
    
    public Observable<ClickEvent> onclick = new Observable<ClickEvent>();
        
    public Button(JSPipe pipe, String caption) {
	super(pipe);
	generateId();
	this.caption = caption;
	this.jsPipe.addStatement(
		String.format("new Button(%s, %s);\n", 
			JSONObject.quote(this.id), 
			JSONObject.quote(this.caption)));
    }

    public String getCaption() {
	return this.caption;
    }
        
    public Observable<ClickEvent> getOnClick(){
	return this.onclick;
    }

}
