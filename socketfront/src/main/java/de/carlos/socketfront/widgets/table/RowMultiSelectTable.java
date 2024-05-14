package de.carlos.socketfront.widgets.table;

import java.util.ArrayList;
import java.util.List;

import de.carlos.observer.Observable;
import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.Checkbox;
import de.carlos.socketfront.widgets.InputSourceWidget;
import de.carlos.socketfront.widgets.JSWidget;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class RowMultiSelectTable<T> implements InputSourceWidget<List<T>> {

    private List<T> data = new ArrayList<>();

    private List<T> selected = new ArrayList<>();

    protected RowTable<T> table;

    private Observable<ChangeEvent<RowMultiSelectTable<T>>> onchange = new Observable<>();

    private RowTableDrawInstructions<T> originalInstructions;

    private GuiContext context;

    public RowMultiSelectTable(
	    final RowTableDrawInstructions<T> originalInstructions) {
	super();
	this.originalInstructions = originalInstructions;
    }

    @Override
    public JSWidget createJSWidget(GuiContext context) {

	this.context = context;

	RowTableDrawInstructions<T> modifiedInstructions = new RowTableDrawInstructions<T>() {

	    @Override
	    public List<String> createHeader() {
		List<String> headers = originalInstructions.createHeader();
		headers.add(0, "Select");
		return headers;
	    }

	    @Override
	    public List<JSWidget> createRow(GuiContext context, final T data) {
		List<JSWidget> widgets = originalInstructions.createRow(
			context, data);

		final Checkbox checkbox = new Checkbox();
		checkbox.createJSWidget(context);
		checkbox.setValue(RowMultiSelectTable.this.selected
			.contains(data));
		checkbox.getOnChange().addObserver(
			new Observer<ChangeEvent<Checkbox>>() {

			    @Override
			    public void update(ChangeEvent<Checkbox> event) {
				RowMultiSelectTable.this.handleValueChange(
					data, checkbox.getValue());
			    }
			});

		widgets.add(0, checkbox);
		return widgets;
	    }
	};

	table = new RowTable<T>(modifiedInstructions);

	table.createJSWidget(context);
	
	this.redrawData();

	return this.getMainJSWidget();
    }

    protected void handleValueChange(T object, Boolean isSelected) {
	if (isSelected) {
	    if (this.getData().contains(object))
		this.selected.add(object);
	} else {
	    this.selected.remove(object);
	}
	this.onchange.fire(new ChangeEvent<RowMultiSelectTable<T>>(this,
		this.context));

    }

    @Override
    public List<T> getValue() {
	return selected;
    }

    @Override
    public void setValue(List<T> value) {
	this.selected = new ArrayList<>(value);
	this.selected.retainAll(this.data);
	this.redrawData();

    }

    @Override
    public boolean hasValidInput() {
	return true;
    }

    @Override
    public Observable<ChangeEvent<RowMultiSelectTable<T>>> getOnChange() {
	return this.onchange;
    }

    @Override
    public JSWidget getMainJSWidget() {
	return table.getMainJSWidget();
    }

    public List<T> getData() {
	return data;
    }

    public void setData(List<T> data) {
	this.data = new ArrayList<>(data);
	this.selected.retainAll(this.data);
	this.redrawData();
    }

    public void redrawData() {
	if (this.table != null) {
	    this.table.setData(this.getData());
	    this.table.redrawData();
	}
    }

    @Override
    public void addInfo(Widget info) {
	this.table.addInfo(info);
    }

    @Override
    public void remove() {
	this.table.remove();
    }

}
