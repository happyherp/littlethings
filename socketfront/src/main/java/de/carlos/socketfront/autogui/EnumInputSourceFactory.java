package de.carlos.socketfront.autogui;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.Select;

public class EnumInputSourceFactory implements InputSourceFactory<Enum> {

    @Override
    public InputSourceWidget<Enum> create(GuiContext context, Class<Enum> parameter) {
	
	Select select = context.addWidget(new Select());
	
	
	for (Enum enumm:  parameter.getEnumConstants()){	    
	    select.addOption(enumm.toString(), enumm);
	}
	
	return select;
    }

}
