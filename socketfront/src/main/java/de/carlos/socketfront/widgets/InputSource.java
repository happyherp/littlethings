package de.carlos.socketfront.widgets;

public interface InputSource<T> extends Widget {
    
    
    public T getValue();
    
    public void setValue(T value);
    
    public boolean hasValidInput();    

}
