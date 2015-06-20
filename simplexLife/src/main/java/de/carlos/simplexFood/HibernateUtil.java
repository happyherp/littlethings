package de.carlos.simplexFood;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import de.carlos.simplexFood.food.Food;
import de.carlos.simplexFood.food.FoodNutrient;

public class HibernateUtil {

    private static transient final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(HibernateUtil.class);

    private static SessionFactory sessionFactory;
    private static final Configuration configuration;

    static {
        try {
            configuration = new Configuration()
                    .configure()
                    .addPackage("de.carlos.simplexFood")
                    // the fully qualified package name
                    .addAnnotatedClass(Food.class)
                    .addAnnotatedClass(FoodNutrient.class)
            		;

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static Transaction beginTransaction() {
       return getSessionFactory().getCurrentSession().beginTransaction();
    }

    public static void commit() {
        getSessionFactory().getCurrentSession().getTransaction().commit();
        beginTransaction();
    }

    public static void flush() {
        getSessionFactory().getCurrentSession().flush();
    }

    public static void sqlNativeQuery(String sql) {
        System.out.println("Executing " + sql);
        Iterator<Object[]> iterator = getSessionFactory().getCurrentSession().createSQLQuery(sql).list().iterator();
        while (iterator.hasNext()) {
            Object[] row = iterator.next();
            String line = "";
            for (Object o : row) {
                line += " ";
                if (o == null)
                    line += "null";
                else
                    line += o.toString();
            }
            System.out.println(line);

        }

    }

    public static void rollback() {
        getSessionFactory().getCurrentSession().getTransaction().rollback();
        beginTransaction();

    }

    public static Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

}
