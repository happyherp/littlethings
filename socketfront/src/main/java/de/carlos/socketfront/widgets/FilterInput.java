package de.carlos.socketfront.widgets;

public abstract class FilterInput<T> extends TextInput {
    
    protected InfoText invalidmarker = null;
    
    public abstract T getFilteredValue();
    
    public abstract void setFilteredValue(T value);
    
    public abstract boolean hasValidInput();
    
    @Override
    protected void setValueInner(String value){
	super.setValueInner(value);
	if (this.hasValidInput() && invalidmarker != null){
	    invalidmarker.remove();
	    invalidmarker = null;
	}else if (!this.hasValidInput() && invalidmarker == null){
	    this.invalidmarker = this.getContext().addWidget(new InfoText("Please enter a number. "));
	    this.addInfoText(this.invalidmarker);
	}
    }



}
