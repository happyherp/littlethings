package de.carlos.hibernateserial;

import java.io.Serializable;

import de.carlos.hibernateserial.models.Product;

public class SomeProcess implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private Product product;
    
    public SomeProcess(){
	
	product = new Product();
	product.setName("cherry keyboard");

	HibernateUtil.getSessionFactory().getCurrentSession().save(product);
		
    }
    
    public void resumeWork(){
	product.setName("ms keyboard");
	HibernateUtil.getSessionFactory().getCurrentSession().save(product);
	
    }
    
    Product getProduct(){
	return product;
    }

}
