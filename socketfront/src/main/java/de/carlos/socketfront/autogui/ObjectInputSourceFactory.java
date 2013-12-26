package de.carlos.socketfront.autogui;

import org.apache.log4j.Logger;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.InputSource;

public class ObjectInputSourceFactory<T> implements InputSourceFactory<T> {


    @Override
    public InputSource<T> create(GuiContext context, Class<T> parameter) {

	Objectcreator<T> creator = context.addWidget(new Objectcreator<T>());
	creator.createContent(parameter);
	return creator;
    }

}
