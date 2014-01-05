package de.carlos.socketfront.widgets;

import de.carlos.socketfront.GuiContext;

public class Text extends WidgetBase {

    String text;

    public Text(String text) {
	this.text = text;
    }

    @Override
    public void constructJSObject(GuiContext context) {
	this.jsPipe.addCall("new Text", this.getId(), this.getText());
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
	this.text = text;
	this.callThisJS("setText", this.getText());
    }

}
