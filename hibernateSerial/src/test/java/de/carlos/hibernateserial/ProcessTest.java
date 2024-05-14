package de.carlos.hibernateserial;

import org.junit.Assert;
import org.junit.Test;

public class ProcessTest {
    
    @Test
    public void test(){
	
	
	HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
	
	SomeProcess process = new SomeProcess();
	
	HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
	HibernateUtil.getSessionFactory().getCurrentSession().close();
	
	HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
	
	
	process.resumeWork();
	
	Assert.assertEquals("ms keyboard", process.getProduct().getName());
	
    }

}
