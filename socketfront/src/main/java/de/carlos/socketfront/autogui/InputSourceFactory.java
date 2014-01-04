package de.carlos.socketfront.autogui;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.InputSourceWidget;

public interface InputSourceFactory<T> {

    public InputSourceWidget<T> create(GuiContext context, Class<T> parameter);

}
