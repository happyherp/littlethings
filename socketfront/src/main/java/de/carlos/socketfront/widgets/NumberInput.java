package de.carlos.socketfront.widgets;


public class NumberInput extends FilterInput<Integer> {

    @Override
    public Integer getValue() {
	return Integer.parseInt(this.getStringValue());
    }

    @Override
    public boolean hasValidInput() {
	return this.getStringValue().matches("^ *-?\\d+ *$");
    }

    @Override
    public void setValue(Integer value) {
	this.setStringValue(value+"");
    }

}
