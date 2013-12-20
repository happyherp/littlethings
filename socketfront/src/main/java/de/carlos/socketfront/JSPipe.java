package de.carlos.socketfront;

import javax.websocket.Session;

public class JSPipe {

    Session session;

    public JSPipe(Session session) {
	this.session = session;
    }

    public void addStatement(String stmt) {
	this.session.getAsyncRemote().sendText(stmt);
    }

}
