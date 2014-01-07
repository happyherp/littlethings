package de.carlos.socketfront.widgets;

public class JSWidgetID {
    
    private String id;
    
    public JSWidgetID(String id){
	this.id = id;
    }

    public String getString() {
        return id;
    }
    
    @Override
    public boolean equals(Object o){
	if (o instanceof JSWidgetID){
	    return this.id.equals(((JSWidgetID)o).getString());
	}
	return super.equals(o);
    }
    
    @Override
    public int hashCode(){
	return this.id.hashCode();
    }
    
    @Override
    public String toString(){
	return this.id;
    }

}
