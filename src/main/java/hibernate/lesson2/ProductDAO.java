package hibernate.lesson2;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ProductDAO {
    private static SessionFactory sessionFactory;

    public static void save(Product product) {
        //create session/tr
        //action
        //close session/tr

        Transaction tr;
        try (Session session = createSessionFactory().openSession()) {
            tr = session.getTransaction();
            tr.begin();

            session.save(product);

            tr.commit();
        } catch (HibernateException e) {
            System.err.println("Save product failed");
            System.err.println(e.getMessage());
        }
    }

    public static void delete(Product product) {
        Transaction tr;
        try (Session session = createSessionFactory().openSession()) {
            tr = session.getTransaction();
            tr.begin();

            session.delete(product);

            tr.commit();
        } catch (HibernateException e) {
            System.err.println("Delete product failed");
            System.err.println(e.getMessage());
        }
    }

    public static void update(Product product) {
        Transaction tr;
        try (Session session = createSessionFactory().openSession()) {
            tr = session.getTransaction();
            tr.begin();

            session.update(product);

            tr.commit();
        } catch (HibernateException e) {
            System.err.println("Update product failed");
            System.err.println(e.getMessage());
        }
    }

    public static void saveProducts(List<Product> products) {
        Session session = null;
        Transaction tr = null;
        try {
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();

            for (Product product : products) {
                session.save(product);
            }
            tr.commit();
        } catch (HibernateException e) {
            System.err.println("SaveProducts failed");
            System.err.println(e.getMessage());

            if (tr != null) tr.rollback();
        } finally {
            if (session != null) session.close();
        }
    }

    public static void deleteProducts(List<Product> products) {
        Session session = null;
        Transaction tr = null;
        try {
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();

            for (Product product : products) {
                session.delete(product);
            }
            tr.commit();
        } catch (HibernateException e) {
            System.err.println("DeleteProducts failed");
            System.err.println(e.getMessage());

            if (tr != null) tr.rollback();
        }  finally {
            if (session != null) session.close();
        }
    }

    public static void updateProducts(List<Product> products) {
        Session session = null;
        Transaction tr = null;
        try {
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();

            for (Product product : products) {
                session.update(product);
            }
            tr.commit();
        } catch (HibernateException e) {
            System.err.println("UpdateProducts failed");
            System.err.println(e.getMessage());

            if (tr != null) tr.rollback();
        } finally {
            if (session != null) session.close();
        }
    }

    private static SessionFactory createSessionFactory() {
        //singleton pattern
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }
}