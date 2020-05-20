package hibernate.lesson3;

import hibernate.lesson3.DAO.HotelDAO;
import hibernate.lesson3.DAO.RoomDAO;
import hibernate.lesson3.model.Hotel;
import hibernate.lesson3.model.Room;

import java.util.Date;

public class Demo {
    public static void main(String[] args) throws Exception {
        Hotel hotel = new Hotel("2", "Test", "Test", "Test");
        Room room = new Room(2, 202.00, 1, 1, new Date(), hotel);

        HotelDAO hotelDAO = new HotelDAO();
        RoomDAO roomDAO = new RoomDAO();

        hotelDAO.save(hotel);
        roomDAO.save(room);

        hotel.setCity("Test2");
        room.setNumberOfGuests(3);
        hotelDAO.update(hotel);
        roomDAO.update(room);

        hotelDAO.findById(2);
        roomDAO.findById(2);

        roomDAO.delete(2);
        hotelDAO.delete(2);
    }
}
