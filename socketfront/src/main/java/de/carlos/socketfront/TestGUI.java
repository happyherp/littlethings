package de.carlos.socketfront;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import de.carlos.observer.Observer;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.Checkbox;
import de.carlos.socketfront.widgets.ClickEvent;
import de.carlos.socketfront.widgets.Select;
import de.carlos.socketfront.widgets.Table;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.TextInput;

@ServerEndpoint("/testGuiEndpoint")
public class TestGUI extends GuiEndpoint {

    GuiContext context;
    int buttoncount = 1;
    TextInput textinput;
    Checkbox box;
    Select<Double> select;
    private Text selecttext;
    Table table;

    @Override
    public void onStart(GuiContext ctx) {
	this.context = ctx;

	// Create a button that makes more buttons.
	Button first = ctx.addWidget(new Button("More buttons"));

	this.context.getMainPane().add(first);

	first.getOnClick().getObservers().add(new Observer<ClickEvent>() {

	    @Override
	    public void update(ClickEvent event) {
		Button newbutton = context.addWidget(new Button(
			"Button number " + buttoncount));
		buttoncount++;
		context.getMainPane().add(newbutton);

		newbutton.getOnClick().getObservers()
			.add(new Observer<ClickEvent>() {
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
	submit.getOnClick().getObservers().add(new Observer<ClickEvent>() {
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
	toggle.getOnClick().getObservers().add(new Observer<ClickEvent>() {
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
	selectButton.getOnClick().getObservers()
		.add(new Observer<ClickEvent>() {
		    @Override
		    public void update(ClickEvent event) {
			selecttext.setText("You selected "+ select.getSelected());

		    }
		});

	context.getMainPane().add(selectButton);
	
	table = ctx.addWidget(new Table(10,20), ctx.getMainPane());

	for (int x = 1;x<=table.getColumns();x++){
	    for (int y = 1;y <= table.getRows();y++){
		table.setCell(ctx.addWidget(new Text(""+(x*y))), x-1, y-1);
	    }
	}
	

    }

    // TODO: These two methods must be here so the annotations are found.
    // Must find better way to do this.
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
	super.onOpen(session, config);

    }

    @OnMessage
    public void receiveEvent(Session session, String msg, boolean last) {
	super.receiveEvent(session, msg, last);
    }

}
