package de.carlos.socketfront.widgets;

import de.carlos.socketfront.GuiContext;

public class VGroup extends Group {

    @Override
    public Group createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	jsPipe.addCall("new VGroup", this.getId());
	return this;
    }

}
