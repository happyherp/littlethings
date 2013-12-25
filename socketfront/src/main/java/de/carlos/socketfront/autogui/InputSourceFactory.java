package de.carlos.socketfront.autogui;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.InputSource;

public interface InputSourceFactory<T> {

    public InputSource<T> create(GuiContext context, Class<T> parameter);

}
