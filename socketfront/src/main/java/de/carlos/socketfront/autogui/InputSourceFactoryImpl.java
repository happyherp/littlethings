package de.carlos.socketfront.autogui;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.util.FactoryImpl;

public class InputSourceFactoryImpl extends FactoryImpl<InputSourceWidget> implements InputSourceFactory<InputSourceWidget> {

    public InputSourceFactoryImpl(Class<? extends InputSourceWidget> clazz) {
	super(clazz);
    }

    @Override
    public InputSourceWidget create(GuiContext context, Class<InputSourceWidget> parameter) {
	return context.addWidget(this.create());
    }

}
