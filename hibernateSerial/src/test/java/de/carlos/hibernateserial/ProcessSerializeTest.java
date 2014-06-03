package de.carlos.hibernateserial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Test;

public class ProcessSerializeTest {
    
    @Test
    public void test() throws IOException, ClassNotFoundException{
	
	HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
	
	SomeProcess process = new SomeProcess();
	
	HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
	HibernateUtil.getSessionFactory().getCurrentSession().close();
	
	
	ByteArrayOutputStream byteoutstream = new ByteArrayOutputStream();
	ObjectOutputStream objectoutstream = new HibernateObjectOutputStream(byteoutstream);
	
	objectoutstream.writeObject(process);	
	objectoutstream.close();
	
	HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	ObjectInputStream objectinputstream =
		new HibernateObjectInputStream(new ByteArrayInputStream(byteoutstream.toByteArray()));	
	SomeProcess reloaded = (SomeProcess) objectinputstream.readObject();
	objectinputstream.close();
	
	
	

	Assert.assertEquals("cherry keyboard", process.getProduct().getName());

	
	reloaded.resumeWork();
	
	Assert.assertEquals("ms keyboard", reloaded.getProduct().getName());
	
    }
    
    
    //@Test
    public void testOld() throws IOException, ClassNotFoundException{
	
	HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
	
	SomeProcess process = new SomeProcess();
	
	HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
	HibernateUtil.getSessionFactory().getCurrentSession().close();
	
	
	ByteArrayOutputStream byteoutstream = new ByteArrayOutputStream();
	ObjectOutputStream objectoutstream = new ObjectOutputStream(byteoutstream);
	
	objectoutstream.writeObject(process);
	
	objectoutstream.close();
	ObjectInputStream objectinputstream =
		new ObjectInputStream(new ByteArrayInputStream(byteoutstream.toByteArray()));
	
	SomeProcess reloaded = (SomeProcess) objectinputstream.readObject();
	objectinputstream.close();
	
	
	
	HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

	Assert.assertEquals("cherry keyboard", process.getProduct().getName());

	
	reloaded.resumeWork();
	
	Assert.assertEquals("ms keyboard", process.getProduct().getName());
	
    }

}
