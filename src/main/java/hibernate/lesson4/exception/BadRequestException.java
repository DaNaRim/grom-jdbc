package hibernate.lesson4.exception;

public class BadRequestException extends Exception {

    public BadRequestException(String message) {
        super(message);
    }
}