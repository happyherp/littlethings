package de.carlos.socketfront.autogui;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.InputSourceWidget;

public class ObjectInputSourceFactory<T> implements InputSourceFactory<T> {


    @Override
    public InputSourceWidget<T> create(GuiContext context, Class<T> parameter) {

	Objectcreator<T> creator = context.addWidget(new Objectcreator<T>());
	creator.createContent(parameter);
	return creator;
    }

}
