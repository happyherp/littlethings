package de.carlos.socketfront.sample;

import javax.websocket.server.ServerEndpoint;

import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.SocketGUI;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.Checkbox;
import de.carlos.socketfront.widgets.ClickEvent;
import de.carlos.socketfront.widgets.Select;
import de.carlos.socketfront.widgets.Table;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.TextInput;
import de.carlos.socketfront.widgets.Window;

public class TestGUI implements SocketGUI {

    GuiContext context;
    int buttoncount = 1;
    TextInput textinput;
    Checkbox box;
    Select<Double> select;
    private Text selecttext;
    Table table;

    @Override
    public void onCreate(GuiContext ctx) {
	this.context = ctx;

	// Create a button that makes more buttons.
	Button first = ctx.addWidget(new Button("More buttons"));

	this.context.getMainPane().add(first);

	first.getOnClick().addObserver(new Observer<ClickEvent>() {

	    @Override
	    public void update(ClickEvent event) {
		Button newbutton = context.addWidget(new Button(
			"Button number " + buttoncount));
		buttoncount++;
		context.getMainPane().add(newbutton);

		newbutton.getOnClick().addObserver(new Observer<ClickEvent>() {
			    @Override
			    public void update(ClickEvent event) {
				context.getJsPipe().addStatement(
					"alert(\"Button clicked!\");");
			    }
			});
	    }
	});

	textinput = ctx.addWidget(new TextInput());
	textinput.setValue("Edit me!");
	context.getMainPane().add(textinput);

	Button submit = ctx.addWidget(new Button("Eingabe"));
	submit.getOnClick().addObserver(new Observer<ClickEvent>() {
	    @Override
	    public void update(ClickEvent event) {
		context.getJsPipe().addStatement(
			"alert(\"" + textinput.getValue() + "\");\n");
	    }
	});
	context.getMainPane().add(submit);

	box = ctx.addWidget(new Checkbox());
	context.getMainPane().add(box);

	Button toggle = ctx.addWidget(new Button("Toggle"));
	toggle.getOnClick().addObserver(new Observer<ClickEvent>() {
	    @Override
	    public void update(ClickEvent event) {
		box.setValue(!box.getValue());
	    }
	});
	context.getMainPane().add(toggle);
	
	selecttext =  ctx.addWidget(new Text("This is a selectbox"), ctx.getMainPane());

	select = ctx.addWidget(new Select<Double>());
	select.addOption("half", 0.5);
	select.addOption("a third", 0.3);
	select.addOption("pii", Math.PI);
	select.setSelected(0.3);
	context.getMainPane().add(select);

	Button selectButton = ctx.addWidget(new Button("show selection"));
	selectButton.getOnClick().addObserver(new Observer<ClickEvent>() {
		    @Override
		    public void update(ClickEvent event) {
			selecttext.setText("You selected "+ select.getSelected());

		    }
		});

	context.getMainPane().add(selectButton);
	
	table = ctx.addWidget(new Table(4,7));

	for (int x = 1;x<=table.getColumns();x++){
	    for (int y = 1;y <= table.getRows();y++){
		table.setCell(ctx.addWidget(new Text(""+(x*y))), x-1, y-1);
	    }
	}
	
	table.setCell(context.addWidget(new Button("I am button")), 2, 6);
	context.getMainPane().add(table);
	
	Text absText = context.addWidget(new Text("I am flying over things!"), context.getMainPane());
	absText.setPositionAbsolute(100, 200);
	
	Button openWindow = context.addWidget(new Button("open window"), context.getMainPane());
	openWindow.getOnClick().addObserver(new Observer<ClickEvent>() {
	    @Override
	    public void update(ClickEvent event) {
		Window window = context.addWidget(new Window(), context.getMainPane());
		Button closeButton = context.addWidget(new Button("Close"));
		window.add(closeButton);		
		closeButton.getOnClick().addObserver(new WindowCloser(window));
	    }
	});
	
    }
    
    private class WindowCloser implements Observer<ClickEvent>{
	
	Window window;
	
	public WindowCloser(Window window){
	    this.window = window;
	}

	@Override
	public void update(ClickEvent event) {
	    this.window.close();
	}
	
    }

}
