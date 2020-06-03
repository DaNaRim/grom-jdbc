package hibernate.lesson4.DAO;

import hibernate.lesson4.exception.BadRequestException;
import hibernate.lesson4.exception.InternalServerException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DAO<T> {
    private final Class<T> tClass;

    public DAO(Class<T> tClass) {
        this.tClass = tClass;
    }

    public T findById(long id) throws InternalServerException, BadRequestException {
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {

            T object = session.get(this.tClass, id);

            if (object == null) {
                throw new BadRequestException("findById failed: missing object with id: " + id);
            }

            return object;
        } catch (HibernateException e) {
            throw new InternalServerException("findById failed: something went wrong");
        }
    }

    public final T save(T object) throws InternalServerException {
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            session.save(object);

            transaction.commit();
            return object;
        } catch (HibernateException e) {
            throw new InternalServerException("save failed: something went wrong");
        }
    }

    public final T update(T object) throws InternalServerException {
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            session.update(object);

            transaction.commit();
            return object;
        } catch (HibernateException e) {
            throw new InternalServerException("update failed: something went wrong");
        }
    }

    public final void delete(T object) throws InternalServerException {
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            session.delete(object);

            transaction.commit();
        } catch (HibernateException e) {
            throw new InternalServerException("delete failed: something went wrong");
        }
    }
}