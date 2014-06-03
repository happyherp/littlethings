package de.carlos.hibernateserial;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

import de.carlos.hibernateserial.models.Product;

public class DBTest {

    @Test
    public void test() {
	
	Session s = HibernateUtil.getSessionFactory().openSession();
	
	Product p1 = new Product();
	p1.setName("waffles");
	
	
	s.beginTransaction();
	
	s.save(p1);
	
	s.getTransaction().commit();
	
	s.close();
	
	
	
	s = HibernateUtil.getSessionFactory().openSession();
	Product reloaded = (Product) s.load(Product.class, p1.getId());
	Assert.assertEquals(p1.getName(), reloaded.getName());
	
    }

}
