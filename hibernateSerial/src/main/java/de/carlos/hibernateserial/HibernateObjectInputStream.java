package de.carlos.hibernateserial;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.apache.derby.impl.store.raw.data.SetReservedSpaceOperation;

public class HibernateObjectInputStream extends ObjectInputStream {

    public HibernateObjectInputStream(InputStream in) throws IOException {
	super(in);
	this.enableResolveObject(true);
    }
    
    @Override
    final protected Object resolveObject(Object o){
	
	if (o instanceof EntityReference){
	    o = ((EntityReference)o).load();
	}
	
	return o;
    }

}
