package de.carlos.socketfront.autogui;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.InputSource;
import de.carlos.util.FactoryImpl;

public class InputSourceFactoryImpl extends FactoryImpl<InputSource> implements InputSourceFactory<InputSource> {

    public InputSourceFactoryImpl(Class<? extends InputSource> clazz) {
	super(clazz);
    }

    @Override
    public InputSource create(GuiContext context, Class<InputSource> parameter) {
	return context.addWidget(this.create());
    }

}
