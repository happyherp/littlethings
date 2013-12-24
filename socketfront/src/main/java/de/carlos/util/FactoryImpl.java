package de.carlos.util;

import java.lang.reflect.InvocationTargetException;

public class FactoryImpl<T> implements Factory<T> {
    
    Class<? extends T> clazz;
    
    public FactoryImpl(Class<? extends T> clazz){
	this.clazz = clazz;
    }

    @Override
    public T create() {
	
	 try {
	    return clazz.getConstructor().newInstance();
	} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	    throw new RuntimeException(e);
	}
	
	
    }

}
