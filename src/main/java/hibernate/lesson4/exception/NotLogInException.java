package hibernate.lesson4.exception;

public class NotLogInException extends NoAccessException {

    public NotLogInException(String message) {
        super(message);
    }
}