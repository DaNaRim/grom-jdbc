package hibernate.lesson4.service;

import hibernate.lesson4.DAO.OrderDAO;
import hibernate.lesson4.DAO.RoomDAO;
import hibernate.lesson4.DAO.UserDAO;
import hibernate.lesson4.exception.BadRequestException;
import hibernate.lesson4.exception.InternalServerException;
import hibernate.lesson4.exception.NoAccessException;
import hibernate.lesson4.model.Order;
import hibernate.lesson4.model.Room;
import hibernate.lesson4.model.User;

import java.util.Date;

public class OrderService {
    private static final OrderDAO orderDAO = new OrderDAO(Order.class);
    private static final RoomDAO roomDAO = new RoomDAO(Room.class);
    private static final UserDAO userDAO = new UserDAO(User.class);
    private static final UserService userService = new UserService();

    public void bookRoom(long roomId, long userId, Date dateFrom, Date dateTo)
            throws InternalServerException, NoAccessException, BadRequestException {
        validateRoomAndUser(roomId, userId);
        userService.checkUserForOperation(userId);
        validateOrder(roomId, dateFrom, dateTo);

        Order order = createOrder(roomId, userId, dateFrom, dateTo);
        orderDAO.save(order);
    }

    public void cancelReservation(long roomId, long userId)
            throws InternalServerException, NoAccessException, BadRequestException {
        validateRoomAndUser(roomId, userId);
        userService.checkUserForOperation(userId);
        validateCancellation(roomId, userId);

        Order order = orderDAO.findOrderByRoomAndUser(roomId, userId);
        orderDAO.delete(order);
    }

    private void validateCancellation(long roomId, long userId)
            throws InternalServerException, BadRequestException {
        Date orderDateFrom = orderDAO.findOrderByRoomAndUser(roomId, userId).getDateFrom();

        if (orderDateFrom.before(new Date())) {
            throw new BadRequestException("validateCancellation failed: possible cancellation has expired");
        }
    }

    private void validateOrder(long roomId, Date dateFrom, Date dateTo)
            throws InternalServerException, BadRequestException {
        if (dateFrom == null || dateTo == null) {
            throw new BadRequestException("validateOrder failed: not all fields are filled correctly");
        }
        if (dateTo.before(dateFrom) || dateTo.equals(dateFrom) || dateFrom.before(new Date())) {
            throw new BadRequestException("validateOrder failed: date filled is incorrect");
        }
        orderDAO.checkRoomForBusy(roomId, dateFrom, dateTo);
    }

    private void validateRoomAndUser(long roomId, long userId) throws InternalServerException, BadRequestException {
        roomDAO.findById(roomId);
        userDAO.findById(userId);
    }

    private Order createOrder(long roomId, long userId, Date dateFrom, Date dateTo)
            throws InternalServerException, BadRequestException {
        return new Order(
                userDAO.findById(userId),
                roomDAO.findById(roomId),
                dateFrom,
                dateTo,
                roomDAO.findById(roomId).getPrice());
    }
}