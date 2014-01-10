package de.carlos.socketfront.widgets;

public abstract class ControlWidget extends JSWidgetBase {
    
    protected boolean disabled = false;

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
	this.callThisJS("setDisabled", disabled);
        this.disabled = disabled;
    }

}
