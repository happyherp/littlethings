package de.carlos.socketfront;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

@WebServlet("/guiservlet")
public class GuiServlet extends HttpServlet {
    
    private static Logger LOGGER = Logger.getLogger(GuiServlet.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	
	
	JSONObject data = new JSONObject(new JSONTokener(req.getInputStream()));	
	

	if (data.has("openGui")) {
	    String guiname = data.getString("openGui");
	    SocketGUI gui = GuiMapping.getInstance().createGUI(guiname);

	    GuiContext context = new GuiContext();
	    
	    Integer guiId = ContextContainer.getInstance().addContext(context);	
	    context.getJsPipe().addStatement("GuiInfo.guiId="+guiId);
	    
	    try{
		gui.onCreate(context);
	    }catch(Exception e){
		context.showExceptionWindow(e);
	    }
	    	   
	    resp.getWriter().write(context.getJsPipe().takeOutJS());;
	}else if (data.has("guiId")){
	    //TODO: What if a request overtakes another one? weird behaviour could apprear
	    //if events are processed in the wrong order.
	    GuiContext context = ContextContainer.getInstance().getContext(data.getInt("guiId"));
	    try{
		context.processEvent(data);
	    }catch(Exception e){
		context.showExceptionWindow(e);
	    }	    
	    resp.getWriter().write(context.getJsPipe().takeOutJS());;
	
	}else{
	    String message ="No idea what to do with this request. "+data; 
	   LOGGER.warn(message);
	   resp.getWriter().write("console.log("+ JSONObject.quote(message) + ")" );
	   
	}
	resp.getWriter().flush();

    }

}
