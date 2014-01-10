package de.carlos.socketfront.sample;

import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.SocketGUI;
import de.carlos.socketfront.util.OnAllValid;
import de.carlos.socketfront.util.OnAllValidHandler;
import de.carlos.socketfront.widgets.Button;
import de.carlos.socketfront.widgets.Checkbox;
import de.carlos.socketfront.widgets.Group;
import de.carlos.socketfront.widgets.NumberInput;
import de.carlos.socketfront.widgets.RadioGroup;
import de.carlos.socketfront.widgets.RadioButton;
import de.carlos.socketfront.widgets.Select;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.TextInput;
import de.carlos.socketfront.widgets.Window;
import de.carlos.socketfront.widgets.events.ChangeEvent;
import de.carlos.socketfront.widgets.events.ClickEvent;
import de.carlos.socketfront.widgets.table.Grid;

public class TestGUI implements SocketGUI {

    GuiContext context;
    int buttoncount = 1;
    TextInput textinput;
    Checkbox box;
    Select<Double> select;
    private Text selecttext;
    Grid table;
    NumberInput numberinput;
    Button squarebutton;

    @Override
    public void onCreate(GuiContext ctx) {
	this.context = ctx;

	// Create a button that makes more buttons.
	Button first = ctx.addWidget(new Button("More buttons"));

	this.context.getMainPane().add(first);

	first.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {

	    @Override
	    public void update(ClickEvent<Button> event) {
		Button newbutton = context.addWidget(new Button(
			"Button number " + buttoncount));
		buttoncount++;
		context.getMainPane().add(newbutton);

		newbutton.getOnClick().addObserver(
			new Observer<ClickEvent<Button>>() {
			    @Override
			    public void update(ClickEvent<Button> event) {
				context.getJsPipe().addStatement(
					"alert(\"Button clicked!\");");
			    }
			});
	    }
	});

	textinput = ctx.addWidget(new TextInput());
	textinput.setStringValue("Edit me!");
	context.getMainPane().add(textinput);

	Button submit = ctx.addWidget(new Button("Eingabe"));
	submit.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		context.getJsPipe().addStatement(
			"alert(\"" + textinput.getStringValue() + "\");\n");
	    }
	});
	context.getMainPane().add(submit);

	box = ctx.addWidget(new Checkbox());
	context.getMainPane().add(box);

	Button toggle = ctx.addWidget(new Button("Toggle"));
	toggle.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		box.setValue(!box.getValue());
	    }
	});
	context.getMainPane().add(toggle);

	Group selectgroup = ctx.addWidget(new Group(), ctx.getMainPane());

	selecttext = ctx.addWidget(new Text("This is a select"), selectgroup);

	select = ctx.addWidget(new Select<Double>());
	select.addOption("half", 0.5);
	select.addOption("a third", 0.3);
	select.addOption("pii", Math.PI);
	select.setValue(0.3);
	selectgroup.add(select);

	Button selectButton = ctx.addWidget(new Button("show selection"));
	selectButton.getOnClick().addObserver(
		new Observer<ClickEvent<Button>>() {
		    @Override
		    public void update(ClickEvent<Button> event) {
			selecttext.setText("You selected " + select.getValue());

		    }
		});

	selectgroup.add(selectButton);

	table = new Grid(4, 7).createJSWidget(ctx);

	for (int x = 1; x <= table.getColumns(); x++) {
	    for (int y = 1; y <= table.getRows(); y++) {
		table.setCell(ctx.addWidget(new Text("" + (x * y))), x - 1,
			y - 1);
	    }
	}

	table.setCell(context.addWidget(new Button("I am button")), 2, 6);
	context.getMainPane().add(table);

	Button removeSnd = context.addWidget(new Button("remove 2nd row"),
		context.getMainPane());
	removeSnd.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		table.removeRow(1);
	    }
	});

	Button addCol = context.addWidget(new Button("append Col"),
		context.getMainPane());
	addCol.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {

	    @Override
	    public void update(ClickEvent<Button> event) {
		table.appendColumn();
	    }
	});
	
	Button clear = context.getMainPane().add(new Button("clear").createJSWidget(ctx));
	clear.getOnClick().addObserver(new Observer<ClickEvent<Button>> () {

	    @Override
	    public void update(ClickEvent<Button> event) {
		table.clear();
	    }
	});

	Text absText = context.addWidget(new Text("I am flying over things!"),
		context.getMainPane());
	absText.setPositionAbsolute(100, 200);

	Button openWindow = context.addWidget(new Button("open window"),
		context.getMainPane());
	openWindow.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		Window window = context.addWidget(new Window(),
			context.getMainPane());
		Button closeButton = context.addWidget(new Button("Close"));
		window.add(closeButton);
		closeButton.getOnClick().addObserver(new WindowCloser(window));
	    }
	});

	numberinput = context.addWidget(new NumberInput(),
		context.getMainPane());

	squarebutton = context.addWidget(new Button("squareit"),
		context.getMainPane());
	squarebutton.getOnClick().addObserver(
		new Observer<ClickEvent<Button>>() {
		    @Override
		    public void update(ClickEvent<Button> event) {
			if (numberinput.hasValidInput()) {
			    numberinput.setValue(numberinput.getValue()
				    * numberinput.getValue());
			    ;
			} else {
			    context.getJsPipe().addCall("alert",
				    "Please enter a number");
			}
		    }
		});

	new OnAllValid(new OnAllValidHandler() {

	    @Override
	    public void onInvalid() {
		squarebutton.setDisabled(true);
	    }

	    @Override
	    public void onAllValid() {
		squarebutton.setDisabled(false);
	    }
	}, numberinput);

	Button toggleEnabled = context.addWidget(new Button("toggle disabled"),
		context.getMainPane());
	toggleEnabled.getOnClick().addObserver(
		new Observer<ClickEvent<Button>>() {
		    @Override
		    public void update(ClickEvent<Button> event) {
			numberinput.setDisabled(!numberinput.isDisabled());
			select.setDisabled(!select.isDisabled());
			box.setDisabled(!box.isDisabled());
			squarebutton.setDisabled(!squarebutton.isDisabled());
		    }
		});

	Grid grid = context.addWidget(new Grid(2, 0), context.getMainPane());
	for (int i = 0; i < 6; i++) {
	    grid.addRow(3);
	    grid.setCell(context.addWidget(new Text(i + "")), 0,
		    Math.min(grid.getRows() - 1, 3));
	    grid.setCell(context.addWidget(new Text(i * 2 + "")), 1,
		    Math.min(grid.getRows() - 1, 3));
	}

	final RadioGroup<Integer> group = new RadioGroup<>(this.context);

	for (int i = 0; i < 4; i++) {
	    RadioButton<Integer> radiobutton = group.newRadio(i);
	    context.getMainPane().add(radiobutton);
	    radiobutton.getOnChange().addObserver(new Observer<ChangeEvent<RadioButton<Integer>>>(){

		@Override
		public void update(ChangeEvent<RadioButton<Integer>> event) {
		    context.alert("Value of radiobutton with "+ event.getSource().getObject() + " changed to "+ event.getSource().getValue());
		}

	
	    });
	}
	Button showRadio = context.addWidget(new Button("showResult."), this.context.getMainPane());
	showRadio.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {

	    @Override
	    public void update(ClickEvent<Button> event) {
		context.getJsPipe().addCall("alert", group.getValue());
	    }
	});
	
	
	Grid fromempty = new Grid(10, 10).createJSWidget(ctx);
	context.getMainPane().add(fromempty);
	
	fromempty.clear();
	fromempty.appendRow();
	fromempty.appendColumn();
	fromempty.setCell(new Text("Hello").createJSWidget(ctx), 0, 0);
	


    }

    private class WindowCloser implements Observer<ClickEvent<Button>> {

	Window window;

	public WindowCloser(Window window) {
	    this.window = window;
	}

	@Override
	public void update(ClickEvent<Button> event) {
	    this.window.close();
	}

    }

}
