package hibernate.lesson3.DAO;

import hibernate.lesson3.model.Hotel;

public class HotelDAO extends DAO<Hotel> {
    public HotelDAO() {
        super(Hotel.class);
    }
}
