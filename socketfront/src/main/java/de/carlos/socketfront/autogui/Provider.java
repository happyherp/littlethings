package de.carlos.socketfront.autogui;

import java.util.List;

public interface Provider<T> {

    List<T> getAll();

    void remove(T entity);

    T findById(int id);
    
    void create(T entity);
    
    Class<T> getEntityClass();
    
}
