package de.carlos.socketfront;

import javax.websocket.Session;

import org.json.JSONObject;

public class JSPipe {

    Session session;

    public JSPipe(Session session) {
	this.session = session;
    }

    public void addStatement(String stmt) {
	this.session.getAsyncRemote().sendText(stmt);
    }
    
    public void addCall(String function, Object... args){
	String call = function+"(";
	
	if (args.length > 0){
	    call += JSONObject.valueToString(args[0]);
	    for (int i = 1 ; i< args.length; i++){
		call += ","+ JSONObject.valueToString(args[i]);
	    }
	}
	call += ");\n";
	this.addStatement(call);	
    }

}
