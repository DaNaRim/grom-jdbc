package hibernate.lesson1;

import org.hibernate.Session;

public class ProductRepository {

    public static void save(Product product) throws Exception {
        try (Session session = new HibernateUtils().createSessionFactory().openSession()) {
            session.getTransaction().begin();

            session.save(product);

            session.getTransaction().commit();
        } catch (Exception e) {
            throw new Exception("An error occurred while trying to save product " + product.getId() + " : " +
                    e.getMessage());
        }
    }

    public static void delete(long id) throws Exception {
        try (Session session = new HibernateUtils().createSessionFactory().openSession()) {
            session.getTransaction().begin();

            Product product = new Product();
            product.setId(id);
            session.delete(product);

            session.getTransaction().commit();
        } catch (Exception e) {
            throw new Exception("An error occurred while trying to save product " + id + " : " +
                    e.getMessage());
        }
    }

    public static void update(Product product) throws Exception {
        try (Session session = new HibernateUtils().createSessionFactory().openSession()) {
            session.getTransaction().begin();

            session.update(product);

            session.getTransaction().commit();
        } catch (Exception e) {
            throw new Exception("An error occurred while trying to update product " + product.getId() + " : " +
                    e.getMessage());
        }
    }
}