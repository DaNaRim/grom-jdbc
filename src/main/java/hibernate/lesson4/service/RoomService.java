package hibernate.lesson4.service;

import hibernate.lesson4.DAO.HotelDAO;
import hibernate.lesson4.DAO.RoomDAO;
import hibernate.lesson4.exception.BadRequestException;
import hibernate.lesson4.exception.InternalServerException;
import hibernate.lesson4.exception.NoAccessException;
import hibernate.lesson4.model.Filter;
import hibernate.lesson4.model.Hotel;
import hibernate.lesson4.model.Room;

import java.util.List;

public class RoomService {
    private static final RoomDAO roomDAO = new RoomDAO(Room.class);
    private static final HotelDAO hotelDAO = new HotelDAO(Hotel.class);
    private static final UserService userService = new UserService();

    public List<Room> findRooms(Filter filter) throws InternalServerException, BadRequestException {
        validateFilter(filter);
        return roomDAO.findRooms(filter);
    }

    public Room addRoom(Room room) throws InternalServerException, BadRequestException, NoAccessException {
        userService.checkAccess();
        validateRoom(room);
        return roomDAO.save(room);
    }

    public void deleteRoom(long roomId) throws InternalServerException, NoAccessException, BadRequestException {
        userService.checkAccess();
        Room room = roomDAO.findById(roomId);
        roomDAO.delete(room);
    }

    private void validateFilter(Filter filter) throws BadRequestException {
        if (filter == null ||
                (filter.getNumberOfGuests() == 0 && filter.getPrice() == 0 &&
                        filter.getBreakfastIncluded() == null && filter.getPetsAllowed() == null &&
                        filter.getDateAvailableFrom() == null && filter.getCountry() == null &&
                        filter.getCity() == null)) {
            throw new BadRequestException("validateFilter failed: you have not selected any options for filtering");
        }
        if (filter.getNumberOfGuests() < 0 && filter.getPrice() < 0) {
            throw new BadRequestException("validateFilter failed: you have not selected correct options for filtering");
        }
    }

    private void validateRoom(Room room) throws InternalServerException, BadRequestException {
        if (room == null) {
            throw new BadRequestException("validateRoom failed: impossible to process null room");
        }
        if (room.getNumberOfGuests() <= 0 || room.getPrice() <= 0.0 || room.isBreakfastIncluded() == null ||
                room.isPetsAllowed() == null || room.getDateAvailableFrom() == null || room.getHotel() == null) {
            throw new BadRequestException("validateRoom failed: not all fields are filled correctly");
        }
        hotelDAO.findById(room.getHotel().getId());
    }
}