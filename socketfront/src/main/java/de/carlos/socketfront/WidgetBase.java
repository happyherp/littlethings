package de.carlos.socketfront;

import java.util.Random;

public class WidgetBase implements Widget {

    protected String id;
    protected JSPipe jsPipe;
    
    protected WidgetBase(JSPipe jsPipe){
	this.jsPipe = jsPipe;
    }

    @Override
    public String getId() {
	return this.id;
    }

    protected void generateId() {
	this.id = String.format("generated:%d", new Random().nextInt());
    }

}
