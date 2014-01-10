package de.carlos.socketfront.widgets;

import de.carlos.socketfront.GuiContext;

public class Text extends JSWidgetBase {

    String text;

    public Text(String text) {
	this.text = text;
    }

    @Override
    public Text createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	this.jsPipe.addCall("new Text", this.getId(), this.getText());
	return this;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
	this.text = text;
	this.callThisJS("setText", this.getText());
    }

}
