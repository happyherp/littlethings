package de.carlos.socketfront.widgets;

public class Text extends WidgetBase {

    String text;

    public Text(String text) {
	this.text = text;
    }

    @Override
    public void constructJSObject() {
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
