package de.carlos.socketfront.widgets.table;

import java.util.List;

import de.carlos.socketfront.widgets.Widget;

public interface RowTableDrawInstructions<T> {
    List<String> createHeader();

    List<Widget> createRow(T data);
}
