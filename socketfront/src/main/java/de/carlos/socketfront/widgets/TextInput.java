package de.carlos.socketfront.widgets;

public class TextInput extends Input<String> implements InputSource<String> {

    @Override
    public String getValue() {
	return this.getStringValue();
    }

    @Override
    public void setValue(String value) {
	this.setStringValue(value);
    }

    @Override
    public boolean hasValidInput() {
	return true;
    }

    @Override
    InputSource<String> getThis() {
	return this;
    }

}
