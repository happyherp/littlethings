package de.carlos.socketfront.widgets.table;

import java.util.List;

import de.carlos.observer.Observable;
import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.InputSource;
import de.carlos.socketfront.widgets.RadioButton;
import de.carlos.socketfront.widgets.RadioGroup;
import de.carlos.socketfront.widgets.Widget;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class RowSelectTable<T> implements WidgetComposition, InputSource<T> {

    RowTable<T> table;

    RowTableDrawInstructions<T> drawInstructions;

    RadioGroup<T> radiogroup;
    
    Observable<ChangeEvent<RowSelectTable<T>>> onchange = new Observable<ChangeEvent<RowSelectTable<T>>>();

    private List<T> data;

    private T value;

    @Override
    public void create(GuiContext context) {
	radiogroup = new RadioGroup<T>(context);
	radiogroup.getOnChange().addObserver(new Observer<ChangeEvent<RadioButton<T>>>() {

	    @Override
	    public void update(ChangeEvent<RadioButton<T>> event) {
		RowSelectTable.this.value = event.getSource().getObject();
		RowSelectTable.this.getOnChange().fire(new ChangeEvent<RowSelectTable<T>>(RowSelectTable.this, event.getContext()));
	    }
	});

	RowTableDrawInstructions<T> withselection = addSelectionToDrawInstructions();
	
	table = new RowTable<T>(withselection);
	table.setData(this.getData());
	table.create(context);
	
	radiogroup.setValue(this.getValue());

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
	    public List<Widget> createRow(GuiContext context, T data) {
		List<Widget> widgets = RowSelectTable.this.drawInstructions
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
    public Grid getMainWidget() {
	return this.table.getMainWidget();
    }

    @Override
    public T getValue() {
	return this.value;
    }

    @Override
    public void setValue(T value) {
	this.value = value;
	if (this.radiogroup != null){
	    this.radiogroup.setValue(value);	
	}
    }

    @Override
    public boolean hasValidInput() {
	return this.getValue() != null;
    }

    @Override
    public  Observable<ChangeEvent<RowSelectTable<T>>> getOnChange() {
	return this.onchange;
    }
    
    public List<T> getData(){
	return this.data;
    }

    public void setData(List<T> data) {
	this.data = data;
    }

}
