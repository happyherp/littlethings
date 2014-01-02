package de.carlos.socketfront;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.carlos.socketfront.widgets.MainPane;

@WebServlet("/guiservlet")
public class GuiServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {

	if (req.getParameterMap().containsKey("openGui")) {
	    String guiname = req.getParameter("openGui");
	    SocketGUI gui = GuiMapping.getInstance().createGUI(guiname);

	    GuiContext context = new GuiContext();
	    context.setJsPipe(new JSPipe());
	    context.setMainPane(context.addWidget(new MainPane()));

	}

    }

}
