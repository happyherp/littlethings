package de.carlos.socketfront.widgets;

import de.carlos.observer.Observable;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public interface InputSource<T> {
    
    public T getValue();
    
    public void setValue(T value);
    
    public boolean hasValidInput(); 
    
    public Observable<? extends ChangeEvent> getOnChange();
}
