package hibernate.lesson4.DAO;

import hibernate.lesson4.exception.BadRequestException;
import hibernate.lesson4.exception.InternalServerException;
import hibernate.lesson4.model.Hotel;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.util.List;

public class HotelDAO extends DAO<Hotel> {
    private static final String findHotelByNameQuery = "SELECT * FROM HOTEL WHERE NAME = :name";
    private static final String findHotelByCityQuery = "SELECT * FROM HOTEL WHERE CITY = :city";
    private static final String isHotelExistQuery = "SELECT * FROM HOTEL WHERE NAME = :name AND COUNTRY = :country AND CITY = :city AND STREET = :street";

    public HotelDAO(Class<Hotel> hotelClass) {
        super(hotelClass);
    }

    public List<Hotel> findHotelByName(String name) throws InternalServerException, BadRequestException {
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {

            List<Hotel> hotels = session.createNativeQuery(findHotelByNameQuery, Hotel.class)
                    .setParameter("name", name)
                    .list();

            if (hotels.size() == 0) {
                throw new BadRequestException("findHotelByName failed: there is no hotels with this parameters");
            }

            return hotels;
        } catch (HibernateException e) {
            throw new InternalServerException("findHotelByName failed: something went wrong: " + e.getMessage());
        }
    }

    public List<Hotel> findHotelByCity(String city) throws InternalServerException, BadRequestException {
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {

            List<Hotel> hotels = session.createNativeQuery(findHotelByCityQuery, Hotel.class)
                    .setParameter("city", city)
                    .list();

            if (hotels.size() == 0) {
                throw new BadRequestException("findHotelByCity failed: there is no hotels with this parameters");
            }

            return hotels;
        } catch (HibernateException e) {
            throw new InternalServerException("findHotelByCity failed: something went wrong: " + e.getMessage());
        }
    }

    public void isHotelExist(Hotel hotel) throws InternalServerException, BadRequestException {
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {

            Hotel hotel1 = session.createNativeQuery(isHotelExistQuery, Hotel.class)
                    .setParameter("name", hotel.getName())
                    .setParameter("country", hotel.getCountry())
                    .setParameter("city", hotel.getCity())
                    .setParameter("street", hotel.getStreet())
                    .getSingleResult();

            throw new BadRequestException("isHotelExist failed: the hotel is already exist. Id - : " + hotel1.getId());

        } catch (NoResultException e) {
            System.out.println("isHotelExist: Object not found in database. Will be saved");
        } catch (HibernateException e) {
            throw new InternalServerException("isHotelExist failed: something went wrong: " + e.getMessage());
        }
    }
}