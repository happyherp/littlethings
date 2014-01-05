package de.carlos.socketfront;

import org.json.JSONObject;

public class JSPipe {
    
    StringBuffer buffer = new StringBuffer();


    public void addStatement(String stmt) {
    	this.buffer.append(stmt+"\n");
    }
    
    public void addCall(String function, Object... args){
	String call = function+"(";
	
	if (args.length > 0){
	    call += JSONObject.valueToString(args[0]);
	    for (int i = 1 ; i< args.length; i++){
		call += ","+ JSONObject.valueToString(args[i]);
	    }
	}
	call += ")";
	this.addStatement(call);	
    }
    
    public String  takeOutJS(){
	String js = buffer.toString();
    	buffer = new StringBuffer();
    	return js; 
    }

}
