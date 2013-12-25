package de.carlos.observer;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
    
    protected List<Observer <T>> observers = new ArrayList<Observer <T>>();
    
    public void fire(T event){
	for (Observer<T> o : observers){
	    o.update(event);
	}
    }
    
    public void addObserver(Observer<T> observer){
	this.observers.add(observer);
    }
    
}
