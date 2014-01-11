package de.carlos.socketfront.autogui;

import java.util.List;

public interface Provider<T> {

    List<T> getAll();

    void remove(T entity);
    
    void insert(T entity);
    
    Class<T> getEntityClass();
    
    void save(T entity);
    
    T newEntity();
    
}
