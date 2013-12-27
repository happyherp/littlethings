package de.carlos.socketfront;

import javax.websocket.Session;

import org.json.JSONObject;

public class JSPipe {

    Session session;
    
    StringBuffer buffer = new StringBuffer();

    public JSPipe(Session session) {
	this.session = session;
    }

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
    
    public void sendToServer(){
    	this.session.getAsyncRemote().sendText(buffer.toString());
    	buffer = new StringBuffer();
    }

}
