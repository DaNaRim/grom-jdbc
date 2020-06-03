package hibernate.lesson4.DAO;

import hibernate.lesson4.exception.BadRequestException;
import hibernate.lesson4.exception.InternalServerException;
import hibernate.lesson4.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.util.List;

public class UserDAO extends DAO<User> {
    private static final String logInQuery = "SELECT * FROM USERS WHERE USERNAME = :username";
    private static final String checkUsernameQuery = "SELECT * FROM USERS WHERE USERNAME = :username";

    public UserDAO(Class<User> userClass) {
        super(userClass);
    }

    public User logIn(String userName, String password) throws InternalServerException, BadRequestException {
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {

            List<User> users = session.createNativeQuery(logInQuery, User.class)
                    .setParameter("username", userName)
                    .list();

            if (users.size() == 0) {
                throw new BadRequestException("logIn failed: wrong username or user not registered");
            }

            for (User user : users) {
                if (user.getPassword().equals(password)) {
                    return user;
                }
            }
            throw new BadRequestException("logIn failed: wrong password");

        } catch (HibernateException e) {
            throw new InternalServerException("logIn failed: something went wrong: " + e.getMessage());
        }
    }

    public void checkUsername(String userName) throws InternalServerException, BadRequestException {
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {

            session.createNativeQuery(checkUsernameQuery, User.class)
                    .setParameter("username", userName)
                    .getSingleResult();

            throw new BadRequestException("checkUsername failed: username is already taken");

        } catch (NoResultException e) {
            System.out.println("checkUsername: Object not found in database. Will be saved");
        } catch (HibernateException e) {
            throw new InternalServerException("checkUsername failed: something went wrong: " + e.getMessage());
        }
    }
}