package hibernate.lesson4.demo;

import hibernate.lesson4.controller.OrderController;
import hibernate.lesson4.controller.UserController;
import hibernate.lesson4.exception.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DemoOrder {
    private static final OrderController orderController = new OrderController();
    private static final UserController userController = new UserController();

    public static void main(String[] args)
            throws NoAccessException, InternalServerException, BadRequestException, ParseException {

        userController.login("TEST1", "SuperPassword2");

//        userController.login("DANARIM", "SuperPassword");

//        orderController.bookRoom(5, 1,
//                new SimpleDateFormat("dd.MM.yyyy kk:00").parse("27.06.2020 12:00"),
//                new SimpleDateFormat("dd.MM.yyyy kk:00").parse("29.06.2020 12:00"));

        orderController.cancelReservation(5, 2);
    }
}