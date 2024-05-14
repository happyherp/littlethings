package de.carlos.hibernateserial;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import de.carlos.hibernateserial.models.BaseModel;

public class HibernateObjectOutputStream extends ObjectOutputStream{
    

    public HibernateObjectOutputStream(OutputStream arg0) throws IOException {
	super(arg0);
	enableReplaceObject(true);
    }
    
    
    /**
     * 
     * Write Objects to the stream like the superclass. If a Hibernate-Entity is encountered,
     * its id and classname will be serialized instead.
     * @throws IOException 
     * 
     */
    @Override
    protected Object replaceObject(Object o) throws IOException{
	
	
	if (o instanceof BaseModel){
	    o = new EntityReference((BaseModel) o);
	}
	
	return o;

    }

}
