package de.carlos.socketfront.widgets.table;

import java.util.List;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.JSWidget;

public interface RowTableDrawInstructions<T> {
    List<String> createHeader();

    List<JSWidget> createRow(GuiContext context, T data);
}
