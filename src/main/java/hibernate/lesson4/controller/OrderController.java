package hibernate.lesson4.controller;

import hibernate.lesson4.exception.BadRequestException;
import hibernate.lesson4.exception.InternalServerException;
import hibernate.lesson4.exception.NoAccessException;
import hibernate.lesson4.service.OrderService;

import java.util.Date;

public class OrderController {
    private static final OrderService orderService = new OrderService();

    public void bookRoom(long roomId, long userId, Date dateFrom, Date dateTo)
            throws NoAccessException, InternalServerException, BadRequestException {
        orderService.bookRoom(roomId, userId, dateFrom, dateTo);
    }

    public void cancelReservation(long roomId, long userId)
            throws NoAccessException, InternalServerException, BadRequestException {
        orderService.cancelReservation(roomId, userId);
    }
}