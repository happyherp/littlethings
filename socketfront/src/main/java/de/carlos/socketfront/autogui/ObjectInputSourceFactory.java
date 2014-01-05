package de.carlos.socketfront.autogui;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.InputSourceWidgetCombinator;

public class ObjectInputSourceFactory<T> implements InputSourceWidgetFactory<T> {


    @Override
    public InputSourceWidget<T> create(GuiContext context, Class<T> parameter) {

	Objectcreator<T> creator = new Objectcreator<T>(parameter);
	creator.create(context);
	context.addWidget(creator.getMainWidget());
	return new InputSourceWidgetCombinator<T>(creator.getMainWidget(),  creator);
    }

}
