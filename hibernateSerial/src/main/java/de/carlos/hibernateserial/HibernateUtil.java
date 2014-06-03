package de.carlos.hibernateserial;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import de.carlos.hibernateserial.models.BaseModel;
import de.carlos.hibernateserial.models.Customer;
import de.carlos.hibernateserial.models.Order;
import de.carlos.hibernateserial.models.Product;


public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
	try {
	    sessionFactory = new AnnotationConfiguration().configure()
		    .addPackage("de.carlos.hibernateserial.models") // the fully qualified package
						 // name
		    .addAnnotatedClass(Order.class)
		    .addAnnotatedClass(Customer.class)
		    .addAnnotatedClass(Product.class)
		    .buildSessionFactory();

	} catch (Throwable ex) {
	    System.err.println("Initial SessionFactory creation failed." + ex);
	    throw new ExceptionInInitializerError(ex);
	}
    }

    public static SessionFactory getSessionFactory() {
	return sessionFactory;
    }
}