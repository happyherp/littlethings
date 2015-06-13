package de.carlos.simplexFood;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
	try {
	    sessionFactory = new Configuration().configure()
		    .addPackage("de.carlos.hibernateserial.models") // the fully qualified package
						 // name
		    .addAnnotatedClass(Food.class)
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