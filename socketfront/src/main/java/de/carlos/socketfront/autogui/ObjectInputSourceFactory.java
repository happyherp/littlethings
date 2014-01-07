package de.carlos.socketfront.autogui;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.autogui.AutoGuiConfig.InputSourceWidgetFactory;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.JSWidget;

public class ObjectInputSourceFactory<T> implements InputSourceWidgetFactory<T> {


    @Override
    public InputSourceWidget<T> create(GuiContext context, Class<T> parameter) {

	Objectcreator<T> creator = new Objectcreator<T>(parameter);
	JSWidget widget = creator.createJSWidget(context);
	return creator;
    }

}
