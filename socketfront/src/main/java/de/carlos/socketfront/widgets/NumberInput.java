package de.carlos.socketfront.widgets;


public class NumberInput extends FilterInput<Integer> {

    @Override
    public Integer getFilteredValue() {
	return Integer.parseInt(this.getValue());
    }

    @Override
    public boolean hasValidInput() {
	return this.getValue().matches("^ *-?\\d+ *$");
    }

    @Override
    public void setFilteredValue(Integer value) {
	this.setValue(value+"");
    }

}
