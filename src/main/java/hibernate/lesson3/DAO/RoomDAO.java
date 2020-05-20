package hibernate.lesson3.DAO;

import hibernate.lesson3.model.Room;

public class RoomDAO extends DAO<Room> {
    public RoomDAO() {
        super(Room.class);
    }
}
