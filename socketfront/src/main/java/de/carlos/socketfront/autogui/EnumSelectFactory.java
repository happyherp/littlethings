package de.carlos.socketfront.autogui;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.autogui.AutoGuiConfig.InputSourceWidgetFactory;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.Select;

public class EnumSelectFactory implements InputSourceWidgetFactory<Enum<?>> {

    @Override
    public InputSourceWidget<Enum<?>> create(GuiContext context, Class<Enum<?>> parameter) {
	
	Select<Enum<?>> select = new Select<Enum<?>>().createJSWidget(context);
	
	select.addOption("None", null);
	
	for (Enum<?> enumm:  parameter.getEnumConstants()){	    
	    select.addOption(enumm.toString(), enumm);
	}
	
	return select;
    }

}
