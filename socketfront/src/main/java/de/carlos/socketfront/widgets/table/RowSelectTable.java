package de.carlos.socketfront.widgets.table;

import java.util.List;

import de.carlos.observer.Observable;
import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.InputSource;
import de.carlos.socketfront.widgets.RadioButton;
import de.carlos.socketfront.widgets.RadioGroup;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.JSWidget;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class RowSelectTable<T> implements Widget, InputSource<T> {

    RowTable<T> table;

    RowTableDrawInstructions<T> drawInstructions;

    RadioGroup<T> radiogroup;

    Observable<ChangeEvent<RowSelectTable<T>>> onchange = new Observable<ChangeEvent<RowSelectTable<T>>>();

    private List<T> data;

    private T value;

    private GuiContext context;

    @Override
    public Grid createJSWidget(GuiContext context) {
	this.context = context;

	RowTableDrawInstructions<T> withselection = addSelectionToDrawInstructions();
	table = new RowTable<T>(withselection);
	table.createJSWidget(context);

	this.refresh();

	return table.getMainJSWidget();

    }

    protected RowTableDrawInstructions<T> addSelectionToDrawInstructions() {

	RowTableDrawInstructions<T> withselection = new RowTableDrawInstructions<T>() {

	    @Override
	    public List<String> createHeader() {

		List<String> headers = RowSelectTable.this.drawInstructions
			.createHeader();
		headers.add(0, "Select");
		return headers;
	    }

	    @Override
	    public List<JSWidget> createRow(GuiContext context, T data) {
		List<JSWidget> widgets = RowSelectTable.this.drawInstructions
			.createRow(context, data);

		RadioButton<T> button = RowSelectTable.this.radiogroup
			.newRadio(data);

		widgets.add(0, button);

		return widgets;
	    }
	};

	return withselection;
    }

    public void setDrawInstructions(
	    final RowTableDrawInstructions<T> drawInstructions) {
	this.drawInstructions = drawInstructions;
    }

    @Override
    public T getValue() {
	return this.value;
    }

    @Override
    public void setValue(T value) {
	this.value = value;
	if (this.radiogroup != null) {
	    this.radiogroup.setValue(value);
	}
    }

    @Override
    public boolean hasValidInput() {
	return this.getValue() != null;
    }

    @Override
    public Observable<ChangeEvent<RowSelectTable<T>>> getOnChange() {
	return this.onchange;
    }

    public List<T> getData() {
	return this.data;
    }

    public void setData(List<T> data) {
	this.data = data;
    }

    @Override
    public JSWidget getMainJSWidget() {
	return this.table.getMainJSWidget();
    }

    public void refresh() {

	radiogroup = new RadioGroup<T>(context);
	radiogroup.getOnChange().addObserver(
		new Observer<ChangeEvent<RadioButton<T>>>() {

		    @Override
		    public void update(ChangeEvent<RadioButton<T>> event) {
			RowSelectTable.this.value = event.getSource()
				.getObject();
			RowSelectTable.this
				.getOnChange()
				.fire(new ChangeEvent<RowSelectTable<T>>(
					RowSelectTable.this, event.getContext()));
		    }
		});
	table.setData(this.getData());

	table.redrawData();

	if (this.radiogroup.canBeUsedAsValue(this.getValue())) {
	    radiogroup.setValue(this.getValue());
	}else{
	    this.value = null;
	}
    }

}
