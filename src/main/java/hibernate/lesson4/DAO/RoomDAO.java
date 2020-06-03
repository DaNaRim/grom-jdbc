package hibernate.lesson4.DAO;

import hibernate.lesson4.exception.BadRequestException;
import hibernate.lesson4.exception.InternalServerException;
import hibernate.lesson4.model.Filter;
import hibernate.lesson4.model.Hotel;
import hibernate.lesson4.model.Room;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO extends DAO<Room> {

    public RoomDAO(Class<Room> roomClass) {
        super(roomClass);
    }

    public List<Room> findRooms(Filter filter) throws InternalServerException, BadRequestException {
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Room> criteria = builder.createQuery(Room.class);

            Root<Room> roomRoot = criteria.from(Room.class);
            Join<Room, Hotel> hotelJoin = roomRoot.join("hotel");

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getNumberOfGuests() > 0) {
                predicates.add(builder.equal(roomRoot.get("numberOfGuests"), filter.getNumberOfGuests()));
            }
            if (filter.getPrice() > 0.0) {
                predicates.add(builder.equal(roomRoot.get("price"), filter.getPrice()));
            }
            if (filter.getBreakfastIncluded() != null) {
                predicates.add(builder.equal(roomRoot.get("breakfastIncluded"), filter.getBreakfastIncluded() ? 1 : 0));
            }
            if (filter.getPetsAllowed() != null) {
                predicates.add(builder.equal(roomRoot.get("petsAllowed"), filter.getPetsAllowed() ? 1 : 0));
            }
            if (filter.getDateAvailableFrom() != null) {
                predicates.add(builder.equal(roomRoot.get("dateAvailableFrom"), filter.getDateAvailableFrom()));
            }
            if (filter.getCountry() != null) {
                predicates.add(builder.equal(hotelJoin.get("country"), filter.getCountry()));
            }
            if (filter.getCity() != null) {
                predicates.add(builder.equal(hotelJoin.get("city"), filter.getCity()));
            }

            criteria.select(roomRoot).where(predicates.toArray(new Predicate[0]));

            List<Room> rooms = session.createQuery(criteria).getResultList();

            if (rooms.size() == 0) {
                throw new BadRequestException("findRooms failed: there is no room with this filter parameters");
            }

            return rooms;
        } catch (HibernateException e) {
            throw new InternalServerException("findRooms failed: something went wrong: " + e.getMessage());
        }
    }
}