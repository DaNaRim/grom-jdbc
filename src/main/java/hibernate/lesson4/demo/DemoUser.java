package hibernate.lesson4.demo;

import hibernate.lesson4.controller.UserController;
import hibernate.lesson4.exception.BadRequestException;
import hibernate.lesson4.exception.InternalServerException;
import hibernate.lesson4.exception.NoAccessException;
import hibernate.lesson4.model.User;
import hibernate.lesson4.model.UserType;

public class DemoUser {
    private static final UserController userController = new UserController();

    public static void main(String[] args)
            throws InternalServerException, NoAccessException, BadRequestException {

//        User user1 = new User("TEST1", "SuperPassword2", "Sweden");
//
//        userController.registerUser(user1);

//        userController.login("DANARIM", "SuperPassword");
//
//        userController.logout();
//
        userController.login("TEST1", "SuperPassword2");
//
        userController.setUserType(2L, UserType.ADMIN);
    }
}