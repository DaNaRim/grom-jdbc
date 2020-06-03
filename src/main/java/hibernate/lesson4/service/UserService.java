package hibernate.lesson4.service;

import hibernate.lesson4.DAO.UserDAO;
import hibernate.lesson4.exception.BadRequestException;
import hibernate.lesson4.exception.InternalServerException;
import hibernate.lesson4.exception.NoAccessException;
import hibernate.lesson4.exception.NotLogInException;
import hibernate.lesson4.model.User;
import hibernate.lesson4.model.UserType;

public class UserService {
    private static final UserDAO userDAO = new UserDAO(User.class);
    private static User loggedUser = null;

    public User registerUser(User user) throws InternalServerException, BadRequestException {
        validateUser(user);
        return userDAO.save(user);
    }

    public void login(String userName, String password) throws InternalServerException, BadRequestException {
        validateLogin(userName);
        loggedUser = userDAO.logIn(userName, password);
    }

    public void logout() throws NotLogInException {
        checkLogIn();
        loggedUser = null;
    }

    public void setUserType(Long id, UserType userType)
            throws NoAccessException, BadRequestException, InternalServerException {
        checkAccess();
        User user = userDAO.findById(id);

        validateUserType(user, userType);
        try {
            user.setUserTypeEnum(userType);
            userDAO.update(user);
        } catch (InternalServerException e) {
            throw new InternalServerException("setUserType failed: " + e.getMessage());
        }
    }

    public void checkLogIn() throws NotLogInException {
        if (loggedUser == null) throw new NotLogInException("checkLogIn failed: user is not log in");
    }

    public void checkAccess() throws NoAccessException {
        checkLogIn();
        if (loggedUser.getUserTypeEnum() != UserType.ADMIN) {
            throw new NoAccessException("checkAccess failed: user don`t have enough rights");
        }
    }

    public void checkUserForOperation(Long id) throws NoAccessException {
        checkLogIn();
        if (!loggedUser.getId().equals(id)) {
            throw new NoAccessException("checkUserForOperation failed: user cannot do this operation in the name of " +
                    "another user");
        }
    }

    private void validateLogin(String userName) throws BadRequestException {
        if (loggedUser != null) {
            if (loggedUser.getUserName().equals(userName)) {
                throw new BadRequestException("validateLogin failed: user already log in");
            }
            throw new BadRequestException("validateLogin failed: another user is logged in now");
        }
    }

    private void validateUser(User user) throws BadRequestException, InternalServerException {
        if (user == null) {
            throw new BadRequestException("validateUser failed: impossible to process null user");
        }
        if (user.getUserName() == null || user.getPassword() == null || user.getCountry() == null ||
                user.getUserName().equals("") || user.getPassword().equals("") || user.getCountry().equals("")) {
            throw new BadRequestException("validateUser failed: not all fields are filled");
        }

        userDAO.checkUsername(user.getUserName());

        if (user.getPassword().length() < 8) {
            throw new BadRequestException("validateUser failed: password must be at least 8 characters");
        }
        if (user.getUserName().length() > 25) {
            throw new BadRequestException("validateUser failed: username must be no more than 25 characters");
        }
        if (!user.getUserName().equals(user.getUserName().trim()) ||
                !user.getPassword().equals(user.getPassword().trim()) ||
                !user.getCountry().equals(user.getCountry().trim())) {
            throw new BadRequestException("validateUser failed: fields must not begin and end with spaces");
        }
        if (user.getUserName().contains(", ") || user.getPassword().contains(", ") || user.getCountry().contains(", ")) {
            throw new BadRequestException("validateUser failed: fields must not have ', '");
        }
    }

    private void validateUserType(User user, UserType userType) throws BadRequestException {
        if (user.getUserTypeEnum() == userType) {
            throw new BadRequestException("validateUserType failed: user already has this type");
        }
    }
}