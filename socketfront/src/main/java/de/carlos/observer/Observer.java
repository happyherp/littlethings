package de.carlos.observer;

public interface Observer<T> {
    
    public void update(T event);

}
