package hibernate.lesson4.controller;

import hibernate.lesson4.exception.BadRequestException;
import hibernate.lesson4.exception.InternalServerException;
import hibernate.lesson4.exception.NoAccessException;
import hibernate.lesson4.model.Filter;
import hibernate.lesson4.model.Room;
import hibernate.lesson4.service.RoomService;

import java.util.List;

public class RoomController {
    private static final RoomService roomService = new RoomService();

    public List<Room> findRooms(Filter filter) throws BadRequestException, InternalServerException {
        return roomService.findRooms(filter);
    }

    public Room addRoom(Room room) throws NoAccessException, InternalServerException, BadRequestException {
        return roomService.addRoom(room);
    }

    public void deleteRoom(long roomId) throws NoAccessException, InternalServerException, BadRequestException {
        roomService.deleteRoom(roomId);
    }
}