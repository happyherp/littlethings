package de.carlos.socketfront.autogui;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.autogui.AutoGuiConfig.InputSourceWidgetFactory;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.Select;

public class EntitySelectFactory<T> implements InputSourceWidgetFactory<T> {
    
    private Provider<T> provider;

    public EntitySelectFactory(Provider<T> provider){
	this.provider = provider;
    }

    @Override
    public InputSourceWidget<T> create(GuiContext context, Class<T> parameter) {
	
	Select<T> select = new Select<>();
	select.createJSWidget(context);
	select.addOption("None", null);

	for (T obj: provider.getAll()){
	    select.addOption(obj.toString(), obj);
	}
	
	return select;
    }

}
