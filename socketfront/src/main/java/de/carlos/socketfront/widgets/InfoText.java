package de.carlos.socketfront.widgets;

import de.carlos.socketfront.GuiContext;

public class InfoText extends Text {

    public InfoText(String text) {
	super(text);
    }
    
    public InfoText createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	return this;
    }


}
