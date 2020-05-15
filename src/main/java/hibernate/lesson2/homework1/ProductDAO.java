package hibernate.lesson2.homework1;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ProductDAO {
    private static SessionFactory sessionFactory;

    public static Product findById(long id) throws Exception {
        try (Session session = createSessionFactory().openSession()) {

            Query query = session.createQuery("FROM Product WHERE id = :Id");
            query.setParameter("Id", id);
            return (Product) query.getSingleResult();

        } catch (HibernateException e) {
            throw new Exception("findById failed" + e.getMessage());
        }
    }

    public static List<Product> findByName(String name) throws Exception {
        try (Session session = createSessionFactory().openSession()) {

            Query query = session.createQuery("FROM Product WHERE name = :Name");
            query.setParameter("Name", name);
            return query.list();

        } catch (HibernateException e) {
            throw new Exception("findByName failed" + e.getMessage());
        }
    }

    public static List<Product> findByContainedName(String name) throws Exception {
        try (Session session = createSessionFactory().openSession()) {

            Query query = session.createQuery("FROM Product WHERE name LIKE CONCAT('%', :ContainedName, '%')");
            query.setParameter("ContainedName", name);
            return query.list();

        } catch (HibernateException e) {
            throw new Exception("findByContainedName failed" + e.getMessage());
        }
    }

    public static List<Product> findByPrice(int price, int delta) throws Exception {
        try (Session session = createSessionFactory().openSession()) {

            Query query = session.createQuery("FROM Product WHERE price BETWEEN :minValue AND :maxValue");
            query.setParameter("minValue", price - delta);
            query.setParameter("maxValue", price + delta);
            return query.list();

        } catch (HibernateException e) {
            throw new Exception("findByPrice failed" + e.getMessage());
        }
    }

    public static List<Product> findByNameSortedAsc(String name) throws Exception {
        try (Session session = createSessionFactory().openSession()) {

            Query query = session.createQuery("FROM Product WHERE name LIKE CONCAT('%', :ContainedName, '%') ORDER BY name ASC");
            query.setParameter("ContainedName", name);
            return query.list();

        } catch (HibernateException e) {
            throw new Exception("findByNameSortedAsc failed" + e.getMessage());
        }
    }

    public static List<Product> findByNameSortedDesc(String name) throws Exception {
        try (Session session = createSessionFactory().openSession()) {

            Query query = session.createQuery("FROM Product WHERE name LIKE CONCAT('%', :ContainedName, '%') ORDER BY name DESC");
            query.setParameter("ContainedName", name);
            return query.list();

        } catch (HibernateException e) {
            throw new Exception("findByNameSortedDesc failed" + e.getMessage());
        }
    }

    public static List<Product> findByPriceSortedDesc(int price, int delta) throws Exception {
        try (Session session = createSessionFactory().openSession()) {

            Query query = session.createQuery("FROM Product WHERE price BETWEEN :minValue AND :maxValue ORDER BY price DESC");
            query.setParameter("minValue", price - delta);
            query.setParameter("maxValue", price + delta);
            return query.list();

        } catch (HibernateException e) {
            throw new Exception("findByPriceSortedDesc failed" + e.getMessage());
        }
    }

    private static SessionFactory createSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }
}